package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;

public class AntlrRelationship
        extends AntlrElement
{
    private static final Converter<String, String> LOWER_CAMEL_TO_UPPER_CAMEL =
            CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    private static final Converter<String, String> UPPER_TO_LOWER_CAMEL =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    private final AntlrAssociation association;

    private AntlrCriteria criteria;

    public AntlrRelationship(
            @Nonnull RelationshipContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            AntlrAssociation association)
    {
        super(elementContext, compilationUnit);
        this.association = Objects.requireNonNull(association);
    }

    @Nonnull
    @Override
    public RelationshipContext getElementContext()
    {
        return (RelationshipContext) super.getElementContext();
    }

    public void setCriteria(AntlrCriteria criteria)
    {
        if (this.criteria != null)
        {
            throw new IllegalStateException();
        }
        this.criteria = Objects.requireNonNull(criteria);
    }

    public AntlrCriteria getCriteria()
    {
        return this.criteria;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.association);
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        AntlrAssociationEnd sourceEnd = this.association.getSourceEnd();
        AntlrAssociationEnd targetEnd = this.association.getTargetEnd();

        if (targetEnd.isToOne() && sourceEnd.isToMany() || sourceEnd.isToOne() && sourceEnd.isOwned())
        {
            this.reportInferredEnd(
                    compilerErrorHolder,
                    sourceEnd,
                    AntlrRelationship::getSourceInferredRelationshipText);
        }
        else if (sourceEnd.isToOne() && targetEnd.isToMany() || targetEnd.isToOne() && targetEnd.isOwned())
        {
            this.reportInferredEnd(
                    compilerErrorHolder,
                    targetEnd,
                    AntlrRelationship::getTargetInferredRelationshipText);
        }

        this.criteria.reportErrors(compilerErrorHolder);
    }

    private void reportInferredEnd(
            CompilerErrorState compilerErrorHolder,
            AntlrAssociationEnd associationEnd,
            Function<AntlrAssociationEnd, String> sourceCodeTextFunction)
    {
        if (this.compilationUnit.flatMap(CompilationUnit::getMacroElement).isPresent())
        {
            return;
        }

        CriteriaExpressionContext criteriaExpressionContext = this.getElementContext().criteriaExpression();

        String sourceText             = getSourceText(criteriaExpressionContext);
        String sourceWithoutSpaceText = sourceText.replaceAll("\\s+", "");

        String sourceCodeText = sourceCodeTextFunction.apply(associationEnd);

        if (sourceCodeText.equals(sourceWithoutSpaceText))
        {
            compilerErrorHolder.add(
                    "ERR_REL_INF",
                    "Relationship in association '%s' is inferred and can be removed.".formatted(this.association.getName()),
                    this.criteria,
                    criteriaExpressionContext);
        }
    }

    // relationship this.otherTypeKey == OtherType.key
    private static String getSourceInferredRelationshipText(AntlrAssociationEnd associationEnd)
    {
        AntlrClass oppositeType = associationEnd.getOpposite().getType();

        return oppositeType
                .getKeyProperties()
                .collect(each -> "this.%s%s==%s.%s".formatted(
                        UPPER_TO_LOWER_CAMEL.convert(oppositeType.getName()),
                        LOWER_CAMEL_TO_UPPER_CAMEL.convert(each.getName()),
                        oppositeType.getName(),
                        each.getName()))
                .makeString("&&");
    }

    // relationship this.key == OtherType.thisTypeKey
    private static String getTargetInferredRelationshipText(AntlrAssociationEnd associationEnd)
    {
        AntlrClass oppositeType = associationEnd.getOpposite().getType();

        return oppositeType
                .getKeyProperties()
                .collect(each -> "this.%s==%s.%s%s".formatted(
                        each.getName(),
                        associationEnd.getType().getName(),
                        UPPER_TO_LOWER_CAMEL.convert(oppositeType.getName()),
                        LOWER_CAMEL_TO_UPPER_CAMEL.convert(each.getName())))
                .makeString("&&");
    }
}
