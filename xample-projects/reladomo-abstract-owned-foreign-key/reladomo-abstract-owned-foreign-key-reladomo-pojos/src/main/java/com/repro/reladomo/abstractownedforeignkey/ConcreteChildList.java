package com.repro.reladomo.abstractownedforeignkey;

import com.gs.fw.finder.Operation;

public class ConcreteChildList extends ConcreteChildListAbstract
{
    public ConcreteChildList()
    {
        super();
    }

    public ConcreteChildList(int initialSize)
    {
        super(initialSize);
    }

    public ConcreteChildList(Collection c)
    {
        super(c);
    }

    public ConcreteChildList(Operation operation)
    {
        super(operation);
    }
}
