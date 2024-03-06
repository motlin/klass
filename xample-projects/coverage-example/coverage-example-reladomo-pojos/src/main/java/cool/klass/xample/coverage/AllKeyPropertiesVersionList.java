package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AllKeyPropertiesVersionList extends AllKeyPropertiesVersionListAbstract
{
    public AllKeyPropertiesVersionList()
    {
    }

    public AllKeyPropertiesVersionList(int initialSize)
    {
        super(initialSize);
    }

    public AllKeyPropertiesVersionList(Collection c)
    {
        super(c);
    }

    public AllKeyPropertiesVersionList(Operation operation)
    {
        super(operation);
    }
}
