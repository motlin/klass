package cool.klass.model.converter.compiler.state.value;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
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
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
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
            @Nonnull CompilerErrorHolder compilerErrorHolder,
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
                        "ERR_MEM_EXP: Cannot find member '%s.%s'.",
                        currentClassState.getName(),
                        identifier.getText());
                compilerErrorHolder.add(message, this, identifier);
                return null;
            }
            currentClassState = associationEndState.getType();
        }
        return currentClassState;
    }
}
