package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ThisMemberReferencePathList extends ThisMemberReferencePathListAbstract
{
    public ThisMemberReferencePathList()
    {
    }

    public ThisMemberReferencePathList(int initialSize)
    {
        super(initialSize);
    }

    public ThisMemberReferencePathList(Collection c)
    {
        super(c);
    }

    public ThisMemberReferencePathList(Operation operation)
    {
        super(operation);
    }
}
