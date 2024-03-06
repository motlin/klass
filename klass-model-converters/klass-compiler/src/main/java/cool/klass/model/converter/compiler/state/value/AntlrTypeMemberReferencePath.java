package cool.klass.model.converter.compiler.state.value;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.meta.domain.value.TypeMemberReferencePathImpl.TypeMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrTypeMemberReferencePath extends AntlrMemberReferencePath
{
    private TypeMemberReferencePathBuilder elementBuilder;

    public AntlrTypeMemberReferencePath(
            @Nonnull TypeMemberReferencePathContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull AntlrClass classState,
            ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState,
            IAntlrElement expressionValueOwner)
    {
        super(
                elementContext,
                compilationUnit,
                macroElement,
                classState,
                associationEndStates,
                dataTypePropertyState,
                expressionValueOwner);
    }

    @Nonnull
    @Override
    public TypeMemberReferencePathBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new TypeMemberReferencePathBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.classState.getElementBuilder(),
                this.associationEndStates.collect(AntlrAssociationEnd::getElementBuilder),
                this.dataTypePropertyState.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public TypeMemberReferencePathBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.classState == AntlrClass.AMBIGUOUS || this.classState == AntlrClass.NOT_FOUND)
        {
            ClassReferenceContext offendingToken = this.getElementContext().classReference();

            // TODO: This error message is firing for ambiguity, not just NOT_FOUND.
            String message = String.format(
                    "Cannot find class '%s'.",
                    offendingToken.getText());

            compilerErrorHolder.add("ERR_MEM_TYP", message, this, offendingToken);
            return;
        }

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
            compilerErrorHolder.add("ERR_TYP_MEM", message, this, identifier);
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
    public TypeMemberReferencePathContext getElementContext()
    {
        return (TypeMemberReferencePathContext) super.getElementContext();
    }
}
