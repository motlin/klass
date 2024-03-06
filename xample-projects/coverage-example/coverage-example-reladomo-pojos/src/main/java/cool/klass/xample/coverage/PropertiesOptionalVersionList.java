package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PropertiesOptionalVersionList extends PropertiesOptionalVersionListAbstract
{
    public PropertiesOptionalVersionList()
    {
    }

    public PropertiesOptionalVersionList(int initialSize)
    {
        super(initialSize);
    }

    public PropertiesOptionalVersionList(Collection c)
    {
        super(c);
    }

    public PropertiesOptionalVersionList(Operation operation)
    {
        super(operation);
    }
}
