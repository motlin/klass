package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EnumerationPropertyModifierList extends EnumerationPropertyModifierListAbstract
{
    public EnumerationPropertyModifierList()
    {
    }

    public EnumerationPropertyModifierList(int initialSize)
    {
        super(initialSize);
    }

    public EnumerationPropertyModifierList(Collection<?> c)
    {
        super(c);
    }

    public EnumerationPropertyModifierList(Operation operation)
    {
        super(operation);
    }
}
