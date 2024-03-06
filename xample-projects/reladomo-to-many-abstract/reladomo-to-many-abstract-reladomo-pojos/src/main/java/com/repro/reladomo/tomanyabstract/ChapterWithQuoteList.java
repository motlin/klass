package com.repro.reladomo.tomanyabstract;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ChapterWithQuoteList
        extends ChapterWithQuoteListAbstract
{
    public ChapterWithQuoteList()
    {
    }

    public ChapterWithQuoteList(int initialSize)
    {
        super(initialSize);
    }

    public ChapterWithQuoteList(Collection c)
    {
        super(c);
    }

    public ChapterWithQuoteList(Operation operation)
    {
        super(operation);
    }
}
