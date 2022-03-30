package di.container;

import di.container.dependency.Dependency;
import javax.inject.Provider;

public class MyProvider<T> implements Provider<T> {

    private final Dependency dependency;

    public MyProvider(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public T get() {
        try {
            return (T) dependency.getBean();
        } catch (DIContainerException e) {
            e.printStackTrace();
        }
        return null;
    }
}