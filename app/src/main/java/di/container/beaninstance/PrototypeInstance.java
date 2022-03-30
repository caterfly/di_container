package di.container.beaninstance;

public class PrototypeInstance implements BeanInstance {
    @Override
    public Object get() {
        return null;
    }

    @Override
    public void put(Object object) {
        // Ignore
    }
}