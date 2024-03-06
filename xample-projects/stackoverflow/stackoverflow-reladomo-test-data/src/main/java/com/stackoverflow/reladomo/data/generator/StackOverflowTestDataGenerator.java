package com.stackoverflow.reladomo.data.generator;

import java.sql.Timestamp;
import java.time.Instant;

import com.gs.fw.common.mithra.MithraManagerProvider;
import com.stackoverflow.Answer;
import com.stackoverflow.Question;
import com.stackoverflow.QuestionVersion;

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
        Question question = MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            Question question1 = new Question();
            question1.setTitle("Example title ☝️");
            question1.setBody("Example body ☝️");
            question1.setStatus("Example status ☝️");
            question1.setCreatedById("Example userId ☝️");
            question1.setLastUpdatedById("Example userId ☝️");
            question1.setCreatedOn(Timestamp.from(Instant.now()));
            question1.insert();

            QuestionVersion questionVersion = new QuestionVersion();
            questionVersion.setId(question1.getId());
            questionVersion.setNumber(1);
            questionVersion.setQuestion(question1);
            questionVersion.insert();

            Answer answer = new Answer();
            answer.setBody("Example body ☝️");
            answer.setQuestion(question1);
            answer.insert();

            Answer answer2 = new Answer();
            answer2.setBody("Example body ✌️");
            answer2.setQuestion(question1);
            answer2.insert();

            Question question2 = new Question();
            question2.setTitle("Example title ✌️");
            question2.setBody("Example body ✌️");
            question2.setStatus("Example status ✌️");
            question2.setCreatedById("Example userId ✌️");
            question2.setLastUpdatedById("Example userId ✌️");
            question2.setCreatedOn(Timestamp.from(Instant.now()));
            question2.insert();

            return question1;
        });

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            question.setTitle("Updated title ✍️");
            question.setBody("Updated body ✍️");
            question.setStatus("Updated status ✍️");
            question.setLastUpdatedById("Updated userId ✍️");

            QuestionVersion questionVersion = question.getVersion();
            questionVersion.setNumber(2);

            return null;
        });
    }
}
