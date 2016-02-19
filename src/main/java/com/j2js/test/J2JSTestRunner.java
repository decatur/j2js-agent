package com.j2js.test;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class J2JSTestRunner extends Runner implements Filterable {
    
    //private BlockJUnit4ClassRunner runner;
    //private static RunnerFactory runnerFactory;
    
//    static {
//        try {
//            runnerFactory = (RunnerFactory) Class.forName("com.j2js.test.RunnerFactoryImpl").newInstance();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    
    public J2JSTestRunner(Class<?> testClass) throws InitializationError {
        //runner = new J2JSTestRunner(testClass);
        throw new UnsupportedOperationException();
    }

    @Override
    public Description getDescription() {
        throw new UnsupportedOperationException();
        //return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        throw new UnsupportedOperationException();
        //runner.run(notifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        throw new UnsupportedOperationException();
        //runner.filter(filter);
    }

}
