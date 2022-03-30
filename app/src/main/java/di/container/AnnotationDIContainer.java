package di.container;

import di.beanparser.AnnotationBeanParser;
import di.beanparser.BeanParserException;

import java.io.IOException;
import java.net.URISyntaxException;

public class AnnotationDIContainer extends GenericDIContainer {
    public AnnotationDIContainer(Class<?>... startupClasses) throws DIContainerException {
        try {
            beanFactory = new AnnotationBeanParser(startupClasses).getBeanFactory();
        } catch (BeanParserException e) {
            throw new DIContainerException(e.getMessage());
        }
    }
}