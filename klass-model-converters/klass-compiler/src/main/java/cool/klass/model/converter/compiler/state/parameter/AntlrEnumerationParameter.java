package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.parameter.EnumerationParameter.EnumerationParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationParameter extends AntlrParameter
{
    @Nonnull
    public static final AntlrEnumerationParameter AMBIGUOUS = new AntlrEnumerationParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            -1,
            AntlrEnumeration.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    public static final AntlrEnumerationParameter NOT_FOUND = new AntlrEnumerationParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration url parameter",
            -1,
            AntlrEnumeration.NOT_FOUND,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrEnumeration            enumerationState;
    private       EnumerationParameterBuilder enumerationParameterBuilder;

    public AntlrEnumerationParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrEnumeration enumerationState,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrParameterOwner parameterOwner,
            @Nonnull ImmutableList<AntlrParameterModifier> parameterModifiers)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                multiplicityState,
                parameterOwner,
                parameterModifiers);
        this.enumerationState = Objects.requireNonNull(enumerationState);
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.enumerationState;
    }

    @Nonnull
    @Override
    public EnumerationParameterBuilder build()
    {
        if (this.enumerationParameterBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.enumerationParameterBuilder = new EnumerationParameterBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.multiplicityState.getMultiplicity(),
                this.enumerationState.getEnumerationBuilder());
        return this.enumerationParameterBuilder;
    }
}
