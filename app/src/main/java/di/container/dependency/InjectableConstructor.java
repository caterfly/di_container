package di.container.dependency;

import java.util.List;

public interface InjectableConstructor {
    List<Dependency> getArguments();
}