package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.service.AntlrCriteriaOwner;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociation extends AntlrPackageableElement implements AntlrCriteriaOwner
{
    @Nonnull
    public static final AntlrAssociation AMBIGUOUS = new AntlrAssociation(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous association",
            -1,
            null)
    {
        @Override
        public void enterAssociationEnd(AntlrAssociationEnd associationEndState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };

    private final MutableList<AntlrAssociationEnd>                              associationEndStates     = Lists.mutable.empty();
    private final MutableOrderedMap<AssociationEndContext, AntlrAssociationEnd> associationEndsByContext = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private AntlrCriteria criteriaState;

    private AssociationBuilder associationBuilder;
    private AntlrClass         versionClass;

    public AntlrAssociation(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, packageName);
    }

    public MutableList<AntlrAssociationEnd> getAssociationEndStates()
    {
        return this.associationEndStates.asUnmodifiable();
    }

    public int getNumAssociationEnds()
    {
        return this.associationEndStates.size();
    }

    public AntlrAssociationEnd getAssociationEndByContext(AssociationEndContext ctx)
    {
        return this.associationEndsByContext.get(ctx);
    }

    public void enterAssociationEnd(AntlrAssociationEnd associationEndState)
    {
        AntlrAssociationEnd duplicate = this.associationEndsByContext.put(
                associationEndState.getElementContext(),
                associationEndState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }

        this.associationEndStates.add(associationEndState);
    }

    public void exitAssociationDeclaration()
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            throw new AssertionError(numAssociationEnds);
        }

        AntlrAssociationEnd sourceAntlrAssociationEnd = this.associationEndStates.get(0);
        AntlrAssociationEnd targetAntlrAssociationEnd = this.associationEndStates.get(1);

        AntlrClass sourceType = sourceAntlrAssociationEnd.getType();
        AntlrClass targetType = targetAntlrAssociationEnd.getType();

        if (sourceType == AntlrClass.NOT_FOUND
                || targetType == AntlrClass.NOT_FOUND
                || sourceType == AntlrClass.AMBIGUOUS
                || targetType == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        sourceAntlrAssociationEnd.setOpposite(targetAntlrAssociationEnd);
        targetAntlrAssociationEnd.setOpposite(sourceAntlrAssociationEnd);

        sourceAntlrAssociationEnd.setOwningClassState(targetType);
        targetAntlrAssociationEnd.setOwningClassState(sourceType);

        sourceType.enterAssociationEnd(targetAntlrAssociationEnd);
        targetAntlrAssociationEnd.getType().enterAssociationEnd(sourceAntlrAssociationEnd);
    }

    public AssociationBuilder build()
    {
        if (this.associationBuilder != null)
        {
            throw new IllegalStateException();
        }

        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            throw new AssertionError(numAssociationEnds);
        }

        CriteriaBuilder criteriaBuilder = this.criteriaState.build();

        this.associationBuilder = new AssociationBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName,
                criteriaBuilder);

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::build)
                .toImmutable();

        this.associationBuilder.setAssociationEndBuilders(associationEndBuilders);
        return this.associationBuilder;
    }

    public AssociationBuilder getAssociationBuilder()
    {
        return this.associationBuilder;
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: ⬇ Potentially refine a smaller list of keywords that clash with associations and a separate name pattern

        this.reportKeywordCollision(compilerErrorHolder);

        if (!TYPE_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_ASO_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext);
        }
    }

    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(message, this.nameContext);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "ERR_ASO_END: Association '%s' should have 2 ends. Found %d",
                    this.name,
                    numAssociationEnds);
            compilerErrorHolder.add(message, this.getElementContext().identifier());
        }

        // TODO: reportErrors: Check that both ends aren't owned

        AntlrAssociationEnd sourceAntlrAssociationEnd = this.associationEndStates.get(0);
        AntlrAssociationEnd targetAntlrAssociationEnd = this.associationEndStates.get(1);

        AntlrClass sourceType = sourceAntlrAssociationEnd.getType();
        AntlrClass targetType = targetAntlrAssociationEnd.getType();

        if (sourceType == AntlrClass.NOT_FOUND || targetType == AntlrClass.NOT_FOUND)
        {
            this.reportTypeNotFound(compilerErrorHolder, sourceAntlrAssociationEnd, sourceType);
            this.reportTypeNotFound(compilerErrorHolder, targetAntlrAssociationEnd, targetType);

            return;
        }

        if (this.criteriaState == null)
        {
            // TODO: Editor error matching this one
            String message = String.format(
                    "ERR_REL_INF: Relationship inference not yet supported. '%s' must declare a relationship.",
                    this.name);
            compilerErrorHolder.add(message, this.getElementContext().identifier());
        }
        else
        {
            this.criteriaState.reportErrors(compilerErrorHolder, this.getParserRuleContexts());
        }
    }

    @Nonnull
    @Override
    public AssociationDeclarationContext getElementContext()
    {
        return (AssociationDeclarationContext) this.elementContext;
    }

    private void reportTypeNotFound(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull AntlrAssociationEnd antlrAssociationEnd, AntlrClass type)
    {
        if (type == AntlrClass.NOT_FOUND)
        {
            ClassReferenceContext offendingToken = antlrAssociationEnd.getElementContext().classType().classReference();
            String message = String.format(
                    "ERR_ASO_TYP: Cannot find class '%s'.",
                    offendingToken.getText());
            compilerErrorHolder.add(
                    message,
                    offendingToken,
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }
    }

    @Nonnull
    @Override
    public AntlrCriteria getCriteria()
    {
        return this.criteriaState;
    }

    @Override
    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        this.criteriaState = Objects.requireNonNull(criteria);
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        parserRuleContexts.add(this.compilationUnit.getParserContext());
    }

    public void setVersionClass(AntlrClass classState)
    {
        if (this.versionClass != null)
        {
            throw new AssertionError();
        }
        this.versionClass = Objects.requireNonNull(classState);
    }

    public void reportDuplicateVersionAssociation(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull AntlrClass versionClass)
    {
        String message = String.format(
                "ERR_MUL_VER_ASSO: Multiple version associations on '%s'.",
                versionClass.getName());
        compilerErrorHolder.add(message, this.getElementContext().versions().classReference());
    }

    public void reportVersionAssociation(@Nonnull CompilerErrorHolder compilerErrorHolder, AntlrClass antlrClass)
    {
        // TODO: Check for the class they probably meant to point to
        String message = String.format(
                "ERR_VER_ASSO: Version association points to class which isn't a version '%s'.",
                this.versionClass.getName());
        compilerErrorHolder.add(message, this.getElementContext().versions().classReference());
    }
}
