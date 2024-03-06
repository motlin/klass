package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AllForeignKeyPropertiesList extends AllForeignKeyPropertiesListAbstract
{
    public AllForeignKeyPropertiesList()
    {
    }

    public AllForeignKeyPropertiesList(int initialSize)
    {
        super(initialSize);
    }

    public AllForeignKeyPropertiesList(Collection c)
    {
        super(c);
    }

    public AllForeignKeyPropertiesList(Operation operation)
    {
        super(operation);
    }
}
