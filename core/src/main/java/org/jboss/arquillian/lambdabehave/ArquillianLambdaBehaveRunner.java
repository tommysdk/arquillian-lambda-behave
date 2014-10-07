package org.jboss.arquillian.lambdabehave;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.List;

/**
 * @author Tommy Tynj&auml;
 */
public class ArquillianLambdaBehaveRunner extends Arquillian {

    public ArquillianLambdaBehaveRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return null;
    }
}
