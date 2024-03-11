package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class UrlParameterList extends UrlParameterListAbstract
{
    public UrlParameterList()
    {
    }

    public UrlParameterList(int initialSize)
    {
        super(initialSize);
    }

    public UrlParameterList(Collection c)
    {
        super(c);
    }

    public UrlParameterList(Operation operation)
    {
        super(operation);
    }
}
