package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MemberReferencePathList extends MemberReferencePathListAbstract
{
    public MemberReferencePathList()
    {
    }

    public MemberReferencePathList(int initialSize)
    {
        super(initialSize);
    }

    public MemberReferencePathList(Collection c)
    {
        super(c);
    }

    public MemberReferencePathList(Operation operation)
    {
        super(operation);
    }
}
