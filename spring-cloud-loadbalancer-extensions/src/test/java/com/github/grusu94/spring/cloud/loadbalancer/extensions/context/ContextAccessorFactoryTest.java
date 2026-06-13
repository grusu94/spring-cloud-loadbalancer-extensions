package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ContextAccessorFactory to verify correct accessor selection.
 */
@DisplayName("ContextAccessorFactory Tests")
class ContextAccessorFactoryTest {

    @BeforeEach
    void setUp() {
        // Clean up context before each test
        ContextAccessorFactory.getContextAccessor().remove();
    }

    @Test
    @DisplayName("Should return a valid ContextAccessor")
    void testGetAccessor() {
        ContextAccessor accessor = ContextAccessorFactory.getContextAccessor();
        assertNotNull(accessor);
    }

    @Test
    @DisplayName("Should return the same accessor instance")
    void testGetAccessorSingleton() {
        ContextAccessor accessor1 = ContextAccessorFactory.getContextAccessor();
        ContextAccessor accessor2 = ContextAccessorFactory.getContextAccessor();
        assertSame(accessor1, accessor2);
    }

    @Test
    @DisplayName("Should correctly identify reactive mode")
    void testIsReactive() {
        boolean isReactive = ContextAccessorFactory.isReactiveApplication();
        // The result depends on whether reactor is on the classpath
        assertFalse(isReactive);
    }

    @Test
    @DisplayName("Should use ThreadLocalContextAccessor when Reactor is not available")
    void testAccessorTypeWhenReactorNotAvailable() {
        ContextAccessor accessor = ContextAccessorFactory.getContextAccessor();
        if (!ContextAccessorFactory.isReactiveApplication()) {
            assertInstanceOf(ThreadLocalContextAccessor.class, accessor);
        }
    }

    @Test
    @DisplayName("Should use ReactorContextAccessor when Reactor is available")
    void testAccessorTypeWhenReactorAvailable() {
        ContextAccessor accessor = ContextAccessorFactory.getContextAccessor();
        if (ContextAccessorFactory.isReactiveApplication()) {
            assertInstanceOf(ReactorContextAccessor.class, accessor);
        }
    }

    @Test
    @DisplayName("Should store and retrieve context through factory")
    void testContextStorageAndRetrieval() {
        ContextAccessor accessor = ContextAccessorFactory.getContextAccessor();
        
        // Only test storage/retrieval for ThreadLocal (servlet) mode
        // Reactor mode requires reactive chains to work properly
        if (!ContextAccessorFactory.isReactiveApplication()) {
            ExecutionContext context = new DefaultExecutionContext();
            context.put("testKey", "testValue");

            accessor.switchTo(context);
            ExecutionContext retrieved = accessor.current();

            assertEquals("testValue", retrieved.get("testKey"));
        }
    }

    @Test
    @DisplayName("Should remove context through factory")
    void testContextRemoval() {
        ContextAccessor accessor = ContextAccessorFactory.getContextAccessor();
        
        // Only test removal for ThreadLocal (servlet) mode
        // Reactor mode requires reactive chains to work properly
        if (!ContextAccessorFactory.isReactiveApplication()) {
            ExecutionContext context = new DefaultExecutionContext();
            context.put("testKey", "testValue");

            accessor.switchTo(context);
            ExecutionContext removed = accessor.remove();

            assertEquals("testValue", removed.get("testKey"));
            
            // After removal, a fresh context should be returned
            ExecutionContext newContext = accessor.current();
            assertNull(newContext.get("testKey"));
        }
    }
}
