package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassWithDerivedPropertyList
        extends ClassWithDerivedPropertyListAbstract
{
    public ClassWithDerivedPropertyList()
    {
    }

    public ClassWithDerivedPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public ClassWithDerivedPropertyList(Collection c)
    {
        super(c);
    }

    public ClassWithDerivedPropertyList(Operation operation)
    {
        super(operation);
    }
}
