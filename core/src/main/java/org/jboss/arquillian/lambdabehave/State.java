package org.jboss.arquillian.lambdabehave;

import org.jboss.arquillian.test.spi.TestRunnerAdaptor;

/**
 * @author Tommy Tynj&auml;
 */
public class State {
    /*
     * @HACK
     * JUnit Hack:
     * In JUnit a Exception is thrown and verified/swallowed if @Test(expected) is set. We need to transfer this
     * Exception back to the client so the client side can throw it again. This to avoid a incontainer working but failing
     * on client side due to no Exception thrown.
     */
    // Cleaned up in JUnitTestRunner
    private static ThreadLocal<Throwable> caughtTestException = new ThreadLocal<Throwable>();

    /*
     * Keep track of previous BeforeSuite initialization exceptions
     */
    private static ThreadLocal<Throwable> caughtInitializationException = new ThreadLocal<Throwable>();

    /*
     * @HACK
     * Eclipse hack:
     * When running multiple TestCases, Eclipse will create a new runner for each of them.
     * This results in that AfterSuite is call pr TestCase, but BeforeSuite only on the first created instance.
     * A instance of all TestCases are created before the first one is started, so we keep track of which one
     * was the last one created. The last one created is the only one allowed to call AfterSuite.
     */
    private static ThreadLocal<Integer> lastCreatedRunner = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return new Integer(0);
        }
    };

    private static ThreadLocal<TestRunnerAdaptor> deployableTest = new ThreadLocal<TestRunnerAdaptor>();

    static void runnerStarted() {
        lastCreatedRunner.set(lastCreatedRunner.get() + 1);
    }

    static Integer runnerFinished() {
        Integer currentCount = lastCreatedRunner.get() - 1;
        lastCreatedRunner.set(currentCount);
        return currentCount;
    }

    static Integer runnerCurrent() {
        return lastCreatedRunner.get();
    }

    static boolean isLastRunner() {
        return runnerCurrent() == 0;
    }

    static void testAdaptor(TestRunnerAdaptor adaptor) {
        deployableTest.set(adaptor);
    }

    static boolean hasTestAdaptor() {
        return getTestAdaptor() != null;
    }

    static TestRunnerAdaptor getTestAdaptor() {
        return deployableTest.get();
    }

    static void caughtInitializationException(Throwable throwable) {
        caughtInitializationException.set(throwable);
    }

    static boolean hasInitializationException() {
        return getInitializationException() != null;
    }

    static Throwable getInitializationException() {
        return caughtInitializationException.get();
    }

    public static void caughtTestException(Throwable throwable) {
        if (throwable == null) {
            caughtTestException.remove();
        } else {
            caughtTestException.set(throwable);
        }
    }

    public static boolean hasTestException() {
        return getTestException() != null;
    }

    public static Throwable getTestException() {
        return caughtTestException.get();
    }

    static void clean() {
        lastCreatedRunner.remove();
        deployableTest.remove();
        caughtInitializationException.remove();
    }
}
