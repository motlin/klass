package com.repro.reladomo.tomanyabstract;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ChapterWithIllustrationList
        extends ChapterWithIllustrationListAbstract
{
    public ChapterWithIllustrationList()
    {
    }

    public ChapterWithIllustrationList(int initialSize)
    {
        super(initialSize);
    }

    public ChapterWithIllustrationList(Collection c)
    {
        super(c);
    }

    public ChapterWithIllustrationList(Operation operation)
    {
        super(operation);
    }
}
