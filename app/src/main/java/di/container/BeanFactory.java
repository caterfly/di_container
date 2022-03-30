package di.container;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanFactory {

    private Map<String, BeanDescription> beanDescriptions;

    private Set<BeanDescription> beanDescriptionSet;


    public Object getBean(String id) throws DIContainerException {
        var beanDescription = beanDescriptions.get(id);
        if (beanDescription == null) {
            throw new DIContainerException("Illegal id!!!");
        }

        return beanDescription.getBean();
    }

    public Object getBean(Class<?> clazz) throws DIContainerException {
        List<BeanDescription> descriptions = Sets.union(
                        beanDescriptionSet, new HashSet<>(beanDescriptions.values())).stream()
                .filter(description -> clazz.isAssignableFrom(description.getClazz()))
                .collect(Collectors.toList());
        if (descriptions.isEmpty()) {
            throw new DIContainerException("No beans found: " + clazz);
        }
        if (descriptions.size() > 1) {
            throw new DIContainerException("Too many beans found");
        }
        return descriptions.get(0).getBean();
    }

    public BeanDescription getBeanDescription(String id) {
        return beanDescriptions.get(id);
    }

    public Map<String, BeanDescription> getBeanDescriptions() {
        return beanDescriptions;
    }

    public void setBeanDescriptions(Map<String, BeanDescription> beanDescriptions) {
        this.beanDescriptions = new ConcurrentHashMap<>(beanDescriptions);
    }

    public void setBeanDescriptionSet(Set<BeanDescription> beanDescriptionSet) {
        this.beanDescriptionSet = Collections.synchronizedSet(new HashSet<>(beanDescriptionSet));
    }
}