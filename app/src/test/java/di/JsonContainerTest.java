package di;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.JsonDIContainer;
import di.jsonparser.FullName;
import di.jsonparser.IPassport;
import di.jsonparser.Person;

import java.util.Arrays;

import org.junit.Test;

public class JsonContainerTest {
    @Test
    public void jsonContainerTest() throws DIContainerException {
        DIContainer container = new JsonDIContainer("person.json");
        Person john = (Person) container.getBean("personJohn");
        assertEquals(30, john.getAge());
        assertEquals("John", john.getFullName().getFirstName());
        assertEquals("Doe", john.getFullName().getSecondName());
        assertEquals("M", john.getGender());

        Person jane = container.getBean("personJane", Person.class);

        assertEquals(0, jane.getAge());
        assertEquals("Jane", jane.getFullName().getFirstName());
        assertEquals("Doe", jane.getFullName().getSecondName());
        assertNull(jane.getGender());
        assertEquals(3, container.getBeans().size());
        assertTrue(container.getBeans().containsAll(
                Arrays.asList("personJohn", "personJane", "fullNameJohn")));
        // Singleton
        assertSame(john, container.getBean("personJohn"));
        // Prototype
        assertNotSame(container.getBean("fullNameJohn"), container.getBean("fullNameJohn"));
    }

    @Test
    public void beanGenerationByClassTest() throws DIContainerException {
        DIContainer container = new JsonDIContainer("person.json");
        assertNotNull(container.getBean(FullName.class));
        Person john = (Person) container.getBean("personJohn");
        assertEquals("Super Car", john.getCar().getCar());
        try {
            container.getBean(Person.class);
            fail();
        } catch (DIContainerException expected) {

        }
    }

    @Test
    public void providerTest() throws DIContainerException {
        DIContainer container = new JsonDIContainer("person.json");

        Person john = container.getBean("personJohn", Person.class);

        IPassport passport = john.getPassport();

        assertNotNull(passport);

        assertEquals("2281488666", passport.getNumber());

        assertNotSame(john.getPassport(), john.getPassport());
    }
}