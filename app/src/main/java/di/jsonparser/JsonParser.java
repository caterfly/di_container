package di.jsonparser;

import com.google.gson.Gson;
import di.container.BeanDescription;
import di.container.BeanFactory;
import di.container.BeanLifecycle;
import di.container.beanproperty.BeanProperty;
import di.container.beanproperty.BeanPropertyWithId;
import di.container.beanproperty.BeanPropertyWithValue;
import di.container.beanproperty.InnerBeanProperty;
import di.jsonparser.objects.Argument;
import di.jsonparser.objects.Bean;
import di.jsonparser.objects.Beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>() {{
        put("boolean", boolean.class);
        put("byte", byte.class);
        put("char", char.class);
        put("short", short.class);
        put("int", int.class);
        put("long", long.class);
        put("float", float.class);
        put("double", double.class);
    }};

    private final BeanFactory beanFactory = new BeanFactory();

    public BeanFactory getBeanFactory(String jsonFileName) throws IOException, ClassNotFoundException {
        String jsonString = new String(JsonParser.class.getResourceAsStream("/" + jsonFileName).readAllBytes());
        Beans beans = new Gson().fromJson(jsonString, Beans.class);

        Map<String, BeanDescription> beanMap = new HashMap<>();
        for (Bean bean : beans.getBeans()) {
            beanMap.put(bean.getId(), parseBeanDescription(bean));
        }

        beanFactory.setBeans(beanMap);

        return beanFactory;
    }

    private BeanDescription parseBeanDescription(Bean bean) throws ClassNotFoundException {

        return new BeanDescription(
                switch (bean.getLifecycle()) {
                    default -> BeanLifecycle.SINGLETON;
                    case "thread" -> BeanLifecycle.THREAD;
                    case "prototype" -> BeanLifecycle.PROTOTYPE;
                },
                getClazz(bean.getClassName()),
                bean.isProxy(),
                parseBeanProperties(bean.getConstructorArguments()),
                parseBeanProperties(bean.getSetterArguments())
        );
    }

    private List<BeanProperty> parseBeanProperties(Argument[] arguments) throws ClassNotFoundException {
        List<BeanProperty> beanProperties = new ArrayList<>();

        if (arguments != null) {
            for (Argument argument : arguments) {
                if (argument.getBean() != null) {
                    beanProperties.add(
                            argument.getFieldName() != null ?
                                    new InnerBeanProperty(argument.getFieldName(), parseBeanDescription(argument.getBean())) :
                                    new InnerBeanProperty(parseBeanDescription(argument.getBean()))
                    );
                } else if (argument.getRef() != null) {
                    beanProperties.add(
                            argument.getFieldName() != null ?
                                    new BeanPropertyWithId(beanFactory, argument.getRef(), argument.getFieldName()) :
                                    new BeanPropertyWithId(beanFactory, argument.getRef())
                    );
                } else { // todo parse real type value
                    Class<?> clazz = getClazz(argument.getClassName());

                    beanProperties.add(
                            argument.getFieldName() != null ?
                                    new BeanPropertyWithValue(argument.getFieldName(), argument.getValue(), clazz) :
                                    new BeanPropertyWithValue(argument.getValue(), clazz)
                    );
                }
            }
        }

        return beanProperties;
    }

    private Class<?> getClazz(String className) throws ClassNotFoundException {
        Class<?> clazz = PRIMITIVES.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
        }

        return clazz;
    }
}