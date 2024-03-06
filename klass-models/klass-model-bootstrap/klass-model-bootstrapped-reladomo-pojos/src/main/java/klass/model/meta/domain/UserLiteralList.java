package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class UserLiteralList extends UserLiteralListAbstract
{
    public UserLiteralList()
    {
    }

    public UserLiteralList(int initialSize)
    {
        super(initialSize);
    }

    public UserLiteralList(Collection c)
    {
        super(c);
    }

    public UserLiteralList(Operation operation)
    {
        super(operation);
    }
}
