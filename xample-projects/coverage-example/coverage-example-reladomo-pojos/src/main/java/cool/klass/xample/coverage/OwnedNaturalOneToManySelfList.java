package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedNaturalOneToManySelfList extends OwnedNaturalOneToManySelfListAbstract
{
    public OwnedNaturalOneToManySelfList()
    {
    }

    public OwnedNaturalOneToManySelfList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedNaturalOneToManySelfList(Collection c)
    {
        super(c);
    }

    public OwnedNaturalOneToManySelfList(Operation operation)
    {
        super(operation);
    }
}
