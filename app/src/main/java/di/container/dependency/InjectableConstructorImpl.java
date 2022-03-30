package di.container.dependency;

import java.util.List;

public class InjectableConstructorImpl implements InjectableConstructor {

    private final List<Dependency> arguments;

    public InjectableConstructorImpl(List<Dependency> arguments) {
        this.arguments = arguments;
    }

    @Override
    public List<Dependency> getArguments() {
        return arguments;
    }
}