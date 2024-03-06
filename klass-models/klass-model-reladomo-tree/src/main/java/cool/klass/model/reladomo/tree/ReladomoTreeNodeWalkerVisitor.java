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
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitRoot(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visitDataTypeProperty(DataTypePropertyReladomoTreeNode node)
    {
        this.listener.enterDataTypeProperty(node);
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitDataTypeProperty(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visitSuperClass(SuperClassReladomoTreeNode node)
    {
        this.listener.enterSuperClass(node);
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSuperClass(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visitSubClass(SubClassReladomoTreeNode node)
    {
        this.listener.enterSubClass(node);
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSubClass(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visitReferenceProperty(ReferencePropertyReladomoTreeNode node)
    {
        this.listener.enterReferenceProperty(node);
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitReferenceProperty(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visit(ReferenceReladomoTreeNode node)
    {
        this.listener.enterReference(node);
        this.listener.assertInvariants();
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitReference(node);
        this.listener.assertInvariants();
    }
}
