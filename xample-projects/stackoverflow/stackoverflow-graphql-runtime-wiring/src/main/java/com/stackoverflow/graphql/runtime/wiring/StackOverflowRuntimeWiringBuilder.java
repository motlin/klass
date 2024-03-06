package com.stackoverflow.graphql.runtime.wiring;

import java.util.function.Supplier;

import cool.klass.graphql.scalar.temporal.GraphQLTemporalScalar;
import com.stackoverflow.graphql.data.fetcher.all.AllAnswerDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllAnswerVersionDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllQuestionDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllQuestionTagMappingDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllQuestionVersionDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllQuestionVoteDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllTagDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllTagVersionDataFetcher;
import com.stackoverflow.graphql.data.fetcher.all.AllUserDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.AnswerByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.AnswerVersionByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.QuestionByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.QuestionTagMappingByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.QuestionVersionByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.QuestionVoteByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.TagByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.TagVersionByKeyDataFetcher;
import com.stackoverflow.graphql.data.fetcher.key.UserByKeyDataFetcher;
import com.stackoverflow.graphql.type.runtime.wiring.AnswerTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.AnswerVersionTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.QuestionTagMappingTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.QuestionTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.QuestionVersionTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.QuestionVoteTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.TagTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.TagVersionTypeRuntimeWiringProvider;
import com.stackoverflow.graphql.type.runtime.wiring.UserTypeRuntimeWiringProvider;
import graphql.schema.idl.RuntimeWiring;

public class StackOverflowRuntimeWiringBuilder
        implements Supplier<RuntimeWiring>
{
    @Override
    public RuntimeWiring get()
    {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(new GraphQLTemporalScalar("Instant"))
                .scalar(new GraphQLTemporalScalar("TemporalInstant"))
                .scalar(new GraphQLTemporalScalar("TemporalRange"))
                .type(
                        "Query",
                        typeWiring -> typeWiring
                                .dataFetcher("answer", new AnswerByKeyDataFetcher())
                                .dataFetcher("answerVersion", new AnswerVersionByKeyDataFetcher())
                                .dataFetcher("question", new QuestionByKeyDataFetcher())
                                .dataFetcher("questionTagMapping", new QuestionTagMappingByKeyDataFetcher())
                                .dataFetcher("questionVersion", new QuestionVersionByKeyDataFetcher())
                                .dataFetcher("questionVote", new QuestionVoteByKeyDataFetcher())
                                .dataFetcher("tag", new TagByKeyDataFetcher())
                                .dataFetcher("tagVersion", new TagVersionByKeyDataFetcher())
                                .dataFetcher("user", new UserByKeyDataFetcher())
                                // TODO: plurals will be tricky to generate
                                .dataFetcher("answers", new AllAnswerDataFetcher())
                                .dataFetcher("answerVersions", new AllAnswerVersionDataFetcher())
                                .dataFetcher("questions", new AllQuestionDataFetcher())
                                .dataFetcher("questionTagMappings", new AllQuestionTagMappingDataFetcher())
                                .dataFetcher("questionVersions", new AllQuestionVersionDataFetcher())
                                .dataFetcher("questionVotes", new AllQuestionVoteDataFetcher())
                                .dataFetcher("tags", new AllTagDataFetcher())
                                .dataFetcher("tagVersions", new AllTagVersionDataFetcher())
                                .dataFetcher("users", new AllUserDataFetcher()))
                .type(new AnswerTypeRuntimeWiringProvider().get())
                .type(new AnswerVersionTypeRuntimeWiringProvider().get())
                .type(new QuestionTagMappingTypeRuntimeWiringProvider().get())
                .type(new QuestionTypeRuntimeWiringProvider().get())
                .type(new QuestionVersionTypeRuntimeWiringProvider().get())
                .type(new QuestionVoteTypeRuntimeWiringProvider().get())
                .type(new TagTypeRuntimeWiringProvider().get())
                .type(new TagVersionTypeRuntimeWiringProvider().get())
                .type(new UserTypeRuntimeWiringProvider().get())
                .build();
    }
}
