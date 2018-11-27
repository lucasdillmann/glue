package glue.core;

/**
 * Glue application startup
 *
 * <p>This class acts as a bridge between the application and the Glue. It allows to start the Glue lifecycle when
 * the application was started.</p>
 *
 * <p>It's important to remember that Glue is based in IoC concepts, which is done by using CDI. To enable automatic
 * detection of the beans provided by your application Glue needs to know the base package of the application.
 * Since that value is extracted from the the application main class, you should place the class in the base package
 * of your application.</p>
 *
 * <p>The automatic bean discovery is done recursively using what you provide here as the start point. Any beans
 * that reside in a parent package will not be detected without the {@code beans.xml} file.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public final class GlueApplication {

    /**
     * Starts the Glue using the application main class
     *
     * <p>This method starts the Glue using the application main class as the start point of the bean automatic
     * recursive detection.</p>
     *
     * @param applicationClass Application main class
     */
    public static void start(Class<?> applicationClass) {
        new ApplicationController(applicationClass).start();
    }

}
