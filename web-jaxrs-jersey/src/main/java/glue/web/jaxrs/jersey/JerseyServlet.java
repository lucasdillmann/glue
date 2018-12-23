package glue.web.jaxrs.jersey;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jersey qualifier for {@link javax.servlet.Servlet} producer
 *
 * <p>This class implements a CDI qualifier for use in instance producers. It main goal is to simple identify the
 * injection of {@link javax.servlet.Servlet} instances that are a Jersey servlet.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Qualifier
public @interface JerseyServlet {
}
