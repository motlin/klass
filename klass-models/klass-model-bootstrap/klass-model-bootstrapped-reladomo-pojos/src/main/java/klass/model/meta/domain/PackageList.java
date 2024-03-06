package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PackageList extends PackageListAbstract
{
    public PackageList()
    {
    }

    public PackageList(int initialSize)
    {
        super(initialSize);
    }

    public PackageList(Collection<?> c)
    {
        super(c);
    }

    public PackageList(Operation operation)
    {
        super(operation);
    }
}
