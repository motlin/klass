package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class UserVersionList
        extends UserVersionListAbstract
{
    public UserVersionList()
    {
    }

    public UserVersionList(int initialSize)
    {
        super(initialSize);
    }

    public UserVersionList(Collection c)
    {
        super(c);
    }

    public UserVersionList(Operation operation)
    {
        super(operation);
    }
}
