package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalOneToManySelfList extends SharedNaturalOneToManySelfListAbstract
{
    public SharedNaturalOneToManySelfList()
    {
    }

    public SharedNaturalOneToManySelfList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalOneToManySelfList(Collection c)
    {
        super(c);
    }

    public SharedNaturalOneToManySelfList(Operation operation)
    {
        super(operation);
    }
}
