package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionDataTypePropertyList extends ProjectionDataTypePropertyListAbstract
{
    public ProjectionDataTypePropertyList()
    {
    }

    public ProjectionDataTypePropertyList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionDataTypePropertyList(Collection c)
    {
        super(c);
    }

    public ProjectionDataTypePropertyList(Operation operation)
    {
        super(operation);
    }
}
