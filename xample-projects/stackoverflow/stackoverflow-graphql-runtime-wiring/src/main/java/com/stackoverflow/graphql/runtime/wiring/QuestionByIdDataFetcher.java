package com.stackoverflow.graphql.runtime.wiring;

import com.gs.fw.common.mithra.finder.Operation;
import com.stackoverflow.Question;
import com.stackoverflow.QuestionFinder;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class QuestionByIdDataFetcher implements DataFetcher<Question>
{
    @Override
    public Question get(DataFetchingEnvironment environment)
    {
        Long      id        = environment.getArgument("id");
        Operation operation = QuestionFinder.id().eq(id);
        return QuestionFinder.findOne(operation);
    }
}
