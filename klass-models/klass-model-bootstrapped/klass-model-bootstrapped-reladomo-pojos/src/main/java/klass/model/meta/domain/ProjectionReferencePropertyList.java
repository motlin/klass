package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionReferencePropertyList
        extends ProjectionReferencePropertyListAbstract
{
    public ProjectionReferencePropertyList()
    {
    }

    public ProjectionReferencePropertyList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionReferencePropertyList(Collection c)
    {
        super(c);
    }

    public ProjectionReferencePropertyList(Operation operation)
    {
        super(operation);
    }
}
