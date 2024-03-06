package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrParameter extends AntlrNamedElement
{
    @Nonnull
    public static final AntlrParameter AMBIGUOUS = new AntlrParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            -1,
            AntlrEnumeration.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS);
    @Nonnull
    public static final AntlrParameter NOT_FOUND = new AntlrParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration url parameter",
            -1,
            AntlrEnumeration.NOT_FOUND,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS);

    @Nonnull
    private final AntlrMultiplicity multiplicityState;
    @Nonnull
    private final IAntlrElement     parameterOwner;
    @Nonnull
    private final AntlrType         typeState;

    // TODO: Factor modifiers into type checking
    private final MutableList<AntlrParameterModifier> parameterModifiers = Lists.mutable.empty();

    private ParameterBuilder parameterBuilder;

    public AntlrParameter(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrType typeState,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull IAntlrElement parameterOwner)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.typeState = Objects.requireNonNull(typeState);
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
        this.parameterOwner = Objects.requireNonNull(parameterOwner);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of((IAntlrElement) this.parameterOwner);
    }

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }

    public int getNumModifiers()
    {
        return this.parameterModifiers.size();
    }

    public void enterParameterModifier(AntlrParameterModifier parameterModifierState)
    {
        this.parameterModifiers.add(parameterModifierState);
    }

    public void reportDuplicateParameterName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PAR: Duplicate parameter: '%s'.", this.getName());
        compilerErrorHolder.add(message, this);
    }

    @Nonnull
    public AntlrType getType()
    {
        return this.typeState;
    }

    @Nonnull
    public ParameterBuilder build()
    {
        if (this.parameterBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.parameterBuilder = new ParameterBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                // TODO: Fuller interface hierarchy with AntlrType, AntlrDataType, etc.
                (DataTypeGetter) this.typeState.getElementBuilder(),
                this.multiplicityState.getMultiplicity());
        return this.parameterBuilder;
    }

    @Nonnull
    public ParameterBuilder getElementBuilder()
    {
        return this.parameterBuilder;
    }
}
