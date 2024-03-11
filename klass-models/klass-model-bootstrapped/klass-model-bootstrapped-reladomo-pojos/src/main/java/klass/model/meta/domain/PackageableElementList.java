package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PackageableElementList
        extends PackageableElementListAbstract
{
    public PackageableElementList()
    {
    }

    public PackageableElementList(int initialSize)
    {
        super(initialSize);
    }

    public PackageableElementList(Collection c)
    {
        super(c);
    }

    public PackageableElementList(Operation operation)
    {
        super(operation);
    }
}
