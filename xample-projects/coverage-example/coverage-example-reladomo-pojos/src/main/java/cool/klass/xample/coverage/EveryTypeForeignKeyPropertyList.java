package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EveryTypeForeignKeyPropertyList extends EveryTypeForeignKeyPropertyListAbstract
{
    public EveryTypeForeignKeyPropertyList()
    {
    }

    public EveryTypeForeignKeyPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public EveryTypeForeignKeyPropertyList(Collection c)
    {
        super(c);
    }

    public EveryTypeForeignKeyPropertyList(Operation operation)
    {
        super(operation);
    }
}
