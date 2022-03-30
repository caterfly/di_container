package di.beanparser;

import com.google.gson.Gson;
import di.beanparser.objects.Argument;
import di.beanparser.objects.Bean;
import di.beanparser.objects.Beans;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithType;
import di.container.dependency.DependencyWithValue;
import di.container.dependency.InjectableConstructorImpl;
import di.container.dependency.InjectableSetterMethod;
import di.container.dependency.InnerDependency;
import di.container.dependency.ProviderDependency;
import di.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonBeanParser implements BeanParser {

    private final BeanFactory beanFactory = new BeanFactory();

    public JsonBeanParser(String jsonFileName) throws BeanParserException {
        try {
            parseBeans(new Gson().fromJson(Utils.getResourceAsString(jsonFileName), Beans.class));
        } catch (ClassNotFoundException | IOException e) {
            throw new BeanParserException(e);
        }
    }

    private void parseBeans(Beans beans) throws ClassNotFoundException, BeanParserException {
        Map<String, BeanDescription> beanMap = new HashMap<>();
        Set<BeanDescription> beanSet = new HashSet<>();
        for (Bean bean : beans.getBeans()) {
            BeanDescription beanDescription = parseBeanDescription(bean);
            if (bean.getId() != null) {
                beanMap.put(bean.getId(), beanDescription);
            } else {
                beanSet.add(beanDescription);
            }
        }
        beanFactory.setBeanDescriptions(beanMap);
        beanFactory.setBeanDescriptionSet(beanSet);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private BeanDescription parseBeanDescription(Bean bean) throws BeanParserException {
        return new BeanDescription(
                switch (bean.getLifecycle()) {
                    case "singleton" -> BeanLifecycle.SINGLETON;
                    case "thread" -> BeanLifecycle.THREAD;
                    case "prototype" -> BeanLifecycle.PROTOTYPE;
                    default -> throw new BeanParserException("Unknown bean lifecycle: " + bean.getLifecycle());
                },
                getClazz(bean.getClassName()),
                new ArrayList<>() {{
                    add(new InjectableConstructorImpl(parseBeanProperties(bean.getConstructorArguments())));
                }},
                parseBeanProperties(bean.getFields()),
                parseBeanProperties(bean.getSetterArguments()).stream().map(InjectableSetterMethod::new).collect(Collectors.toList())
        );
    }

    private List<Dependency> parseBeanProperties(Argument[] arguments) throws BeanParserException {
        List<Dependency> dependencies = new ArrayList<>();

        if (arguments != null) {
            for (Argument argument : arguments) {
                Dependency dependency;
                if (argument.getBean() != null) {
                    dependency = argument.getFieldName() != null ?
                            new InnerDependency(argument.getFieldName(), parseBeanDescription(argument.getBean())) :
                            new InnerDependency(parseBeanDescription(argument.getBean()));
                } else if (argument.getRef() != null) {
                    dependency = argument.getFieldName() != null ?
                            new DependencyWithId(beanFactory, argument.getRef(), argument.getFieldName()) :
                            new DependencyWithId(beanFactory, argument.getRef());
                } else if (argument.getValue() != null) {
                    Class<?> clazz = getClazz(argument.getClassName());
                    Object typedValue = parseValue(argument.getClassName(), argument.getValue());

                    dependency = argument.getFieldName() != null ?
                            new DependencyWithValue(argument.getFieldName(), typedValue, clazz) :
                            new DependencyWithValue(argument.getValue(), clazz);
                } else {
                    Class<?> clazz = getClazz(argument.getClassName());
                    dependency = argument.getFieldName() != null ?
                            new DependencyWithType(beanFactory, argument.getFieldName(), clazz) :
                            new DependencyWithType(beanFactory, clazz);
                }
                if (argument.isProvider()) {
                    dependency = argument.getFieldName() != null ?
                            new ProviderDependency(dependency, argument.getFieldName()) :
                            new ProviderDependency(dependency);
                }
                dependencies.add(dependency);
            }
        }
        return dependencies;
    }

    private Class<?> getClazz(String className) throws BeanParserException {
        try {
            return switch (className) {
                case "boolean" -> boolean.class;
                case "byte" -> byte.class;
                case "char" -> char.class;
                case "short" -> short.class;
                case "int" -> int.class;
                case "long" -> long.class;
                case "float" -> float.class;
                case "double" -> double.class;
                default -> Class.forName(className);
            };
        } catch (ClassNotFoundException e) {
            throw new BeanParserException(e);
        }
    }

    private Object parseValue(String className, String value) throws BeanParserException {
        try {
            return switch (className) {
                case "boolean" -> Boolean.parseBoolean(value);
                case "byte" -> Byte.parseByte(value);
                case "char" -> value.charAt(0);
                case "short" -> Short.parseShort(value);
                case "int" -> Integer.parseInt(value);
                case "long" -> Long.parseLong(value);
                case "float" -> Float.parseFloat(value);
                case "double" -> Double.parseDouble(value);
                default -> value;
            };
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new BeanParserException(e);
        }
    }
}