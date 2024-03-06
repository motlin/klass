package com.repro.reladomo.bug;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class BookList extends BookListAbstract
{
    public BookList()
    {
    }

    public BookList(int initialSize)
    {
        super(initialSize);
    }

    public BookList(Collection c)
    {
        super(c);
    }

    public BookList(Operation operation)
    {
        super(operation);
    }
}
