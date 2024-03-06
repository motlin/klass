package cool.klass.reladomo.graphql.data.fetcher;

import java.time.LocalDate;

import com.gs.fw.common.mithra.attribute.DateAttribute;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ReladomoLocalDateDataFetcher<Input> implements DataFetcher<LocalDate>
{
    private final DateAttribute<Input> dateAttribute;

    public ReladomoLocalDateDataFetcher(DateAttribute<Input> dateAttribute)
    {
        this.dateAttribute = dateAttribute;
    }

    @Override
    public LocalDate get(DataFetchingEnvironment environment)
    {
        Input persistentInstance = environment.getSource();
        if (persistentInstance == null)
        {
            return null;
        }

        if (this.dateAttribute.isAttributeNull(persistentInstance))
        {
            return null;
        }

        java.sql.Date result = (java.sql.Date) this.dateAttribute.valueOf(persistentInstance);
        return result.toLocalDate();
    }
}
