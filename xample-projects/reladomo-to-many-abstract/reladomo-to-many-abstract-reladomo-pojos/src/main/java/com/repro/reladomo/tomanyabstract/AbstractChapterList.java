package com.repro.reladomo.tomanyabstract;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AbstractChapterList
        extends AbstractChapterListAbstract
{
    public AbstractChapterList()
    {
    }

    public AbstractChapterList(int initialSize)
    {
        super(initialSize);
    }

    public AbstractChapterList(Collection c)
    {
        super(c);
    }

    public AbstractChapterList(Operation operation)
    {
        super(operation);
    }
}
