package glue.core.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link CdiUtils}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-13
 */
@RunWith(MockitoJUnitRunner.class)
public class CdiUtilsTests {

    private class GenericType {}
    private class TestBean<T> {}

    @Mock
    private BeanManager beanManager;
    @Mock
    private Bean<TestBean> bean;
    @Mock
    private CreationalContext<TestBean> creationalContext;
    @Mock
    private Logger logger;

    private CdiUtils utils;

    @Before
    public void setup() {
        final Set<Bean<?>> beans = new HashSet<>();
        beans.add(bean);

        doReturn(beans).when(beanManager).getBeans(any(Type.class), any(Annotation[].class));
        doReturn(beans).when(beanManager).getBeans(any(Type.class));
        doReturn(creationalContext).when(beanManager).createCreationalContext(bean);
        doReturn(bean).when(beanManager).resolve(beans);
        doReturn(new TestBean()).when(bean).create(creationalContext);

        this.utils = new CdiUtils(beanManager, logger);
    }

    @Test
    public void shouldProduceSingleBean() {
        // execution
        final Optional<TestBean<GenericType>> actualBean = utils.getTypedBean(TestBean.class, GenericType.class);

        // validation
        assertNotNull(actualBean);
        assertThat(actualBean.isPresent(), is(true));
        assertThat(actualBean.get(), is(instanceOf(TestBean.class)));
    }

    @Test
    public void shouldProduceAllBeans() {
        // execution
        final List<TestBean<GenericType>> actualBeans = utils.getAllTypedBeans(TestBean.class, GenericType.class);

        // validation
        assertNotNull(actualBeans);
        assertThat(actualBeans.size(), is(1));
        assertThat(actualBeans.get(0), is(instanceOf(TestBean.class)));
    }
}
