package cool.klass.model.converter.compiler.state.value;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.AbstractMemberReferencePath.AbstractMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrMemberReferencePath extends AntlrExpressionValue
{
    @Nonnull
    protected final AntlrClass                         classState;
    @Nonnull
    protected final ImmutableList<AntlrAssociationEnd> associationEndStates;
    @Nonnull
    protected final AntlrDataTypeProperty<?>           dataTypePropertyState;

    protected AntlrMemberReferencePath(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClass classState,
            @Nonnull ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
        this.classState = Objects.requireNonNull(classState);
        this.associationEndStates = Objects.requireNonNull(associationEndStates);
        this.dataTypePropertyState = Objects.requireNonNull(dataTypePropertyState);
    }

    @Nonnull
    public AntlrClass getClassState()
    {
        return this.classState;
    }

    @Nonnull
    public AntlrDataTypeProperty<?> getDataTypePropertyState()
    {
        return this.dataTypePropertyState;
    }

    @Nonnull
    @Override
    public abstract AbstractMemberReferencePathBuilder<?> build();

    @Nullable
    protected AntlrClass reportErrorsAssociationEnds(
            @Nonnull CompilerErrorState compilerErrorHolder,
            @Nonnull List<AssociationEndReferenceContext> associationEndReferenceContexts)
    {
        AntlrClass currentClassState = this.classState;
        for (int i = 0; i < this.associationEndStates.size(); i++)
        {
            AntlrAssociationEnd associationEndState = this.associationEndStates.get(i);
            if (associationEndState == AntlrAssociationEnd.NOT_FOUND)
            {
                IdentifierContext identifier = associationEndReferenceContexts.get(i).identifier();
                String message = String.format(
                        "Cannot find member '%s.%s'.",
                        currentClassState.getName(),
                        identifier.getText());
                compilerErrorHolder.add("ERR_MEM_EXP", message, this, identifier);
                return null;
            }
            currentClassState = associationEndState.getType();
        }
        return currentClassState;
    }
}
