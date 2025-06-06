package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class AbstractPreservesMessagePropertiesTest {
    private final Set<String> keys = new HashSet<>(asList("1", "2"));
    private final AbstractPreservesMessageProperties propagator =
            new AbstractPreservesMessageProperties(keys::contains, new EchoMessagePropertyEncoder()) {
    };
    private final Message message = mock(Message.class);

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void copy_matching_properties() throws Exception {
        when(message.getPropertyNames()).thenReturn(Collections.enumeration(asList("1", "2", "3")));
        when(message.getStringProperty("1")).thenReturn("1");
        when(message.getStringProperty("2")).thenReturn("2");
        when(message.getStringProperty("3")).thenReturn("3");
        propagator.copyFromMessage(message);
        verify(message, never()).getStringProperty(eq("3"));
        keys.forEach(x -> assertThat(current().get(x), is(x)));
        assertThat(current().containsKey("3"), is(false));
    }

    @Test
    public void fail_on_get_property_names() throws Exception {
        when(message.getPropertyNames()).thenThrow(JMSException.class);
        propagator.copyFromMessage(message);
        assertThat(current().entrySet(), empty());
    }

    @Test
    public void fail_on_get_property() throws Exception {
        when(message.getPropertyNames()).thenReturn(Collections.enumeration(asList("1", "2", "3")));
        when(message.getStringProperty("1")).thenReturn("1");
        when(message.getStringProperty("2")).thenThrow(JMSException.class);
        propagator.copyFromMessage(message);
        verify(message).getStringProperty(eq("1"));
        verify(message).getStringProperty(eq("2"));
        verify(message, never()).getStringProperty(eq("3"));
        assertThat(current().containsKey("1"), is(true));
        assertThat(current().entrySet().size(), is(1));
    }
}