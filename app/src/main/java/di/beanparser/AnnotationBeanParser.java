package di.beanparser;

import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.annotations.Bean;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithType;
import di.container.dependency.GenericInjectableMethod;
import di.container.dependency.InjectableConstructor;
import di.container.dependency.InjectableConstructorImpl;
import di.container.dependency.InjectableMethod;
import di.container.dependency.ProviderDependency;
import di.sourcesscanner.DirectorySourcesScanner;
import di.sourcesscanner.JarSourcesScanner;
import di.sourcesscanner.SourcesScanner;
import di.sourcesscanner.SourcesScannerException;
import di.sourcesscanner.Utils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationBeanParser implements BeanParser {
    private final BeanFactory beanFactory = new BeanFactory();

    public AnnotationBeanParser() throws BeanParserException {
        this("", new Class<?>[]{});
    }

    public AnnotationBeanParser(Class<?>... startupClasses) throws BeanParserException {
        this("", startupClasses);
    }

    public AnnotationBeanParser(String directory) throws BeanParserException {
        this(directory, new Class<?>[]{});
    }

    public AnnotationBeanParser(String directory, Class<?>... startupClasses) throws BeanParserException {
        try {
            File codeSourceFile = Utils.getCodeSourceFile(getClass());
            SourcesScanner sourcesScanner =
                    !codeSourceFile.isDirectory() && codeSourceFile.toString().endsWith(".jar") ?
                            new JarSourcesScanner(directory, codeSourceFile) :
                            new DirectorySourcesScanner(directory, startupClasses);
            parseClasses(sourcesScanner.scan());
        } catch (SourcesScannerException e) {
            throw new BeanParserException(e);
        }
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private void parseClasses(Set<Class<?>> classes) throws BeanParserException {
        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();
        for (Class<?> clazz : classes) {
            Bean beanAnnotation = clazz.getAnnotation(Bean.class);
            if (beanAnnotation == null) {
                continue;
            }
            BeanDescription beanDescription = new BeanDescription(
                    beanAnnotation.lifecycle(),
                    clazz,
                    getInjectableConstructors(clazz),
                    getFieldDependencies(clazz),
                    getInjectableMethods(clazz)
            );
            Named namedAnnotation = clazz.getAnnotation(Named.class);
            if (namedAnnotation == null) {
                beanSet.add(beanDescription);
            } else {
                beanMap.put(namedAnnotation.value(), beanDescription);
            }
        }
        beanFactory.setBeanDescriptions(beanMap);
        beanFactory.setBeanDescriptionSet(beanSet);
    }

    private List<InjectableConstructor> getInjectableConstructors(Class<?> clazz) throws BeanParserException {
        List<InjectableConstructor> injectableConstructors = new ArrayList<>();

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 1) {
            injectableConstructors.add(getInjectableConstructor(constructors[0]));
        } else {
            for (Constructor<?> constructor : constructors) {
                Inject injectAnnotation = constructor.getAnnotation(Inject.class);
                if (injectAnnotation == null) {
                    continue;
                }
                injectableConstructors.add(getInjectableConstructor(constructor));
            }
        }
        return injectableConstructors;
    }

    private List<Dependency> getFieldDependencies(Class<?> clazz) throws BeanParserException {
        List<Dependency> fieldDependencies = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            Inject injectAnnotation = field.getAnnotation(Inject.class);
            if (injectAnnotation == null) {
                continue;
            }
            Dependency dependency;
            if (Provider.class.isAssignableFrom(field.getType())) {
                try {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    Named namedAnnotation = field.getAnnotation(Named.class);
                    dependency = new ProviderDependency(
                            namedAnnotation == null ?
                                    new DependencyWithType(beanFactory, actualType) :
                                    new DependencyWithId(beanFactory, namedAnnotation.value()),
                            field.getName()
                    );
                } catch (ClassCastException e) {
                    throw new BeanParserException(clazz.getName() + " has raw injected Provider field: " + field.getName());
                }
            } else {
                Named namedAnnotation = field.getAnnotation(Named.class);
                dependency = namedAnnotation == null ?
                        new DependencyWithType(beanFactory, field.getType()) :
                        new DependencyWithId(beanFactory, namedAnnotation.value());
            }
            fieldDependencies.add(dependency);
        }
        return fieldDependencies;
    }

    private List<InjectableMethod> getInjectableMethods(Class<?> clazz) throws BeanParserException {
        List<InjectableMethod> injectableMethods = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            Inject injectAnnotation = method.getAnnotation(Inject.class);
            if (injectAnnotation == null) {
                continue;
            }
            injectableMethods.add(new GenericInjectableMethod(method.getName(), getDependencies(method)));
        }
        return injectableMethods;
    }

    private InjectableConstructor getInjectableConstructor(Constructor<?> constructor) throws BeanParserException {
        List<Dependency> constructorDependencies = new ArrayList<>();
        if (constructor.isVarArgs()) {
            // todo VarArgs
        } else {
            constructorDependencies = getDependencies(constructor);
        }
        return new InjectableConstructorImpl(constructorDependencies);
    }

    private List<Dependency> getDependencies(Executable executable) throws BeanParserException {
        List<Dependency> dependencies = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            Dependency dependency;
            if (Provider.class.isAssignableFrom(parameter.getType())) {
                try {
                    ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
                    Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    Named namedAnnotation = parameter.getAnnotation(Named.class);
                    dependency = new ProviderDependency(
                            namedAnnotation == null ?
                                    new DependencyWithType(beanFactory, actualType) :
                                    new DependencyWithId(beanFactory, namedAnnotation.value()),
                            parameter.getName()
                    );
                } catch (ClassCastException e) {
                    throw new BeanParserException(executable.getName() + " has raw injected Provider parameter: " + parameter.getName());
                }
            } else {
                Named namedAnnotation = parameter.getAnnotation(Named.class);
                dependency = namedAnnotation == null ?
                        new DependencyWithType(beanFactory, parameter.getType()) :
                        new DependencyWithId(beanFactory, namedAnnotation.value());
            }

            dependencies.add(dependency);
        }

        return dependencies;
    }
}