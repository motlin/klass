package cool.klass.dropwizard.configuration.ddl.executor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

public class DdlExecutorFactory
{
    // TODO: Create a default H2 in-memory configuration?
    // TODO: Share a DataSource more broadly?
    private @Valid @NotNull DataSourceFactory dataSourceFactory;

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
