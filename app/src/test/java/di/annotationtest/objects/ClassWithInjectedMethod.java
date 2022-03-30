package di.annotationtest.objects;

import di.container.annotations.Bean;
import javax.inject.Inject;

@Bean
public class ClassWithInjectedMethod {
    private EmptyClass dependency;

    @Inject
    public void method(EmptyClass dependency) {
        this.dependency = dependency;
    }

    public EmptyClass getDependency() {
        return dependency;
    }
}