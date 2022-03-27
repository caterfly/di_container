package di.jsonparser;

public class Person {

    private FullName fullName;
    private String gender;
    private String age;


    public Person() {}

    public Person(FullName fullName) {
        this.fullName = fullName;
    }

    public Person(FullName fullName, String gender) {
        this.fullName = fullName;
        this.gender = gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public FullName getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}