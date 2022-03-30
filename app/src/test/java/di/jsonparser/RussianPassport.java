package di.jsonparser;

public class RussianPassport implements IPassport {
    private final String number;

    public RussianPassport(String number) {
        this.number = number;
    }

    @Override
    public String getNumber() {
        return number;
    }
}