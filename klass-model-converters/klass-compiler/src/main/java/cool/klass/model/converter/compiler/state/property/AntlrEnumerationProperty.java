package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.property.EnumerationPropertyImpl.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrEnumerationProperty
        extends AntlrDataTypeProperty<EnumerationImpl>
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrEnumerationProperty AMBIGUOUS = new AntlrEnumerationProperty(
            new EnumerationPropertyContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClassifier.AMBIGUOUS,
            false,
            AntlrEnumeration.AMBIGUOUS)
    {
        @Override
        public String toString()
        {
            return "AntlrEnumerationProperty.AMBIGUOUS";
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrEnumerationProperty NOT_FOUND = new AntlrEnumerationProperty(
            new EnumerationPropertyContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClassifier.NOT_FOUND,
            false,
            AntlrEnumeration.NOT_FOUND)
    {
        @Override
        public String toString()
        {
            return "AntlrEnumerationProperty.NOT_FOUND";
        }
    };
    //</editor-fold>

    // TODO: Check that it's not NOT_FOUND
    @Nonnull
    private final AntlrEnumeration enumerationState;

    private EnumerationPropertyBuilder elementBuilder;

    public AntlrEnumerationProperty(
            @Nonnull EnumerationPropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional,
            @Nonnull AntlrEnumeration enumerationState)
    {
        super(
                elementContext,
                compilationUnit,
                ordinal,
                nameContext,
                owningClassifierState,
                isOptional);
        this.enumerationState = Objects.requireNonNull(enumerationState);
    }

    @Nonnull
    @Override
    public AntlrEnumeration getType()
    {
        return this.enumerationState;
    }

    @Override
    protected ParserRuleContext getTypeParserRuleContext()
    {
        return this.getElementContext().enumerationReference();
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.elementBuilder = new EnumerationPropertyBuilder(
                (EnumerationPropertyContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.enumerationState.getElementBuilder(),
                this.owningClassifierState.getElementBuilder(),
                this.isOptional);

        ImmutableList<ModifierBuilder> modifierBuilders = this.getModifiers()
                .collect(AntlrModifier::build)
                .toImmutable();
        this.elementBuilder.setModifierBuilders(modifierBuilders);

        this.buildValidations();

        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public String getTypeName()
    {
        return this.getElementContext().enumerationReference().getText();
    }

    //<editor-fold desc="Report Compiler Errors">
    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        super.reportErrors(compilerAnnotationHolder);

        this.reportTypeNotFound(compilerAnnotationHolder);
        this.reportForwardReference(compilerAnnotationHolder);
    }

    private void reportTypeNotFound(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.enumerationState != AntlrEnumeration.NOT_FOUND)
        {
            return;
        }

        EnumerationReferenceContext offendingToken = this.getElementContext().enumerationReference();
        String message = String.format(
                "Cannot find enumeration '%s'.",
                offendingToken.getText());
        compilerAnnotationHolder.add("ERR_ENM_PRP", message, this, offendingToken);
    }

    private void reportForwardReference(CompilerAnnotationState compilerAnnotationHolder)
    {
        if (!this.isForwardReference(this.enumerationState))
        {
            return;
        }

        String message = String.format(
                "Enumeration property '%s' is declared on line %d and has a forward reference to enumeration '%s' which is declared later in the source file '%s' on line %d.",
                this,
                this.getElementContext().getStart().getLine(),
                this.enumerationState.getName(),
                this.getCompilationUnit().get().getSourceName(),
                this.enumerationState.getElementContext().getStart().getLine());
        compilerAnnotationHolder.add(
                "ERR_FWD_REF",
                message,
                this,
                this.getElementContext().enumerationReference());
    }

    @Override
    protected void reportInvalidIdProperties(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        for (AntlrModifier idModifier : idModifiers)
        {
            String message = "Enumeration properties may not be auto-generated ids.";
            compilerAnnotationHolder.add("ERR_ENM_IDP", message, this, idModifier.getElementContext());
        }
    }
    //</editor-fold>

    @Nonnull
    @Override
    public EnumerationPropertyContext getElementContext()
    {
        return (EnumerationPropertyContext) super.getElementContext();
    }
}
