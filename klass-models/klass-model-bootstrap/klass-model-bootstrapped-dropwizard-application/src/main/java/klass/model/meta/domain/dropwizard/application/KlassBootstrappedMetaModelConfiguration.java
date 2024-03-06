package klass.model.meta.domain.dropwizard.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactory;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactoryProvider;

public class KlassBootstrappedMetaModelConfiguration
        extends AbstractKlassConfiguration
        implements SPARedirectFilterFactoryProvider
{
    private SPARedirectFilterFactory spaRedirectFilterFactory = new SPARedirectFilterFactory();

    @Override
    @JsonProperty("spaRedirectFilter")
    public SPARedirectFilterFactory getSPARedirectFilterFactory()
    {
        return this.spaRedirectFilterFactory;
    }

    @JsonProperty("spaRedirectFilter")
    public void setSPARedirectFilterFactory(SPARedirectFilterFactory spaRedirectFilterFactory)
    {
        this.spaRedirectFilterFactory = spaRedirectFilterFactory;
    }
}
