package di.annotationtest.objects;

import di.container.annotations.Bean;
import javax.inject.Inject;
import javax.inject.Provider;

@Bean
public class ClassWithInjectedProviders {

    @Inject
    private Provider<EmptyClass> fieldProvider;

    private final Provider<EmptyClass> constructorProvider;

    private Provider<EmptyClass> methodProvider;

    @Inject
    public ClassWithInjectedProviders(Provider<EmptyClass> constructorProvider) {
        this.constructorProvider = constructorProvider;
    }

    @Inject
    private void setMethodProvider(Provider<EmptyClass> methodProvider) {
        this.methodProvider = methodProvider;
    }

    public EmptyClass getFieldProvider() {
        return fieldProvider.get();
    }

    public EmptyClass getConstructorProvider() {
        return constructorProvider.get();
    }

    public EmptyClass getMethodProvider() {
        return methodProvider.get();
    }
}