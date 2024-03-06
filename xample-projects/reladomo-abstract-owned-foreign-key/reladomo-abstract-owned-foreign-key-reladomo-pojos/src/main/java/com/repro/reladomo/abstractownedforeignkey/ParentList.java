package com.repro.reladomo.abstractownedforeignkey;

import com.gs.fw.finder.Operation;

public class ParentList extends ParentListAbstract
{
    public ParentList()
    {
        super();
    }

    public ParentList(int initialSize)
    {
        super(initialSize);
    }

    public ParentList(Collection c)
    {
        super(c);
    }

    public ParentList(Operation operation)
    {
        super(operation);
    }
}
