package com.stackoverflow.graphql.runtime.wiring;

import com.stackoverflow.QuestionFinder;
import com.stackoverflow.QuestionList;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class AllQuestionsDataFetcher implements DataFetcher<QuestionList>
{
    @Override
    public QuestionList get(DataFetchingEnvironment environment)
    {
        return QuestionFinder.findMany(QuestionFinder.all());
    }
}
