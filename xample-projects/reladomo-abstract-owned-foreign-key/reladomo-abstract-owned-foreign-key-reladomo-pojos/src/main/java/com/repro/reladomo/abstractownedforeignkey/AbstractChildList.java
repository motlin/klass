package com.repro.reladomo.abstractownedforeignkey;

import com.gs.fw.finder.Operation;

public class AbstractChildList extends AbstractChildListAbstract
{
    public AbstractChildList()
    {
        super();
    }

    public AbstractChildList(int initialSize)
    {
        super(initialSize);
    }

    public AbstractChildList(Collection c)
    {
        super(c);
    }

    public AbstractChildList(Operation operation)
    {
        super(operation);
    }
}
