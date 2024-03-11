package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class UrlList extends UrlListAbstract
{
    public UrlList()
    {
    }

    public UrlList(int initialSize)
    {
        super(initialSize);
    }

    public UrlList(Collection<?> c)
    {
        super(c);
    }

    public UrlList(Operation operation)
    {
        super(operation);
    }
}
