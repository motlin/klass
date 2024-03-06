package com.stackoverflow;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class QuestionVersionList extends QuestionVersionListAbstract
{
    public QuestionVersionList()
    {
    }

    public QuestionVersionList(int initialSize)
    {
        super(initialSize);
    }

    public QuestionVersionList(Collection<?> collection)
    {
        super(collection);
    }

    public QuestionVersionList(Operation operation)
    {
        super(operation);
    }
}
