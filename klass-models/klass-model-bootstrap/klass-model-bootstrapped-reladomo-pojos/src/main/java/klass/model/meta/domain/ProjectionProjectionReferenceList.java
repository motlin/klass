package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionProjectionReferenceList extends ProjectionProjectionReferenceListAbstract
{
    public ProjectionProjectionReferenceList()
    {
    }

    public ProjectionProjectionReferenceList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionProjectionReferenceList(Collection c)
    {
        super(c);
    }

    public ProjectionProjectionReferenceList(Operation operation)
    {
        super(operation);
    }
}
