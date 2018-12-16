package glue.web.jaxrs.api;

import javax.servlet.Servlet;

/**
 * JAX-RS provider definition interface
 *
 * <p>This interface exposes the required methods that a JAX-RS provider needs to implement to allow integration
 * with Glue API. It defines simples methods like startup and shutdown actions.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
public interface JaxRsProvider {

    /**
     * Starts the provider and return the {@link Servlet} to be used
     *
     * <p>This method notifies the provider that application is starting and expectes a Servlet instance as return
     * to integrate the provider under current web container.</p>
     *
     * @return Servlet instance from provider implementation
     */
    Servlet start();

    /**
     * Notifies provider about application shutdown
     *
     * <p>This method is called when application is stopping, allowing provider to gracefully stops.</p>
     */
    void stop();

}
