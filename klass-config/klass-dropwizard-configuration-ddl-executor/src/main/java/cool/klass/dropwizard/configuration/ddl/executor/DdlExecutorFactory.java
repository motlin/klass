package cool.klass.dropwizard.configuration.ddl.executor;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

public class DdlExecutorFactory
{
    // TODO: Create a default H2 in-memory configuration?
    // TODO: Share a DataSource more broadly?

    // Should usually be disabled in production
    private boolean enabled = false;

    private @Valid DataSourceFactory dataSourceFactory;

    @JsonProperty
    public boolean isEnabled()
    {
        return this.enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @JsonProperty("dataSource")
    public DataSourceFactory getDataSourceFactory()
    {
        return this.dataSourceFactory;
    }

    @JsonProperty("dataSource")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory)
    {
        this.dataSourceFactory = dataSourceFactory;
    }
}
