package glue.web.jaxrs.jersey;

import glue.web.container.api.WebContainer;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * {@link ServletConfig} implementation for Jersey servlet
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
class JerseyServletConfig implements ServletConfig {

    private final JerseyConfiguration configuration;
    private final WebContainer webContainer;

    /**
     * Constructor with {@link JerseyConfiguration} and {@link WebContainer} initialization
     *
     * @param configuration Configuration
     */
    @Inject
    JerseyServletConfig(final JerseyConfiguration configuration, final WebContainer webContainer) {
        this.configuration = configuration;
        this.webContainer = webContainer;
    }

    /**
     * Returns the name of this servlet instance.
     * The name may be provided via server administration, assigned in the
     * web application deployment descriptor, or for an unregistered (and thus
     * unnamed) servlet instance it will be the servlet's class name.
     *
     * @return the name of the servlet instance
     */
    @Override
    public String getServletName() {
        return configuration.getServletName();
    }

    /**
     * Returns a reference to the {@link ServletContext} in which the caller
     * is executing.
     *
     * @return a {@link ServletContext} object, used
     * by the caller to interact with its servlet container
     * @see ServletContext
     */
    @Override
    public ServletContext getServletContext() {
        return webContainer.getServletContext();
    }

    /**
     * Gets the value of the initialization parameter with the given name.
     *
     * @param name the name of the initialization parameter whose value to
     *             get
     * @return a <code>String</code> containing the value
     * of the initialization parameter, or <code>null</code> if
     * the initialization parameter does not exist
     */
    @Override
    public String getInitParameter(String name) {
        return getServletContext().getInitParameter(name);
    }

    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has
     * no initialization parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the servlet's
     * initialization parameters
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return getServletContext().getInitParameterNames();
    }
}
