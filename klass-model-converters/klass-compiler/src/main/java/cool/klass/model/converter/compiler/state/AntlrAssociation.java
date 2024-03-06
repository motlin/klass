package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrAssociation
{
    public static final AntlrAssociation AMBIGUOUS = new AntlrAssociation(
            null,
            new AssociationDeclarationContext(null, -1));

    private final String                        packageName;
    private final AssociationDeclarationContext context;

    private final MutableList<AntlrAssociationEnd> associationEndStates = Lists.mutable.empty();
    private final String                           name;

    private AssociationBuilder associationBuilder;

    public AntlrAssociation(String packageName, AssociationDeclarationContext context)
    {
        this.packageName = packageName;
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(context.getText());
    }

    public String getName()
    {
        return this.name;
    }

    public void enterAssociationEnd(AntlrAssociationEnd antlrAssociationEnd)
    {
        this.associationEndStates.add(antlrAssociationEnd);
    }

    public void exitAssociationDeclaration(AbstractCompilerPhase compilerPhase)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "Association '%s' should have 2 ends. Found %d",
                    this.name,
                    numAssociationEnds);
            compilerPhase.error(message, this.context);
            return;
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

        this.associationBuilder = new AssociationBuilder(
                this.context,
                this.context.identifier(),
                this.name,
                this.packageName);

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::build)
                .toImmutable();

        this.associationBuilder.setAssociationEnds(associationEndBuilders);
        return this.associationBuilder;
    }
}
