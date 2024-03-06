package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AllKeyPropertiesList extends AllKeyPropertiesListAbstract
{
    public AllKeyPropertiesList()
    {
    }

    public AllKeyPropertiesList(int initialSize)
    {
        super(initialSize);
    }

    public AllKeyPropertiesList(Collection c)
    {
        super(c);
    }

    public AllKeyPropertiesList(Operation operation)
    {
        super(operation);
    }
}
