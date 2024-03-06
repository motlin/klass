package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PropertiesOptionalList extends PropertiesOptionalListAbstract
{
    public PropertiesOptionalList()
    {
    }

    public PropertiesOptionalList(int initialSize)
    {
        super(initialSize);
    }

    public PropertiesOptionalList(Collection c)
    {
        super(c);
    }

    public PropertiesOptionalList(Operation operation)
    {
        super(operation);
    }
}
