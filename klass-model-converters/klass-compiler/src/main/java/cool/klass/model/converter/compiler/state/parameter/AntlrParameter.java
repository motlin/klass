package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrParameter
        extends AntlrNamedElement
        implements AntlrMultiplicityOwner
{
    @Nonnull
    public static final AntlrParameter AMBIGUOUS = new AntlrParameter(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            -1,
            AntlrEnumeration.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS);

    @Nonnull
    public static final AntlrParameter NOT_FOUND = new AntlrParameter(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "not found enumeration url parameter",
            -1,
            AntlrEnumeration.NOT_FOUND,
            AntlrParameterizedProperty.AMBIGUOUS);

    @Nonnull
    private final IAntlrElement parameterOwner;
    @Nonnull
    private final AntlrType     typeState;

    // TODO: Factor modifiers into type checking
    private final MutableList<AntlrParameterModifier> parameterModifiers = Lists.mutable.empty();

    @Nullable
    private AntlrMultiplicity multiplicityState;

    @Nullable
    private ParameterBuilder elementBuilder;

    public AntlrParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrType typeState,
            @Nonnull IAntlrElement parameterOwner)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.typeState      = Objects.requireNonNull(typeState);
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
        return Optional.of(this.parameterOwner);
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

    @Override
    public void enterMultiplicity(@Nonnull AntlrMultiplicity multiplicityState)
    {
        if (this.multiplicityState != null)
        {
            throw new IllegalStateException();
        }
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
    }

    public void enterParameterModifier(AntlrParameterModifier parameterModifierState)
    {
        this.parameterModifiers.add(parameterModifierState);
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportNameErrors(compilerErrorHolder);
        this.reportTypeErrors(compilerErrorHolder);
    }

    private void reportTypeErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.typeState != AntlrEnumeration.NOT_FOUND)
        {
            return;
        }

        EnumerationReferenceContext offendingToken =
                ((EnumerationParameterDeclarationContext) this.getElementContext()).enumerationReference();
        String message = String.format(
                "Cannot find enumeration '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_ENM_PAR", message, this, offendingToken);
    }

    public void reportDuplicateParameterName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate parameter: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_PAR", message, this);
    }

    @Nonnull
    public AntlrType getType()
    {
        return this.typeState;
    }

    @Nonnull
    public ParameterBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ParameterBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                // TODO: Fuller interface hierarchy with AntlrType, AntlrDataType, etc.
                (DataTypeGetter) this.typeState.getElementBuilder(),
                this.multiplicityState.getMultiplicity());
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public ParameterBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
