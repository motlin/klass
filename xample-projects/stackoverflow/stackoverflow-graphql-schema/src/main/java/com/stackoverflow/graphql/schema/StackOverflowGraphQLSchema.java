package com.stackoverflow.graphql.schema;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

import javax.annotation.Nonnull;

import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

public final class StackOverflowGraphQLSchema
{
    private StackOverflowGraphQLSchema()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @Nonnull
    public static TypeDefinitionRegistry getTypeDefinitionRegistry()
    {
        String fileName = "stackoverflow.graphqls";
        return getTypeDefinitionRegistry(fileName);
    }

    @Nonnull
    public static TypeDefinitionRegistry getTypeDefinitionRegistry(String fileName)
    {
        String       schemaInput  = loadSchema(fileName);
        SchemaParser schemaParser = new SchemaParser();

        TypeDefinitionRegistry typeRegistry           = new TypeDefinitionRegistry();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaInput);
        typeRegistry.merge(typeDefinitionRegistry);
        return typeRegistry;
    }

    private static String loadSchema(String fileName)
    {
        return slurp(fileName, StackOverflowGraphQLSchema.class);
    }

    public static String slurp(String resourceClassPathLocation, Class<?> callingClass)
    {
        InputStream inputStream = callingClass.getResourceAsStream(resourceClassPathLocation);
        Objects.requireNonNull(inputStream, resourceClassPathLocation);
        return slurp(inputStream);
    }

    public static String slurp(InputStream inputStream)
    {
        try (Scanner scanner = new Scanner(inputStream))
        {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
