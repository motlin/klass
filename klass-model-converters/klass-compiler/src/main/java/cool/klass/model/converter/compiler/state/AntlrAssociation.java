package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrAssociation extends AntlrPackageableElement
{
    @Nullable
    public static final AntlrAssociation AMBIGUOUS = new AntlrAssociation(
            new AssociationDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous association",
            null);

    private final MutableList<AntlrAssociationEnd> associationEndStates = Lists.mutable.empty();
    private       AntlrCriteria                    antlrCriteria;

    private AssociationBuilder associationBuilder;

    public AntlrAssociation(
            @Nonnull AssociationDeclarationContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, packageName);
    }

    public MutableList<AntlrAssociationEnd> getAssociationEndStates()
    {
        return this.associationEndStates;
    }

    public void enterAssociationEnd(AntlrAssociationEnd antlrAssociationEnd)
    {
        this.associationEndStates.add(antlrAssociationEnd);
    }

    public void enterRelationship(AntlrCriteria antlrCriteria)
    {
        this.antlrCriteria = antlrCriteria;
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

        sourceAntlrAssociationEnd.setOpposite(targetAntlrAssociationEnd);
        targetAntlrAssociationEnd.setOpposite(sourceAntlrAssociationEnd);

        sourceAntlrAssociationEnd.setOwningClassState(targetAntlrAssociationEnd.getType());
        targetAntlrAssociationEnd.setOwningClassState(sourceAntlrAssociationEnd.getType());

        sourceAntlrAssociationEnd.getType().enterAssociationEnd(targetAntlrAssociationEnd);
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

        CriteriaBuilder criteriaBuilder = this.antlrCriteria.build();

        this.associationBuilder = new AssociationBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
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

    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(this.compilationUnit, message, this.nameContext);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "Association '%s' should have 2 ends. Found %d",
                    this.name,
                    numAssociationEnds);
            compilerErrorHolder.add(this.compilationUnit, message, this.getElementContext().identifier());
        }

        if (this.antlrCriteria == null)
        {
            String message = String.format(
                    "ERR_REL_INF: Relationship inference not yet supported. '%s' must declare a relationship.",
                    this.name);
            compilerErrorHolder.add(this.compilationUnit, message, this.getElementContext().identifier());
        }

        // TODO: Check that both ends aren't owned
    }

    @Nonnull
    @Override
    public AssociationDeclarationContext getElementContext()
    {
        return (AssociationDeclarationContext) this.elementContext;
    }
}
