package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassifierInterfaceMappingList extends ClassifierInterfaceMappingListAbstract
{
    public ClassifierInterfaceMappingList()
    {
    }

    public ClassifierInterfaceMappingList(int initialSize)
    {
        super(initialSize);
    }

    public ClassifierInterfaceMappingList(Collection c)
    {
        super(c);
    }

    public ClassifierInterfaceMappingList(Operation operation)
    {
        super(operation);
    }
}
