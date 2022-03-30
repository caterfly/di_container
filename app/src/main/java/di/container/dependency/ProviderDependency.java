package di.container.dependency;

import di.container.DIContainerException;
import di.container.MyProvider;
import javax.inject.Provider;

public class ProviderDependency implements Dependency {

    private Dependency wrappedDependency;

    private String fieldName;

    /**
     * Creates a new <code>ProviderDependency</code> which in turn wraps another
     * <code>Dependency</code> inside itself. The <code>wrappedDependency</code> shall be generated
     * as if it was meant to be used without
     * <code>Provider</code>.
     *
     * <p>For example, if you have a class like this:
     *
     * <pre>
     * class MyClass {
     *   public MyClass(Provider&lt;AnotherClass> dependency) {
     *     ...
     *   }
     * }</pre>
     *
     * <p>then a <code>ProviderDependency</code> should be used with a <code>DependencyWithType</code>
     * inside with <code>AnotherClass</code> as the type of this dependency.
     *
     * @param wrappedDependency another dependency to be wrapped inside this <code>ProviderDependency</code>
     * @see Provider
     */
    public ProviderDependency(Dependency wrappedDependency) {
        this.wrappedDependency = wrappedDependency;
    }

    public ProviderDependency(Dependency wrappedDependency, String fieldName) {
        this.wrappedDependency = wrappedDependency;
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return new MyProvider<>(wrappedDependency);
    }

    @Override
    public Class<?> getClazz() {
        return Provider.class;
    }
}