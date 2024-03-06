package cool.klass.xample.coverage.dropwizard.test;

import javax.annotation.Nonnull;

import cool.klass.xample.coverage.dropwizard.application.CoverageExampleApplication;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.liftwizard.dropwizard.testing.junit.AbstractDropwizardAppTest;

public abstract class AbstractCoverageTest
        extends AbstractDropwizardAppTest
{
    @Nonnull
    @Override
    protected DropwizardAppRule getDropwizardAppRule()
    {
        return new DropwizardAppRule<>(
                CoverageExampleApplication.class,
                ResourceHelpers.resourceFilePath("config-test.json5"));
    }
}
