package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ThreadLocalContextAccessor to verify ThreadLocal-based context management.
 */
@DisplayName("ThreadLocalContextAccessor Tests")
class ThreadLocalContextAccessorTest {

    private ContextAccessor accessor;

    @BeforeEach
    void setUp() {
        accessor = ThreadLocalContextAccessor.getInstance();
        accessor.remove();
    }

    @Test
    @DisplayName("Should return singleton instance")
    void testSingleton() {
        ContextAccessor accessor1 = ThreadLocalContextAccessor.getInstance();
        ContextAccessor accessor2 = ThreadLocalContextAccessor.getInstance();
        assertSame(accessor1, accessor2);
    }

    @Test
    @DisplayName("Should store and retrieve context")
    void testStoreAndRetrieve() {
        ExecutionContext context = new DefaultExecutionContext();
        context.put("key1", "value1");
        context.put("key2", "value2");

        accessor.switchTo(context);

        ExecutionContext retrieved = accessor.current();
        assertEquals("value1", retrieved.get("key1"));
        assertEquals("value2", retrieved.get("key2"));
    }

    @Test
    @DisplayName("Should return default context when none is set")
    void testDefaultContext() {
        accessor.remove();
        ExecutionContext context = accessor.current();
        assertNotNull(context);
        assertInstanceOf(DefaultExecutionContext.class, context);
    }

    @Test
    @DisplayName("Should remove context")
    void testRemoveContext() {
        ExecutionContext context = new DefaultExecutionContext();
        context.put("testKey", "testValue");
        accessor.switchTo(context);

        ExecutionContext removed = accessor.remove();
        assertEquals("testValue", removed.get("testKey"));

        // After removal, should get a fresh context
        ExecutionContext newContext = accessor.current();
        assertNull(newContext.get("testKey"));
    }

    @Test
    @DisplayName("Should switch context")
    void testSwitchContext() {
        ExecutionContext context1 = new DefaultExecutionContext();
        context1.put("id", "context1");

        ExecutionContext context2 = new DefaultExecutionContext();
        context2.put("id", "context2");

        accessor.switchTo(context1);
        assertEquals("context1", accessor.current().get("id"));

        accessor.switchTo(context2);
        assertEquals("context2", accessor.current().get("id"));
    }

    @Test
    @DisplayName("Should maintain separate contexts for different threads")
    void testThreadIsolation() throws InterruptedException {
        ExecutionContext context1 = new DefaultExecutionContext();
        context1.put("threadId", "main");
        accessor.switchTo(context1);

        Thread thread = new Thread(() -> {
            ExecutionContext threadContext = new DefaultExecutionContext();
            threadContext.put("threadId", "worker");
            ThreadLocalContextAccessor.getInstance().switchTo(threadContext);

            assertEquals("worker", ThreadLocalContextAccessor.getInstance().current().get("threadId"));
        });

        thread.start();
        thread.join();

        // Main thread context should remain unchanged
        assertEquals("main", accessor.current().get("threadId"));
    }
}
