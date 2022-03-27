package di.container;

import di.jsonparser.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class JsonDIContainer implements DIContainer {
    BeanFactory beanFactory;

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws DIContainerException {
        Object bean = getBean(name);
        if (clazz.isInstance(bean)) {
            return (T)bean;
        }
        throw new DIContainerException(name + " is not " + clazz.getName());
    }

    public JsonDIContainer(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public JsonDIContainer(String filename) throws DIContainerException {
        try {
            beanFactory = new JsonParser().getBeanFactory(filename);
        } catch (IOException | ClassNotFoundException e) {
            throw new DIContainerException("Unable to parse file: " + filename);
        }
    }

    @Override
    public Object getBean(String name) throws DIContainerException {
        return beanFactory.getBean(name);
    }

    @Override
    public Collection<String> getBeans() {
        return new HashSet<>(beanFactory.getBeans().keySet());
    }
}