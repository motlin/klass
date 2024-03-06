package cool.klass.graphql.scalar.temporal;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

/**
 * Derived from https://github.com/graphql-java/graphql-java but for Instant instead of OffsetDateTime.
 */
@Internal
public class GraphQLTemporalScalar extends GraphQLScalarType
{
    public GraphQLTemporalScalar(@Nonnull String name)
    {
        super(name, "An RFC-3339 compliant " + name + " Scalar", new InstantCoercing());
    }

    @Nonnull
    private static String typeName(@Nullable Object input)
    {
        if (input == null)
        {
            return "null";
        }
        return input.getClass().getSimpleName();
    }

    private static class InstantCoercing implements Coercing<Instant, String>
    {
        @Nonnull
        @Override
        public String serialize(Object input)
        {
            Instant instant = getInstant(input);
            try
            {
                return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(instant);
            }
            catch (DateTimeException e)
            {
                throw new CoercingSerializeException(
                        "Unable to turn TemporalAccessor into OffsetDateTime because of : '" + e.getMessage() + "'.");
            }
        }

        private static Instant getInstant(Object input)
        {
            if (input instanceof Instant)
            {
                return (Instant) input;
            }

            if (input instanceof OffsetDateTime)
            {
                return ((OffsetDateTime) input).toInstant();
            }

            if (input instanceof ZonedDateTime)
            {
                return ((ZonedDateTime) input).toInstant();
            }

            if (input instanceof String)
            {
                String inputString = input.toString();
                OffsetDateTime parsedOffsetDateTime = parseOffsetDateTime(
                        inputString,
                        CoercingSerializeException::new);
                return parsedOffsetDateTime.toInstant();
            }

            String error = String.format(
                    "Expected something we can convert to 'java.time.OffsetDateTime' but was '%s'.",
                    typeName(input));
            throw new CoercingSerializeException(error);
        }

        @Override
        public Instant parseValue(Object input)
        {
            if (input instanceof Instant)
            {
                return (Instant) input;
            }

            if (input instanceof OffsetDateTime)
            {
                return ((OffsetDateTime) input).toInstant();
            }

            if (input instanceof ZonedDateTime)
            {
                return ((ZonedDateTime) input).toOffsetDateTime().toInstant();
            }

            if (input instanceof String)
            {
                String inputString = input.toString();
                OffsetDateTime parsedOffsetDateTime = parseOffsetDateTime(
                        inputString,
                        CoercingParseValueException::new);
                return parsedOffsetDateTime.toInstant();
            }

            String error = String.format("Expected a 'String' but was '%s'.", typeName(input));
            throw new CoercingParseValueException(error);
        }

        @Override
        public Instant parseLiteral(Object input)
        {
            if (!(input instanceof StringValue))
            {
                String error = String.format(
                        "Expected AST type 'StringValue' but was '%s'.",
                        typeName(input));
                throw new CoercingParseLiteralException(error);
            }
            return parseOffsetDateTime(
                    ((StringValue) input).getValue(),
                    CoercingParseLiteralException::new).toInstant();
        }

        private static OffsetDateTime parseOffsetDateTime(
                @Nonnull String s,
                @Nonnull Function<String, RuntimeException> exceptionMaker)
        {
            try
            {
                return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
            catch (DateTimeParseException e)
            {
                throw exceptionMaker.apply("Invalid RFC3339 value : '"
                        + s
                        + "'. because of : '"
                        + e.getMessage()
                        + "'");
            }
        }
    }
}
