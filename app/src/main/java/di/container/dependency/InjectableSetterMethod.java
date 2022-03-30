package di.container.dependency;

import di.util.Utils;
import java.util.List;

public class InjectableSetterMethod implements InjectableMethod {
    private String methodName;
    private Dependency dependency;

    public InjectableSetterMethod(String fieldName, Dependency dependency) {
        methodName = generateSetterName(fieldName);
        this.dependency = dependency;
    }

    public InjectableSetterMethod(Dependency dependency) {
        methodName = generateSetterName(dependency.getFieldName());
        this.dependency = dependency;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public List<Dependency> getArguments() {
        return List.of(dependency);
    }

    private String generateSetterName(String fieldName) {
        return "set" + Utils.capitalize(fieldName);
    }
}