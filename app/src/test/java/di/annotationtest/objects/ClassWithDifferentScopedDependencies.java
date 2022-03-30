package di.annotationtest.objects;

import di.container.annotations.Bean;
import javax.inject.Inject;
import javax.inject.Provider;

@Bean
public class ClassWithDifferentScopedDependencies {
    @Inject
    private Provider<EmptyClass> singleton;

    @Inject
    private Provider<EmptyPrototypeClass> prototype;

    @Inject
    private Provider<EmptyThreadClass> thread;

    public EmptyClass getSingletonDependency() {
        return singleton.get();
    }

    public EmptyPrototypeClass getPrototypeDependency() {
        return prototype.get();
    }

    public EmptyThreadClass getThreadDependency() {
        return thread.get();
    }
}