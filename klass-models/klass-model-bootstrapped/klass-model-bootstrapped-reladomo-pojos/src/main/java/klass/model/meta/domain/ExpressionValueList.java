package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ExpressionValueList extends ExpressionValueListAbstract
{
    public ExpressionValueList()
    {
    }

    public ExpressionValueList(int initialSize)
    {
        super(initialSize);
    }

    public ExpressionValueList(Collection c)
    {
        super(c);
    }

    public ExpressionValueList(Operation operation)
    {
        super(operation);
    }
}
