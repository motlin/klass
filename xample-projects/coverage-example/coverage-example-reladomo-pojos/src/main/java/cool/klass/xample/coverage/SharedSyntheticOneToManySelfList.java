package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedSyntheticOneToManySelfList extends SharedSyntheticOneToManySelfListAbstract
{
    public SharedSyntheticOneToManySelfList()
    {
    }

    public SharedSyntheticOneToManySelfList(int initialSize)
    {
        super(initialSize);
    }

    public SharedSyntheticOneToManySelfList(Collection c)
    {
        super(c);
    }

    public SharedSyntheticOneToManySelfList(Operation operation)
    {
        super(operation);
    }
}
