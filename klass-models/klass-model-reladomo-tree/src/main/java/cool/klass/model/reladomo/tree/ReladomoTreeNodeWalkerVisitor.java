package cool.klass.model.reladomo.tree;

import java.util.Objects;

public class ReladomoTreeNodeWalkerVisitor
        implements ReladomoTreeNodeVisitor
{
    private final ReladomoTreeNodeListener listener;

    public ReladomoTreeNodeWalkerVisitor(ReladomoTreeNodeListener listener)
    {
        this.listener = Objects.requireNonNull(listener);
    }

    @Override
    public void visitRoot(RootReladomoTreeNode node)
    {
        this.listener.enterRoot(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitRoot(node);
    }

    @Override
    public void visitDataTypeProperty(DataTypePropertyReladomoTreeNode node)
    {
        this.listener.enterDataTypeProperty(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitDataTypeProperty(node);
    }

    @Override
    public void visitSuperClass(SuperClassReladomoTreeNode node)
    {
        this.listener.enterSuperClass(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSuperClass(node);
    }

    @Override
    public void visitSubClass(SubClassReladomoTreeNode node)
    {
        this.listener.enterSubClass(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSubClass(node);
    }

    @Override
    public void visitReferenceProperty(ReferencePropertyReladomoTreeNode node)
    {
        this.listener.enterReferenceProperty(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitReferenceProperty(node);
    }

    @Override
    public void visit(ReferenceReladomoTreeNode node)
    {
        this.listener.enterReference(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitReference(node);
    }
}
