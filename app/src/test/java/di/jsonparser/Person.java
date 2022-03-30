package di.jsonparser;

import di.container.annotations.Bean;

import javax.inject.Inject;
import javax.inject.Provider;

@Bean
public class Person {

    private FullName fullName;
    private ICar car;
    private String gender;
    private int age;
    private Provider<IPassport> passport;
    private String height;

    public String getHeight() {
        return height;
    }

    public Person() {
    }

    @Inject
    public Person(FullName fullName) {
        this.fullName = fullName;
    }

    public Person(FullName fullName, String gender, Provider<IPassport> passport) {
        this.fullName = fullName;
        this.gender = gender;
        this.passport = passport;
    }

    public IPassport getPassport() {
        return passport.get();
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FullName getFullName() {
        return fullName;
    }

    public ICar getCar() {
        return car;
    }

    public void setCar(ICar car) {
        this.car = car;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
}