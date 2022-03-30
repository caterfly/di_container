package di.annotationtest;

import di.annotationtest.objects.ClassWIthMultipleInjectedConstructors;
import di.annotationtest.objects.ClassWithDifferentScopedDependencies;
import di.annotationtest.objects.ClassWithInjectedConstructor;
import di.annotationtest.objects.ClassWithInjectedMethod;
import di.annotationtest.objects.ClassWithoutInjectedConstructor;
import di.annotationtest.objects.ClassWithInjectedProviders;
import di.annotationtest.objects.ClassWithNamedDependency;
import di.annotationtest.objects.EmptyClass;
import di.annotationtest.objects.EmptyPrototypeClass;
import di.annotationtest.objects.EmptyThreadClass;
import di.container.AnnotationDIContainer;
import di.container.DIContainer;
import di.container.DIContainerException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

import static org.junit.Assert.*;
public class AnnotationTest {
    DIContainer container = new AnnotationDIContainer(this.getClass());
    public AnnotationTest() throws DIContainerException {
    }
    @Test
    public void annotatedSingletonTest() throws DIContainerException {
        assertSame(container.getBean(EmptyClass.class), container.getBean(EmptyClass.class));
    }
    @Test
    public void annotatedPrototypeTest() throws DIContainerException {
        assertNotSame(container.getBean(EmptyPrototypeClass.class), container.getBean(EmptyPrototypeClass.class));
    }
    @Test
    public void classWithInjectedConstructorTest() throws DIContainerException {
        assertNotNull(container.getBean(ClassWithInjectedConstructor.class).getDependency());
    }
    @Test
    public void classWithMultipleInjectedConstructorsTest() throws DIContainerException {
        assertThrows(DIContainerException.class, () -> container.getBean(
                ClassWIthMultipleInjectedConstructors.class));
    }
    @Test
    public void classWithoutInjectedConstructorTest() throws DIContainerException {
    /*assertThrows(DIContainerException.class, () -> container.getBean( todo -o think
        ClassWithoutInjectedConstructor.class));*/
    }
    @Test
    public void methodInjectionTest() throws DIContainerException {
        ClassWithInjectedMethod bean = container.getBean(ClassWithInjectedMethod.class);
        assertNotNull(bean.getDependency());
        assertSame(bean.getDependency(), bean.getDependency());
    }

    @Test
    public void namedDependencyTest() throws DIContainerException {
        ClassWithNamedDependency bean = container.getBean(ClassWithNamedDependency.class);
        assertNotNull(bean.getDependency());
        assertEquals("InterfaceImpl", bean.getDependency().getString());
    }

    @Test
    public void singletonProviderTest() throws DIContainerException, InterruptedException {
        ClassWithDifferentScopedDependencies bean = container
                .getBean(ClassWithDifferentScopedDependencies.class);
        assertSame(bean.getSingletonDependency(), bean.getSingletonDependency());

        AtomicReference<EmptyClass> dependency = new AtomicReference<>();

        Thread thread = new Thread(() -> dependency.set(bean.getSingletonDependency()));

        thread.start();

        thread.join();

        assertSame(dependency.get(), bean.getSingletonDependency());
    }

    @Test
    public void prototypeProviderTest() throws DIContainerException, InterruptedException {
        ClassWithDifferentScopedDependencies bean = container
                .getBean(ClassWithDifferentScopedDependencies.class);
        assertNotSame(bean.getPrototypeDependency(), bean.getPrototypeDependency());

        AtomicReference<EmptyPrototypeClass> dependency = new AtomicReference<>();

        Thread thread = new Thread(() -> dependency.set(bean.getPrototypeDependency()));

        thread.start();

        thread.join();

        assertNotSame(dependency.get(), bean.getPrototypeDependency());
    }

    @Test
    public void threadProviderTest() throws DIContainerException, InterruptedException {
        ClassWithDifferentScopedDependencies bean = container
                .getBean(ClassWithDifferentScopedDependencies.class);
        assertSame(bean.getSingletonDependency(), bean.getSingletonDependency());

        AtomicReference<EmptyThreadClass> dependency = new AtomicReference<>();

        Thread thread = new Thread(() -> dependency.set(bean.getThreadDependency()));

        thread.start();

        thread.join();

        assertNotSame(dependency.get(), bean.getThreadDependency());
    }

    @Test
    public void providerTest() throws DIContainerException {
        ClassWithInjectedProviders bean = container.getBean(ClassWithInjectedProviders.class);
        assertNotNull(bean.getConstructorProvider());
        assertNotNull(bean.getFieldProvider());
        assertNotNull(bean.getMethodProvider());
    }
}