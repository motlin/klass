package cool.klass.dropwizard.configuration.domain.model.loader.constant;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.model.meta.domain.api.DomainModel;

@JsonTypeName("constant")
@AutoService(DomainModelFactory.class)
public class DomainModelConstantFactory implements DomainModelFactory
{
    // TODO: Add @NotBlank validation
    // javax.validation.UnexpectedTypeException: HV000030: No validator could be found for constraint 'javax.validation.constraints.NotBlank' validating type 'java.lang.String'. Check configuration for 'fullyQualifiedClassName'
    private @Valid @NotNull String fullyQualifiedClassName;

    @Nonnull
    @Override
    public DomainModel createDomainModel()
    {
        try
        {
            return this.getDomainModelOrThrow();
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private DomainModel getDomainModelOrThrow() throws ReflectiveOperationException
    {
        Class<?> aClass        = Class.forName(this.fullyQualifiedClassName);
        Field    instanceField = aClass.getField("INSTANCE");
        Object   result        = instanceField.get(null);
        Objects.requireNonNull(result);
        return (DomainModel) result;
    }

    @JsonProperty
    public void setFullyQualifiedClassName(String fullyQualifiedClassName)
    {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }
}
