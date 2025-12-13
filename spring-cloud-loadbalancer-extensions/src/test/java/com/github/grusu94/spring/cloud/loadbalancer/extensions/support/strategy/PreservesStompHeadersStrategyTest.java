package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.stomp.PreservesHeadersStompSessionAdapter;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompSession;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class PreservesStompHeadersStrategyTest {
    private final String beanName = "name";
    private final PreservesStompHeadersStrategy processor = new PreservesStompHeadersStrategy(new PropagationProperties(), new EurekaInstanceProperties());

    @Test
    public void should_skip_bean() {
        Object bean = new Object();
        assertThat(processor.postProcessAfterInitialization(bean, beanName), is(bean));
    }

    @Test
    public void should_skip_propagator() {
        PreservesHeadersStompSessionAdapter bean = new PreservesHeadersStompSessionAdapter(null, null, null);
        assertThat(processor.postProcessAfterInitialization(bean, beanName), is(bean));
    }

    @Test
    public void should_decorate_stomp_session() {
        processor.setPropagationProperties(new PropagationProperties());
        processor.setInstanceProperties(new EurekaInstanceProperties());
        assertThat(processor.postProcessAfterInitialization(mock(StompSession.class), beanName).getClass(), equalTo(PreservesHeadersStompSessionAdapter.class));
    }

    @Test
    public void should_skip_stomp_session() {
        PropagationProperties propagationProperties = new PropagationProperties();
        propagationProperties.getStomp().getExcludes().add(compile(beanName));
        processor.setPropagationProperties(propagationProperties);
        processor.setInstanceProperties(new EurekaInstanceProperties());
        assertThat(processor.postProcessAfterInitialization(mock(StompSession.class), beanName).getClass(), not(equalTo(PreservesHeadersStompSessionAdapter.class)));
    }
}