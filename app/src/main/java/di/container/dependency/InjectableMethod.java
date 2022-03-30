package di.container.dependency;

import java.util.List;

public interface InjectableMethod {
    String getMethodName();
    List<Dependency> getArguments();
}