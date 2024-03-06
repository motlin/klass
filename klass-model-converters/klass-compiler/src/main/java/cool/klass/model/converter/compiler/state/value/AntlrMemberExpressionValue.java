package cool.klass.model.converter.compiler.state.value;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.MemberReferencePath.MemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrMemberExpressionValue extends AntlrExpressionValue
{
    @Nonnull
    protected final AntlrClass                         classState;
    @Nonnull
    protected final ImmutableList<AntlrAssociationEnd> associationEndStates;
    @Nonnull
    protected final AntlrDataTypeProperty<?>           dataTypePropertyState;

    public AntlrMemberExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            ImmutableList<AntlrAssociationEnd> associationEndStates,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred);
        this.classState = Objects.requireNonNull(classState);
        this.associationEndStates = Objects.requireNonNull(associationEndStates);
        this.dataTypePropertyState = Objects.requireNonNull(dataTypePropertyState);
    }

    @Nonnull
    @Override
    public abstract MemberReferencePathBuilder build();

    @Nullable
    protected AntlrClass reportErrorsAssociationEnds(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts,
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
                compilerErrorHolder.add(
                        message,
                        identifier,
                        parserRuleContexts.toArray(new ParserRuleContext[]{}));
                return null;
            }
            currentClassState = associationEndState.getType();
        }
        return currentClassState;
    }
}
