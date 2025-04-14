//package com.example.load_balancer_extensions.propagator.hystrix;
//
//
//import com.example.load_balancer_extensions.context.ExecutionContext;
//import com.example.load_balancer_extensions.propagator.concurrent.ContextAwareCallable;
//import jakarta.validation.constraints.NotNull;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import static com.example.load_balancer_extensions.propagator.concurrent.ContextAwareCallable.wrap;
//
//
///**
// * Preserves the {@link ExecutionContext} on async {@link Hystrix} commands: see <a href="https://github.com/Netflix/Hystrix/wiki/Plugins#concurrency-strategy)">Histrix Wiki</a>..
// *
// * @see ContextAwareCallable
// */
//public class ExecutionContextAwareHystrixStrategy extends HystrixConcurrencyStrategy {
//    /**
//     * The delegate Hystrix concurrent strategy.
//     */
//    private HystrixConcurrencyStrategy delegate;
//
//    /**
//     * Sole Constructor
//     *
//     * @param delegate the delegate Hystrix concurrent strategy.
//     */
//    public ExecutionContextAwareHystrixStrategy(@NotNull HystrixConcurrencyStrategy delegate) {
//        this.delegate = delegate;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
//        return delegate.getBlockingQueue(maxQueueSize);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public <T> HystrixRequestVariable<T> getRequestVariable(
//            HystrixRequestVariableLifecycle<T> rv) {
//        return delegate.getRequestVariable(rv);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//                                            HystrixProperty<Integer> corePoolSize,
//                                            HystrixProperty<Integer> maximumPoolSize,
//                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
//                                            BlockingQueue<Runnable> workQueue) {
//        return delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
//        return delegate.getThreadPool(threadPoolKey, threadPoolProperties);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        return wrap(delegate.wrapCallable(callable));
//    }
//}
