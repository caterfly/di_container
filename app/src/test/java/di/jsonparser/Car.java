package di.jsonparser;

public class Car implements ICar {

    private String car;

    public Car() {}

    public Car(String car) {
        this.car = car;
    }

    @Override
    public String getCar() {
        return car;
    }
}