package di.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private Map<String, BeanDescription> beans;

    public void setBeans(Map<String, BeanDescription> beans) {
        this.beans = new ConcurrentHashMap<>(beans);
    }


    public Object getBean(String name) throws DIContainerException {
        var beanDescription = beans.get(name);
        if (beanDescription == null) {
            throw new DIContainerException("Illegal name!!!");
        }

        return beanDescription.getBean();
    }

    public BeanDescription getBeanDescription(String name) {
        return beans.get(name);
    }

    public Map<String, BeanDescription> getBeans() {
        return beans;
    }

}