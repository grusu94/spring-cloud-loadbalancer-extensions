package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms.EchoMessagePropertyEncoder;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms.PreservesMessagePropertiesConnectionFactoryAdapter;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.stomp.PreservesHeadersStompSessionAdapter;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import org.junit.jupiter.api.Test;

import javax.jms.ConnectionFactory;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class PreservesJmsMessagePropertiesStrategyTest {
    private final String beanName = "name";
    private final PreservesJmsMessagePropertiesStrategy processor =
            new PreservesJmsMessagePropertiesStrategy(new PropagationProperties(), new EurekaInstanceProperties());

    @Test
    public void should_skip_bean() {
        Object bean = new Object();
        assertThat(processor.postProcessAfterInitialization(bean, beanName), is(bean));
    }

    @Test
    public void should_skip_propagator() {
        PreservesMessagePropertiesConnectionFactoryAdapter bean =
                new PreservesMessagePropertiesConnectionFactoryAdapter(null, null, null,
                        new EchoMessagePropertyEncoder());
        assertThat(processor.postProcessAfterInitialization(bean, beanName), is(bean));
    }

    @Test
    public void should_decorate() {
        processor.setEncoderType(EchoMessagePropertyEncoder.class);
        processor.setProperties(new PropagationProperties());
        processor.setEurekaInstanceProperties(new EurekaInstanceProperties());
        assertThat(processor.postProcessAfterInitialization(mock(ConnectionFactory.class), beanName).getClass(), equalTo(PreservesMessagePropertiesConnectionFactoryAdapter.class));
        processor.postProcessAfterInitialization(mock(ConnectionFactory.class), beanName);
    }

    @Test()
    public void should_fail_to_decorate_unaccessible_encoder_class() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    processor.setEncoderType(EchoMessagePropertyEncoder1.class);
                    processor.setProperties(new PropagationProperties());
                    processor.setEurekaInstanceProperties(new EurekaInstanceProperties());
                    processor.postProcessAfterInitialization(mock(ConnectionFactory.class), beanName);
                });
    }

    @Test()
    public void should_fail_to_decorate_encoder_with_no_default_constructor() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    processor.setEncoderType(EchoMessagePropertyEncoder2.class);
                    processor.setProperties(new PropagationProperties());
                    processor.setEurekaInstanceProperties(new EurekaInstanceProperties());
                    processor.postProcessAfterInitialization(mock(ConnectionFactory.class), beanName);
                });
    }

    @Test
    public void should_skip_stomp_session() {
        PropagationProperties propagationProperties = new PropagationProperties();
        propagationProperties.getJms().getExcludes().add(compile(beanName));
        processor.setProperties(propagationProperties);
        processor.setEurekaInstanceProperties(new EurekaInstanceProperties());
        assertThat(processor.postProcessAfterInitialization(mock(ConnectionFactory.class), beanName).getClass(), not(equalTo(PreservesHeadersStompSessionAdapter.class)));
    }

    private static class EchoMessagePropertyEncoder1 extends EchoMessagePropertyEncoder {
    }

    public static class EchoMessagePropertyEncoder2 extends EchoMessagePropertyEncoder {
        public EchoMessagePropertyEncoder2(String toto) {
        }
    }
}