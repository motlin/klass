package com.stackoverflow.reladomo.data.generator;

import java.sql.Timestamp;
import java.time.Instant;

import javax.annotation.Nullable;

import com.gs.fw.common.mithra.MithraManagerProvider;
import com.stackoverflow.Answer;
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
        question.setTitle("Example title 🤪");
        question.setBody("Example body 🤪");
        question.setStatus("Example status 🤪");
        question.setCreatedById("Example userId 🤪");
        question.setLastUpdatedById("Example userId 🤪");
        question.setCreatedOn(Timestamp.from(Instant.now()));
        question.insert();

        Answer answer = new Answer();
        answer.setBody("Example body 🤪");
        answer.setQuestion(question);
        answer.insert();

        Answer answer2 = new Answer();
        answer2.setBody("Example body 💩");
        answer2.setQuestion(question);
        answer2.insert();

        Question question2 = new Question();
        question2.setTitle("Example title 💩");
        question2.setBody("Example body 💩");
        question2.setStatus("Example status 💩");
        question2.setCreatedById("Example userId 💩");
        question2.setLastUpdatedById("Example userId 💩");
        question2.setCreatedOn(Timestamp.from(Instant.now()));
        question2.insert();

        return null;
    }
}
