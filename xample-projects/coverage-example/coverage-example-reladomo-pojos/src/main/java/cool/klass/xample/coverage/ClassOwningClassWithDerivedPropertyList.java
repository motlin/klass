package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassOwningClassWithDerivedPropertyList
        extends ClassOwningClassWithDerivedPropertyListAbstract
{
    public ClassOwningClassWithDerivedPropertyList()
    {
    }

    public ClassOwningClassWithDerivedPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public ClassOwningClassWithDerivedPropertyList(Collection c)
    {
        super(c);
    }

    public ClassOwningClassWithDerivedPropertyList(Operation operation)
    {
        super(operation);
    }
}
