package di.annotationtest.objects;

import di.container.annotations.Bean;
import javax.inject.Inject;

@Bean
public class ClassWithInjectedConstructor {
    private EmptyClass dependency;

    @Inject
    public ClassWithInjectedConstructor(EmptyClass dependency) {
        this.dependency = dependency;
    }

    public ClassWithInjectedConstructor() {
    }

    public EmptyClass getDependency() {
        return dependency;
    }
}