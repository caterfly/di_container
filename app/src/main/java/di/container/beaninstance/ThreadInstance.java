package di.container.beaninstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadInstance implements BeanInstance {
    private final Map<Thread, Object> threadObjectMap = new ConcurrentHashMap<>();

    @Override
    public Object get() {
        return threadObjectMap.get(Thread.currentThread());
    }
    @Override
    public void put(Object object) {
        threadObjectMap.put(Thread.currentThread(), object);
    }
}