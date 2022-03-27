package di;

import di.container.DIContainer;
import di.container.DIContainerException;
import di.container.JsonDIContainer;
import di.jsonparser.Person;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonContainerTest {

    @Test public void jsonContainerTest() throws DIContainerException {

        DIContainer container = new JsonDIContainer("person.json");

        Person john = container.getBean("personJohn", Person.class);

        assertEquals("30", john.getAge());
        assertEquals("John", john.getFullName().getFirstName());
        assertEquals("Doe", john.getFullName().getSecondName());
        assertEquals("M", john.getGender());

        Person jane = container.getBean("personJane", Person.class);

        assertNull(jane.getAge());
        assertEquals("Jane", jane.getFullName().getFirstName());
        assertEquals("Doe", jane.getFullName().getSecondName());
        assertNull(jane.getGender());


        assertEquals(3, container.getBeans().size());
        assertTrue(container.getBeans().containsAll(
                Arrays.asList("personJohn", "personJane", "fullNameJohn")));

        // Singleton
        assertEquals(john, container.getBean("personJohn"));

        // Prototype
        assertNotEquals(container.getBean("fullNameJohn"), container.getBean("fullNameJohn"));
    }
}