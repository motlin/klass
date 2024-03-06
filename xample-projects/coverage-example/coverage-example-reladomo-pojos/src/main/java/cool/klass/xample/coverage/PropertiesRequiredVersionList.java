package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PropertiesRequiredVersionList extends PropertiesRequiredVersionListAbstract
{
    public PropertiesRequiredVersionList()
    {
    }

    public PropertiesRequiredVersionList(int initialSize)
    {
        super(initialSize);
    }

    public PropertiesRequiredVersionList(Collection c)
    {
        super(c);
    }

    public PropertiesRequiredVersionList(Operation operation)
    {
        super(operation);
    }
}
