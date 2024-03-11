package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class VariableReferenceList extends VariableReferenceListAbstract
{
    public VariableReferenceList()
    {
    }

    public VariableReferenceList(int initialSize)
    {
        super(initialSize);
    }

    public VariableReferenceList(Collection c)
    {
        super(c);
    }

    public VariableReferenceList(Operation operation)
    {
        super(operation);
    }
}
