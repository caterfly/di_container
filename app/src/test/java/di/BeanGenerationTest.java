package di;

import com.google.common.collect.Lists;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.GenericDIContainer;
import di.container.dependency.Dependency;
import di.container.dependency.DependencyWithId;
import di.container.dependency.DependencyWithValue;
import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.dependency.InjectableConstructorImpl;
import di.container.dependency.InjectableSetterMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class BeanGenerationTest {
    @Test
    public void basicPrototypeTest() {
        String value = "TestString";

        Dependency stringArg = new DependencyWithValue("attribute", value, String.class);

        BeanDescription prototype = new BeanDescription(BeanLifecycle.PROTOTYPE, SimpleClass.class,
                List.of(new InjectableConstructorImpl(Lists.asList(stringArg, new Dependency[0]))),
                new ArrayList<>(), new ArrayList<>());


        Map<String, BeanDescription> map = new HashMap<>();
        map.put("prototype", prototype);
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.setBeanDescriptions(map);
        DIContainer container = new GenericDIContainer(beanFactory);

        try {
            SimpleClass bean1 = container.getBean("prototype", SimpleClass.class);
            assertEquals(value, bean1.getAttribute());

            SimpleClass bean2 = container.getBean("prototype", SimpleClass.class);
            assertNotEquals(bean1, bean2);
        } catch (DIContainerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void basicSingletonTest() {
        String value = "TestString";
        int number = 5150;

        Dependency stringArg = new DependencyWithValue("attribute", value, String.class);
        Dependency intArg = new DependencyWithValue("number", number, int.class);
        BeanDescription singleton = new BeanDescription(BeanLifecycle.SINGLETON, SimpleClass.class,
                List.of(new InjectableConstructorImpl(Lists.newArrayList(stringArg))), new ArrayList<>(),
                Lists.newArrayList(new InjectableSetterMethod("number", intArg)));

        Map<String, BeanDescription> map = new HashMap<>();
        map.put("singleton", singleton);
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.setBeanDescriptions(map);
        DIContainer container = new GenericDIContainer(beanFactory);

        try {
            SimpleClass bean1 = container.getBean("singleton", SimpleClass.class);
            assertEquals(value, bean1.getAttribute());
            assertEquals(number, bean1.getNumber());

            SimpleClass bean2 = container.getBean("singleton", SimpleClass.class);
            assertEquals(bean1, bean2);
        } catch (DIContainerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void complexSingletonTest() {
        String value = "TestString";

        Dependency stringArg = new DependencyWithValue("attribute", value, String.class);

        BeanDescription field = new BeanDescription(BeanLifecycle.SINGLETON, SimpleClass.class,
                List.of(new InjectableConstructorImpl(Lists.newArrayList(stringArg))), new ArrayList<>(),
                new ArrayList<>());

        Map<String, BeanDescription> map = new HashMap<>();
        map.put("singleton", field);

        BeanFactory beanFactory = new BeanFactory();

        Dependency complexSingletonConstructorProperty = new DependencyWithId(beanFactory, "singleton");
        BeanDescription complexSingleton = new BeanDescription(BeanLifecycle.SINGLETON,
                ComplexClass.class,
                List.of(
                new InjectableConstructorImpl(Lists.newArrayList(complexSingletonConstructorProperty))),
                new ArrayList<>(),
                new ArrayList<>());

        map.put("complexSingleton", complexSingleton);

        beanFactory.setBeanDescriptions(map);

        DIContainer container = new GenericDIContainer(beanFactory);

        try {
            ComplexClass bean = container.getBean("complexSingleton", ComplexClass.class);
            assertEquals(value, bean.getField().getAttribute());
        } catch (DIContainerException e) {
            e.printStackTrace();
            fail();
        }
    }
}