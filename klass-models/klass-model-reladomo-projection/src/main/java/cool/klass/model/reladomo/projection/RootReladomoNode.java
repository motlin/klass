package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;

public class RootReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final Klass klass;

    public RootReladomoNode(String name, Klass klass)
    {
        super(name);
        this.klass = Objects.requireNonNull(klass);
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
            ProjectionElementReladomoNode node,
            String indent)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indent);
        stringBuilder.append(node.getNodeString());
        stringBuilder.append('\n');

        String childIndent = indent + "  ";
        for (ProjectionElementReladomoNode child : node.getChildren().values())
        {
            stringBuilder.append(this.toString(child, childIndent));
        }

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
