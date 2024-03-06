package com.stackoverflow.dropwizard.test;

import javax.annotation.Nonnull;

import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.liftwizard.dropwizard.testing.junit.AbstractDropwizardAppTest;

public abstract class AbstractStackOverflowApplicationTest
        extends AbstractDropwizardAppTest
{
    @Nonnull
    @Override
    protected DropwizardAppRule getDropwizardAppRule()
    {
        return new DropwizardAppRule<>(
                StackOverflowApplication.class,
                ResourceHelpers.resourceFilePath("config-test.json5"));
    }
}
