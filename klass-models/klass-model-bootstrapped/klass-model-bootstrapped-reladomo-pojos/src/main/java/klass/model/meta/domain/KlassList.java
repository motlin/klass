package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class KlassList extends KlassListAbstract
{
    public KlassList()
    {
    }

    public KlassList(int initialSize)
    {
        super(initialSize);
    }

    public KlassList(Collection<?> c)
    {
        super(c);
    }

    public KlassList(Operation operation)
    {
        super(operation);
    }
}
