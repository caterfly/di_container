package di;

public class ComplexClass {
    private Interface field;

    public ComplexClass(Interface field) {
        this.field = field;
    }

    public Interface getField() {
        return field;
    }
}