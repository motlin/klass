package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class UserList extends UserListAbstract
{
    public UserList()
    {
    }

    public UserList(int initialSize)
    {
        super(initialSize);
    }

    public UserList(Collection collection)
    {
        super(collection);
    }

    public UserList(Operation operation)
    {
        super(operation);
    }
}
