package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ProjectionElementList extends ProjectionElementListAbstract
{
    public ProjectionElementList()
    {
    }

    public ProjectionElementList(int initialSize)
    {
        super(initialSize);
    }

    public ProjectionElementList(Collection c)
    {
        super(c);
    }

    public ProjectionElementList(Operation operation)
    {
        super(operation);
    }
}
