package cool.klass.dropwizard.application;

import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.liftwizard.dropwizard.application.AbstractLiftwizardApplication;

public abstract class AbstractKlassApplication<T extends AbstractKlassConfiguration>
        extends AbstractLiftwizardApplication<T>
{
    protected AbstractKlassApplication(String name)
    {
        super(name);
    }
}
