package cool.klass.model.reladomo.tree;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;

public class RootReladomoTreeNode
        extends AbstractReladomoTreeNode
{
    private final Klass klass;

    public RootReladomoTreeNode(String name, Klass klass)
    {
        super(name);
        this.klass = Objects.requireNonNull(klass);
    }

    @Override
    public void visit(ReladomoTreeNodeVisitor visitor)
    {
        visitor.visitRoot(this);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.klass;
    }

    @Override
    public Classifier getType()
    {
        return this.klass;
    }

    @Override
    public String toString()
    {
        return this.toString(this, "");
    }

    private String toString(
            ReladomoTreeNode node,
            String indent)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indent);
        stringBuilder.append(node.getNodeString());
        stringBuilder.append('\n');

        String childIndent = indent + "  ";
        node.getChildren().forEachValue(child -> stringBuilder.append(this.toString(child, childIndent)));

        return stringBuilder.toString();
    }

    @Override
    public String getShortString()
    {
        return this.getType().getName() + "Finder";
    }

    @Override
    public String getNodeString()
    {
        return this.getShortString() + ": " + this.getType().getName();
    }
}
