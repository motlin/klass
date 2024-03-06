package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionWithAssociationEndList extends ProjectionWithAssociationEndListAbstract
{
    public ProjectionWithAssociationEndList()
    {
    }

    public ProjectionWithAssociationEndList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionWithAssociationEndList(Collection c)
    {
        super(c);
    }

    public ProjectionWithAssociationEndList(Operation operation)
    {
        super(operation);
    }
}
