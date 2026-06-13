package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ReactorContextAccessor to verify Reactor context management.
 * 
 * Note: These tests verify the basic functionality. For full reactive testing,
 * use reactor.test.StepVerifier or other Reactor test utilities.
 */
@DisplayName("ReactorContextAccessor Tests")
class ReactorContextAccessorTest {

    private ContextAccessor accessor;

    @Test
    @DisplayName("Should return singleton instance")
    void testSingleton() {
        ReactorContextAccessor accessor1 = ReactorContextAccessor.getInstance();
        ReactorContextAccessor accessor2 = ReactorContextAccessor.getInstance();
        assertSame(accessor1, accessor2);
    }

    @Test
    @DisplayName("Should return default context when Reactor context is not available")
    void testDefaultContextWhenNoReactorContext() {
        ReactorContextAccessor accessor = ReactorContextAccessor.getInstance();
        ExecutionContext context = accessor.current();
        
        assertNotNull(context);
        assertInstanceOf(DefaultExecutionContext.class, context);
    }

    @Test
    @DisplayName("Should implement ContextAccessor interface")
    void testImplementsContextAccessor() {
        ReactorContextAccessor accessor = ReactorContextAccessor.getInstance();
        assertInstanceOf(ContextAccessor.class, accessor);
    }

    @Test
    @DisplayName("switchTo should return the provided context")
    void testSwitchToReturnsContext() {
        ReactorContextAccessor accessor = ReactorContextAccessor.getInstance();
        ExecutionContext context = new DefaultExecutionContext();
        context.put("key", "value");

        ExecutionContext result = accessor.switchTo(context);
        assertEquals(context, result);
    }

    @Test
    @DisplayName("remove should return current context")
    void testRemoveReturnsCurrent() {
        ReactorContextAccessor accessor = ReactorContextAccessor.getInstance();
        ExecutionContext removed = accessor.remove();
        
        assertNotNull(removed);
        assertInstanceOf(DefaultExecutionContext.class, removed);
    }

    @Test
    @DisplayName("current should always return a non-null ExecutionContext")
    void testCurrentNeverReturnsNull() {
        ReactorContextAccessor accessor = ReactorContextAccessor.getInstance();
        ExecutionContext context1 = accessor.current();
        ExecutionContext context2 = accessor.current();

        assertNotNull(context1);
        assertNotNull(context2);
    }
}
