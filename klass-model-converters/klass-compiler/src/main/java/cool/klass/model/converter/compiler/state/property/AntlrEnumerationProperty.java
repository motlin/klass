package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.property.EnumerationPropertyImpl.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<EnumerationImpl>
{
    @Nonnull
    public static final AntlrEnumerationProperty NOT_FOUND = new AntlrEnumerationProperty(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            -1,
            AntlrClassifier.NOT_FOUND,
            false,
            AntlrEnumeration.NOT_FOUND);

    // TODO: Check that it's not NOT_FOUND
    @Nonnull
    private final AntlrEnumeration enumerationState;

    private EnumerationPropertyBuilder elementBuilder;

    public AntlrEnumerationProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional,
            @Nonnull AntlrEnumeration enumerationState)
    {
        super(
                elementContext,
                compilationUnit,
                nameContext,
                ordinal,
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

    @Override
    public boolean isTemporal()
    {
        return false;
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
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.ordinal,
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
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportTypeNotFound(compilerErrorHolder);
    }

    private void reportTypeNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.enumerationState != AntlrEnumeration.NOT_FOUND)
        {
            return;
        }

        EnumerationReferenceContext offendingToken = this.getElementContext().enumerationReference();
        String message = String.format(
                "Cannot find enumeration '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_ENM_PRP", message, this, offendingToken);
    }

    @Override
    protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ListIterable<AntlrModifier> idModifiers = this.getModifiers().select(AntlrModifier::isId);
        for (AntlrModifier idModifier : idModifiers)
        {
            ParserRuleContext offendingToken = idModifier.getElementContext();
            String            message        = "Enumeration properties may not be auto-generated ids.";
            compilerErrorHolder.add("ERR_ENM_IDP", message, this, offendingToken);
        }
    }

    @Nonnull
    @Override
    public EnumerationPropertyContext getElementContext()
    {
        return (EnumerationPropertyContext) super.getElementContext();
    }
}
