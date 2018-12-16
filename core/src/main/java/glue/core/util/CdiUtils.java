package glue.core.util;

import org.slf4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CDI related utility methods
 *
 * <p>This class holds utility methods for CDI related operations, like finding all available implementations
 * of a generic, parameterized type.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-13
 */
@Singleton
public class CdiUtils {

    private final BeanManager beanManager;
    private final Logger logger;

    /**
     * Constructor with {@link BeanManager} and {@link Logger} initialization
     *
     * @param beanManager Bean manager
     * @param logger      Logger
     */
    @Inject
    public CdiUtils(final BeanManager beanManager, final Logger logger) {
        this.beanManager = beanManager;
        this.logger = logger;
    }

    /**
     * Look for a bean of the raw type with the provided generic type using CDI
     *
     * <p>This method used CDI {@link BeanManager} to look for all beans compatible with the provided raw type,
     * provided generic type and qualifiers. If no beans were found this method will return an empty Optional.
     * When multiple beans are found this method will use CDI resolution strategies to figure out wich one should
     * be returned.</p>
     *
     * @param rawType     Raw type
     * @param genericType Generic type
     * @param qualifiers  Qualifiers (optional)
     * @param <T>         Generic raw type
     * @return Bean instance if found, empty optional otherwise
     */
    public <T> Optional<T> getTypedBean(final Class<?> rawType,
                                        final Class<?> genericType,
                                        final Annotation... qualifiers) {
        Objects.requireNonNull(rawType);
        Objects.requireNonNull(genericType);

        final Set<Bean<?>> beans = findBeans(rawType, genericType, qualifiers);
        if (beans.isEmpty())
            return Optional.empty();

        final Bean<T> bean = (Bean<T>) beanManager.resolve(beans);
        final T beanInstance = produceBean(bean);
        return Optional.ofNullable(beanInstance);
    }

    /**
     * Look for all beans of the raw type with the provided generic type using CDI
     *
     * <p>This method used CDI {@link BeanManager} to look for all beans compatible with the provided raw type,
     * provided generic type and qualifiers.</p>
     *
     * @param rawType     Raw type
     * @param genericType Generic type
     * @param qualifiers  Qualifiers (optional)
     * @param <T>         Generic raw type
     * @return Collection of found compatible beans
     */
    public <T> List<T> getAllTypedBeans(final Class<?> rawType,
                                        final Class<?> genericType,
                                        final Annotation... qualifiers) {
        Objects.requireNonNull(rawType);
        Objects.requireNonNull(genericType);

        return findBeans(rawType, genericType, qualifiers)
                .stream()
                .map(bean -> (Bean<T>) bean)
                .map(this::produceBean)
                .collect(Collectors.toList());
    }

    /**
     * Executes the bean search using CDI APIs
     *
     * @param rawType Raw bean type
     * @param genericType Generic bean type
     * @param qualifiers Qualifiers
     * @param <T> Generic bean raw type
     * @return Collection of found beans
     */
    private <T> Set<Bean<?>> findBeans(final Class<T> rawType,
                                       final Class<?> genericType,
                                       final Annotation... qualifiers) {
        logger.debug("Looking for all beans compatible with raw type of {} and generic type of {}", rawType.getName(), genericType.getName());
        final ParameterizedType parameterizedType = buildParameterizedType(rawType, genericType);
        final Set<Bean<?>> beans = beanManager.getBeans(parameterizedType, qualifiers);

        logger.debug("{} beans found for {} with generic type of {}", beans.size(), rawType.getName(), genericType.getName());
        return beans;
    }

    /**
     * Produce a Bean instance using CDI
     *
     * @param bean Bean to be produced
     * @param <T> Generic bean type
     * @return Produced bean
     */
    private <T> T produceBean(final Bean<T> bean) {
        final CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);
        return bean.create(creationalContext);
    }

    /**
     * Produces a dynamic instance of a {@link ParameterizedType} for the given raw and generic types
     *
     * @param rawType Raw type
     * @param genericType Generic type
     * @return Parameterized type instance
     */
    private ParameterizedType buildParameterizedType(final Class<?> rawType, final Class<?> genericType) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Class<?>[]{genericType};
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return rawType;
            }
        };
    }

}
