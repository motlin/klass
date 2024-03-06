package cool.klass.model.converter.compiler.state.value;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrThisMemberReferencePath extends AntlrMemberReferencePath
{
    private ThisMemberReferencePathBuilder elementBuilder;

    public AntlrThisMemberReferencePath(
            @Nonnull ThisMemberReferencePathContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClass classState,
            @Nonnull ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(
                elementContext,
                compilationUnit,
                classState,
                associationEndStates,
                dataTypePropertyState,
                expressionValueOwner);
    }

    @Nonnull
    @Override
    public ThisMemberReferencePathBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::getElementBuilder);

        this.elementBuilder = new ThisMemberReferencePathBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.classState.getElementBuilder(),
                associationEndBuilders,
                this.dataTypePropertyState.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public ThisMemberReferencePathBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        List<AssociationEndReferenceContext> associationEndReferenceContexts = this.getElementContext().associationEndReference();
        AntlrClass currentClassState = this.reportErrorsAssociationEnds(
                compilerErrorHolder,
                associationEndReferenceContexts);
        if (currentClassState == null)
        {
            return;
        }

        if (this.dataTypePropertyState == AntlrEnumerationProperty.NOT_FOUND)
        {
            IdentifierContext identifier = this.getElementContext().memberReference().identifier();
            String message = String.format(
                    "Cannot find member '%s.%s'.",
                    currentClassState.getName(),
                    identifier.getText());
            compilerErrorHolder.add("ERR_THS_MEM", message, this, identifier);
        }
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        AntlrType type = this.dataTypePropertyState.getType();
        if (type == AntlrEnumeration.NOT_FOUND)
        {
            return Lists.immutable.empty();
        }
        return Lists.immutable.with(type);
    }

    @Nonnull
    @Override
    public ThisMemberReferencePathContext getElementContext()
    {
        return (ThisMemberReferencePathContext) super.getElementContext();
    }
}
