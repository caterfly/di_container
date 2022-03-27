package di;
import java.util.Date;

public class SimpleClass implements Interface {
    private String attribute;
    private int number;

    public SimpleClass(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}