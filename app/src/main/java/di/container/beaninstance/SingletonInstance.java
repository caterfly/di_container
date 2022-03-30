package di.container.beaninstance;

public class SingletonInstance implements BeanInstance {
    private Object instance;

    @Override
    public Object get() {
        return instance;
    }

    @Override
    public void put(Object object) {
        instance = object;
    }
}