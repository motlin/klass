package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class TypeMemberReferencePathList extends TypeMemberReferencePathListAbstract
{
    public TypeMemberReferencePathList()
    {
    }

    public TypeMemberReferencePathList(int initialSize)
    {
        super(initialSize);
    }

    public TypeMemberReferencePathList(Collection c)
    {
        super(c);
    }

    public TypeMemberReferencePathList(Operation operation)
    {
        super(operation);
    }
}
