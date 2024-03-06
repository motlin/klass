package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrIdentifierElement;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;

// TODO: Specific subclasses for the specific antlr context types
public final class AntlrParameter
        extends AntlrIdentifierElement
        implements AntlrMultiplicityOwner
{
    public static final AntlrParameter AMBIGUOUS = new AntlrParameter(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrEnumeration.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS);

    public static final AntlrParameter NOT_FOUND = new AntlrParameter(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrEnumeration.NOT_FOUND,
            AntlrParameterizedProperty.AMBIGUOUS);

    @Nonnull
    private final IAntlrElement parameterOwner;
    @Nonnull
    private final AntlrType     typeState;

    // TODO: Factor modifiers into type checking
    private final MutableList<AntlrModifier> modifiers = Lists.mutable.empty();

    @Nullable
    private AntlrMultiplicity multiplicityState;

    @Nullable
    private ParameterBuilder elementBuilder;

    public AntlrParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrType typeState,
            @Nonnull IAntlrElement parameterOwner)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.typeState      = Objects.requireNonNull(typeState);
        this.parameterOwner = Objects.requireNonNull(parameterOwner);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.parameterOwner);
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }

    public int getNumModifiers()
    {
        return this.modifiers.size();
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

    public void enterModifier(AntlrModifier modifierState)
    {
        this.modifiers.add(modifierState);
    }

    //<editor-fold desc="Report Compiler Errors">
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportNameErrors(compilerAnnotationHolder);
        this.reportTypeErrors(compilerAnnotationHolder);
    }

    private void reportTypeErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
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
        compilerAnnotationHolder.add("ERR_ENM_PAR", message, this, offendingToken);
    }

    public void reportDuplicateParameterName(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format("Duplicate parameter: '%s'.", this.getName());
        compilerAnnotationHolder.add("ERR_DUP_PAR", message, this);
    }
    //</editor-fold>

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
                this.ordinal,
                this.getNameContext(),
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
