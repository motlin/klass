package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.liftwizard.dropwizard.testing.junit.AbstractDropwizardAppTest;

public class AbstractKlassBootstrappedMetaModelApplicationTest
        extends AbstractDropwizardAppTest
{
    @Nonnull
    @Override
    protected DropwizardAppRule getDropwizardAppRule()
    {
        return new DropwizardAppRule<>(
                KlassBootstrappedMetaModelApplication.class,
                ResourceHelpers.resourceFilePath("config-test.json5"));
    }
}
