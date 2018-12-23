package glue.web.container.api;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Web Container interface
 *
 * <p>This interface defines the required methods that any Web Container should implement to allow integration
 * with Glue. Example of Web Containers include Jetty and Tomcat.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-26
 */
public interface WebContainer {

    /**
     * Starts the container
     *
     * <p>This method starts the container, enabling the project to handle web requests.</p>
     */
    void startContainer();

    /**
     * Stops the container
     *
     * <p>This method initiates the shutdown procedure of the web container.</p>
     */
    void stopContainer();

    /**
     * Returns current servlet context
     *
     * @return Servlet context
     */
    ServletContext getServletContext();

    /**
     * Starts a {@link Servlet} under the container
     *
     * <p>This method uses the provided {@link Servlet} instance to startContainer it under current web container.</p>
     *
     * @param servlet Servlet instance
     * @param contextPath Context path
     * @param initAttributes Init attributes
     */
    void startServlet(Servlet servlet, String contextPath, Map<String, String> initAttributes);

}
