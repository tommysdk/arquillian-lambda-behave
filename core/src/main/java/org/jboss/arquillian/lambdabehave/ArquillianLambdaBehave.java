package org.jboss.arquillian.lambdabehave;

import com.insightfullogic.lambdabehave.impl.AbstractSuiteRunner;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptorBuilder;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

/**
 * @author Tommy Tynj&auml;
 */
public class ArquillianLambdaBehave extends AbstractSuiteRunner {


    public ArquillianLambdaBehave(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void run(final RunNotifier notifier) {
        //arquillian.run(notifier);
        // first time we're being initialized
        if (!State.hasTestAdaptor()) {
            // no, initialization has been attempted before and failed, refuse to do anything else
            if (State.hasInitializationException()) {
                // failed on suite level, ignore children
                //notifier.fireTestIgnored(getDescription());
                notifier.fireTestFailure(
                        new Failure(getDescription(),
                                new RuntimeException(
                                        "Arquillian has previously been attempted initialized, but failed. See cause for previous exception",
                                        State.getInitializationException())));
            } else {
                TestRunnerAdaptor adaptor = TestRunnerAdaptorBuilder.build();
                try {
                    // don't set it if beforeSuite fails
                    adaptor.beforeSuite();
                    State.testAdaptor(adaptor);
                } catch (Exception e) {
                    // caught exception during BeforeSuite, mark this as failed
                    State.caughtInitializationException(e);
                    notifier.fireTestFailure(new Failure(getDescription(), e));
                }
            }
        }
        notifier.addListener(new RunListener() {
            @Override
            public void testRunFinished(Result result) throws Exception {
                State.runnerFinished();
                shutdown();
            }

            private void shutdown() {
                try {
                    if (State.isLastRunner()) {
                        try {
                            if (State.hasTestAdaptor()) {
                                TestRunnerAdaptor adaptor = State.getTestAdaptor();
                                adaptor.afterSuite();
                                adaptor.shutdown();
                            }
                        } finally {
                            State.clean();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Could not run @AfterSuite", e);
                }
            }
        });
        // initialization ok, run children
        if (State.hasTestAdaptor()) {
            super.run(notifier);
        }
    }

    // TODO: with before classes
    // TODO: with after classes
    // TODO: with befores
    // TODO: with afters
    // TODO: method invoker
    // TODO: multiExecute


}
