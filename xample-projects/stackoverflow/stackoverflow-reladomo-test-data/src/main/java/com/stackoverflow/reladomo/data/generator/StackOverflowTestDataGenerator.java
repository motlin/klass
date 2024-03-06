package com.stackoverflow.reladomo.data.generator;

import java.sql.Timestamp;
import java.time.Instant;

import javax.annotation.Nullable;

import com.gs.fw.common.mithra.MithraManagerProvider;
import com.stackoverflow.Question;

public final class StackOverflowTestDataGenerator
{
    private StackOverflowTestDataGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        StackOverflowTestDataGenerator.populateData();
    }

    public static void populateData()
    {
        MithraManagerProvider.getMithraManager()
                .executeTransactionalCommand(tx -> StackOverflowTestDataGenerator.populateDataInTransaction());
    }

    @Nullable
    public static Object populateDataInTransaction()
    {
        Question question = new Question();
        question.setTitle("Example title 1");
        question.setBody("Example body 1");
        question.setStatus("Example status 1");
        question.setCreatedById("Example userId 1");
        question.setLastUpdatedById("Example userId 1");
        question.setCreatedOn(Timestamp.from(Instant.now()));
        question.insert();

        return null;
    }
}
