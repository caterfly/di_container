package di.container.dependency;

import java.util.List;

public class GenericInjectableMethod implements InjectableMethod {

    private final String methodName;

    private final List<Dependency> arguments;

    public GenericInjectableMethod(String methodName,
                                   List<Dependency> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public List<Dependency> getArguments() {
        return arguments;
    }
}