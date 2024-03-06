package com.stackoverflow.graphql.runtime.wiring;

import java.util.function.Function;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import com.stackoverflow.Question;
import com.stackoverflow.meta.constants.StackOverflowDomainModel;
import graphql.schema.DataFetcher;
import graphql.schema.PropertyDataFetcher;
import graphql.schema.idl.RuntimeWiring;

public class StackOverflowRuntimeWiringBuilder
{
    private final DataStore dataStore;

    public StackOverflowRuntimeWiringBuilder(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    @Nonnull
    private PropertyDataFetcher<Object> fetching(DataTypeProperty dataTypeProperty)
    {
        Function<Object, Object> function = (Object object) -> this.dataStore.getDataTypeProperty(
                object,
                dataTypeProperty);

        return PropertyDataFetcher.fetching(function);
    }

    public RuntimeWiring buildRuntimeWiring()
    {
        return RuntimeWiring.newRuntimeWiring()
                .type(
                        "Query",
                        typeWiring -> typeWiring
                                .dataFetcher("question", new QuestionByIdDataFetcher())
                                .dataFetcher("questions", new AllQuestionsDataFetcher()))
                .type(
                        "Question",
                        typeWiring -> typeWiring
                                .dataFetcher("id", PropertyDataFetcher.fetching(Question::getId))
                                .dataFetcher("title", PropertyDataFetcher.fetching(Question::getTitle))
                                .dataFetcher("body", PropertyDataFetcher.fetching(Question::getBody))
                                .dataFetcher("createdOn", this.fetching(StackOverflowDomainModel.Question.createdOn))
                                .dataFetcher("systemFrom", this.fetching(StackOverflowDomainModel.Question.systemFrom))
                                .dataFetcher("systemTo", this.fetching(StackOverflowDomainModel.Question.systemTo))
                                .dataFetcher(
                                        "status",
                                        this.fetching(Question::getStatus, StackOverflowDomainModel.Status))
                                .dataFetcher(
                                        "tags",
                                        PropertyDataFetcher.fetching((Question question) -> question.getTags().getTags()))
                )
                .build();
    }

    private <T> DataFetcher<?> fetching(
            Function<T, String> enumerationLiteralNameFunction,
            Enumeration enumeration)
    {
        return PropertyDataFetcher.fetching(enumerationLiteralNameFunction
                .andThen(enumerationLiteralName -> this.getEnumerationLiteralPrettyName(
                        enumeration,
                        enumerationLiteralName)));
    }

    @Nonnull
    private Object getEnumerationLiteralPrettyName(Enumeration enumeration, String enumerationLiteralName)
    {
        EnumerationLiteral enumerationLiteral = enumeration.getEnumerationLiterals()
                .select(each -> each.getPrettyName().equals(enumerationLiteralName))
                .getOnly();

        return enumerationLiteral.getName();
    }
}
