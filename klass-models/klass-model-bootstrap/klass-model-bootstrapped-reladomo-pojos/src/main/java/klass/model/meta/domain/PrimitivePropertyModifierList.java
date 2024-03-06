package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PrimitivePropertyModifierList extends PrimitivePropertyModifierListAbstract
{
    public PrimitivePropertyModifierList()
    {
    }

    public PrimitivePropertyModifierList(int initialSize)
    {
        super(initialSize);
    }

    public PrimitivePropertyModifierList(Collection<?> c)
    {
        super(c);
    }

    public PrimitivePropertyModifierList(Operation operation)
    {
        super(operation);
    }
}
