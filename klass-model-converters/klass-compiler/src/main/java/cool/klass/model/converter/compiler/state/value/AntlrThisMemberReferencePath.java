package cool.klass.model.converter.compiler.state.value;

import java.util.List;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
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
    public AntlrThisMemberReferencePath(
            @Nonnull ThisMemberReferencePathContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState,
            IAntlrElement expressionValueOwner)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                classState,
                associationEndStates,
                dataTypePropertyState,
                expressionValueOwner);
    }

    @Nonnull
    @Override
    public ThisMemberReferencePathBuilder build()
    {
        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates.collect(
                AntlrAssociationEnd::getElementBuilder);

        return new ThisMemberReferencePathBuilder(
                this.elementContext,
                this.inferred,
                this.classState.getElementBuilder(),
                associationEndBuilders,
                this.dataTypePropertyState.getPropertyBuilder());
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
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
                    "ERR_THS_MEM: Cannot find member '%s.%s'.",
                    currentClassState.getName(),
                    identifier.getText());
            compilerErrorHolder.add(message, this, identifier);
        }
    }

    @Nonnull
    @Override
    public ThisMemberReferencePathContext getElementContext()
    {
        return (ThisMemberReferencePathContext) super.getElementContext();
    }

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
}
