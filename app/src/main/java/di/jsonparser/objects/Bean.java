package di.jsonparser.objects;

import java.util.Arrays;

public class Bean {

    private String id;
    private String className;
    private String lifecycle = "singleton";
    private boolean proxy = false;
    private Argument[] constructorArguments;
    private Argument[] setterArguments;

    @Override
    public String toString() {
        return "BeanJson{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", lifecycle='" + lifecycle + '\'' +
                ", proxy=" + proxy +
                ", constructorArguments=" + Arrays.toString(constructorArguments) +
                ", setterArguments=" + Arrays.toString(setterArguments) +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getLifecycle() {
        return lifecycle;
    }

    public boolean isProxy() {
        return proxy;
    }

    public Argument[] getConstructorArguments() {
        return constructorArguments;
    }

    public Argument[] getSetterArguments() {
        return setterArguments;
    }
}