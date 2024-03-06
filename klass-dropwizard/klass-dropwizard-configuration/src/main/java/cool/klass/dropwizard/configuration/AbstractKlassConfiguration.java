package cool.klass.dropwizard.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class AbstractKlassConfiguration extends Configuration
{
    private @NotNull @Valid KlassFactory klassFactory;

    public KlassFactory getKlassFactory()
    {
        return this.klassFactory;
    }

    @JsonProperty("klass")
    public void setKlassFactory(KlassFactory klassFactory)
    {
        this.klassFactory = klassFactory;
    }
}
