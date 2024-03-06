package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class FinalPropertiesList
        extends FinalPropertiesListAbstract
{
    public FinalPropertiesList()
    {
    }

    public FinalPropertiesList(int initialSize)
    {
        super(initialSize);
    }

    public FinalPropertiesList(Collection c)
    {
        super(c);
    }

    public FinalPropertiesList(Operation operation)
    {
        super(operation);
    }
}
