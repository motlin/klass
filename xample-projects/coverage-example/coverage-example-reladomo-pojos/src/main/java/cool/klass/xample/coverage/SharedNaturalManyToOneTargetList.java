package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalManyToOneTargetList extends SharedNaturalManyToOneTargetListAbstract
{
    public SharedNaturalManyToOneTargetList()
    {
    }

    public SharedNaturalManyToOneTargetList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalManyToOneTargetList(Collection c)
    {
        super(c);
    }

    public SharedNaturalManyToOneTargetList(Operation operation)
    {
        super(operation);
    }
}
