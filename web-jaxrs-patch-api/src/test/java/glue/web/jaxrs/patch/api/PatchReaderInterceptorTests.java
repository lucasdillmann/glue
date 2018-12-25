package glue.web.jaxrs.patch.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link PatchReaderInterceptor}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@RunWith(MockitoJUnitRunner.class)
public class PatchReaderInterceptorTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Logger logger;
    @Mock
    private UriInfo uriInfo;
    @Mock
    private ResourceInfo resourceInfo;
    @Mock
    private ResourceContext resourceContext;
    @Mock
    private PatchProcessorResolver resolver;
    @Mock
    private PatchResource patchResource;
    @Mock
    private ReaderInterceptorContext requestContext;
    @Mock
    private MediaType mediaType;
    @Mock
    private PatchProcessor processor;
    @Mock
    private InputStream originalStream;
    @Mock
    private InputStream patchedStream;
    @Mock
    private Object patchEntity;

    private Class<?> resourceClass;
    private Method resourceMethod;
    private Optional patchEntityContainer;
    private PatchReaderInterceptor interceptor;

    @Before
    public void setup() throws Exception {
        this.resourceClass = getClass();
        this.resourceMethod = resourceClass.getMethod("setup");
        this.interceptor = new PatchReaderInterceptor(resolver, logger);
        this.patchEntityContainer = Optional.of(patchEntity);

        injectAttribute(UriInfo.class, uriInfo);
        injectAttribute(ResourceInfo.class, resourceInfo);
        injectAttribute(ResourceContext.class, resourceContext);

        doReturn(resourceClass).when(resourceInfo).getResourceClass();
        doReturn(patchResource).when(resourceContext).getResource(any(Class.class));
        doReturn(mediaType).when(requestContext).getMediaType();
        doReturn(resourceMethod).when(resourceInfo).getResourceMethod();
        doReturn(patchedStream).when(processor).apply(any(), any());
        doReturn(processor).when(resolver).getProcessor(mediaType);
        doReturn(originalStream).when(requestContext).getInputStream();
        doReturn(patchEntityContainer).when(patchResource).getPatchTargetObject(resourceInfo, uriInfo);
    }

    private void injectAttribute(final Class<?> attributeType, final Object value) {
        Arrays.stream(interceptor.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(attributeType))
                .peek(field -> field.setAccessible(true))
                .forEach(field -> {
                    try {
                        field.set(interceptor, value);
                    } catch (final Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    @Test
    public void shouldThrowExceptionWhenIsntPatchResourceInterfaceImplementation() throws Exception {
        // scenario
        doReturn(this).when(resourceContext).getResource(any(Class.class));
        expectedException.expect(PatchException.class);

        // execution
        interceptor.aroundReadFrom(requestContext);
    }

    @Test
    public void shouldCallProcessor() throws IOException {
        // execution
        interceptor.aroundReadFrom(requestContext);

        // validation
        verify(processor, times(1)).apply(originalStream, patchEntityContainer);
    }

    @Test
    public void shouldCallResolver() throws IOException {
        // execution
        interceptor.aroundReadFrom(requestContext);

        // validation
        verify(resolver, times(1)).getProcessor(mediaType);
    }

    @Test
    public void shouldReplaceInputStream() throws IOException {
        // execution
        interceptor.aroundReadFrom(requestContext);

        // validation
        verify(requestContext, times(1)).setInputStream(patchedStream);
    }
}
