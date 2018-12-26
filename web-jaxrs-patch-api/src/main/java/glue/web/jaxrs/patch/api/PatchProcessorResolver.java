package glue.web.jaxrs.patch.api;

import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * {@link PatchProcessor} resolver utility class
 *
 * <p>This class lookups for all {@link PatchProcessor} implementations available from CDI, selecting the
 * appropriated one for the {@link javax.ws.rs.PATCH} request that is able to handle it.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
class PatchProcessorResolver {

    private static final String NO_COMPATIBLE_PROCESSOR_FOR_CONTENT_TYPE = "No compatible PatchProcessor implementation " +
            "found for media type ";
    private static final String MULTIPLE_PROCESSOR_FOR_CONTENT_TYPe = "Multiple compatible PatchProcessor found for " +
            "media type ";

    private final Instance<PatchProcessor> processors;
    private final Logger logger;

    /**
     * Constructor with CDI {@link Instance} gateway object for {@link PatchProcessor} and {@link Logger} initialization
     *
     * @param processor CDI instance gateway for patch processor objects
     * @param logger Logger
     */
    @Inject
    PatchProcessorResolver(final Instance<PatchProcessor> processor, final Logger logger) {
        this.processors = processor;
        this.logger = logger;
    }

    /**
     * Lookups for a compatible {@link PatchProcessor} for the given {@link MediaType}
     *
     * <p>This method will looks for a compatible {@link PatchProcessor} implementation for the provided {@link MediaType}.
     * When no implementation or multiple ones are found this method throws a {@link PatchException}.</p>
     *
     * @param mediaType Media type
     * @return Compatible PatchProcessor instance
     * @throws PatchException when no processor is found
     * @throws PatchException when multiple processors are available
     */
    PatchProcessor getProcessor(final MediaType mediaType) {
        Objects.requireNonNull(mediaType);

        logger.debug(
                "Looking for compatible {} implementations for media type '{}'",
                PatchProcessor.class.getName(), mediaType
        );

        final List<PatchProcessor> compatibleProcessors = StreamSupport
                .stream(processors.spliterator(), false)
                .filter(processor -> processor.isCompatible(mediaType))
                .collect(Collectors.toList());

        compatibleProcessors.forEach(processor -> logger.debug(
                "Found {} as a {} for media type {}",
                processor.getClass().getName(),
                PatchProcessor.class.getName(),
                mediaType
        ));

        if (compatibleProcessors.isEmpty())
            throw new PatchException(NO_COMPATIBLE_PROCESSOR_FOR_CONTENT_TYPE + mediaType.toString());

        if (compatibleProcessors.size() > 1)
            throw new PatchException(MULTIPLE_PROCESSOR_FOR_CONTENT_TYPe + mediaType.toString());

        return compatibleProcessors.get(0);

    }
}
