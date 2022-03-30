package di.container;

import java.util.Collection;
import java.util.HashSet;

public class GenericDIContainer implements DIContainer {
    protected BeanFactory beanFactory;

    public GenericDIContainer() {
        beanFactory = new BeanFactory();
    }

    public GenericDIContainer(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) throws DIContainerException {
        Object bean = getBean(id);
        if (clazz.isInstance(bean)) {
            return (T)bean;
        }
        throw new DIContainerException(id + " is not " + clazz.getName());
    }

    @Override
    public Object getBean(String id) throws DIContainerException {
        return beanFactory.getBean(id);
    }

    @Override
    public Collection<String> getBeans() {
        return new HashSet<>(beanFactory.getBeanDescriptions().keySet());
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws DIContainerException {
        return (T)beanFactory.getBean(clazz);
    }
}