package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PropertiesRequiredList extends PropertiesRequiredListAbstract
{
    public PropertiesRequiredList()
    {
    }

    public PropertiesRequiredList(int initialSize)
    {
        super(initialSize);
    }

    public PropertiesRequiredList(Collection c)
    {
        super(c);
    }

    public PropertiesRequiredList(Operation operation)
    {
        super(operation);
    }
}
