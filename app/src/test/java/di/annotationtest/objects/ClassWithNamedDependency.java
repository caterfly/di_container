package di.annotationtest.objects;

import di.container.annotations.Bean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Bean
public class ClassWithNamedDependency {
    @Inject
    @Named(value = "InterfaceImpl")
    private Provider<Interface> provider;

    public Interface getDependency() {
        return provider.get();
    }
}