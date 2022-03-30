package di.beanparser.objects;

import java.util.Arrays;

public class Beans {

    private Bean[] beans;

    public Bean[] getBeans() {
        return beans;
    }

    @Override
    public String toString() {
        return "BeansJson{" +
                "beans=" + Arrays.toString(beans) +
                '}';
    }
}