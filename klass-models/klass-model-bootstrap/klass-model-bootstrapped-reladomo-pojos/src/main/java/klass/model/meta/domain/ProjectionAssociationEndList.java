package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionAssociationEndList extends ProjectionAssociationEndListAbstract
{
    public ProjectionAssociationEndList()
    {
    }

    public ProjectionAssociationEndList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionAssociationEndList(Collection c)
    {
        super(c);
    }

    public ProjectionAssociationEndList(Operation operation)
    {
        super(operation);
    }
}
