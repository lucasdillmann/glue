package glue.webcontainer.api;

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
    void start();

    /**
     * Stops the container
     *
     * <p>This method initiates the shutdown procedure of the web container.</p>
     */
    void stop();


}
