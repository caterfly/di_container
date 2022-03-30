package di.container;

import di.beanparser.BeanParserException;
import di.beanparser.JsonBeanParser;

public class JsonDIContainer extends GenericDIContainer {
    public JsonDIContainer(String filename) throws DIContainerException {
        try {
            beanFactory = new JsonBeanParser(filename).getBeanFactory();
        } catch (BeanParserException e) {
            throw new DIContainerException("Unable to parse file: " + filename + " " + e);
        }
    }
}