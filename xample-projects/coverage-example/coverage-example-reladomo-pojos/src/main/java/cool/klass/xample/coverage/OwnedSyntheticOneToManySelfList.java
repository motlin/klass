package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedSyntheticOneToManySelfList extends OwnedSyntheticOneToManySelfListAbstract
{
    public OwnedSyntheticOneToManySelfList()
    {
    }

    public OwnedSyntheticOneToManySelfList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedSyntheticOneToManySelfList(Collection c)
    {
        super(c);
    }

    public OwnedSyntheticOneToManySelfList(Operation operation)
    {
        super(operation);
    }
}
