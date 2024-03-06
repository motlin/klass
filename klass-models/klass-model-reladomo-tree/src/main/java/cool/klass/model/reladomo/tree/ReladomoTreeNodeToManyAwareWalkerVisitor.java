package cool.klass.model.reladomo.tree;

import java.util.Objects;
import java.util.Optional;

public class ReladomoTreeNodeToManyAwareWalkerVisitor
        implements ReladomoTreeNodeVisitor
{
    private final ReladomoTreeNodeToManyAwareListener listener;

    public ReladomoTreeNodeToManyAwareWalkerVisitor(ReladomoTreeNodeToManyAwareListener listener)
    {
        this.listener = Objects.requireNonNull(listener);
    }

    @Override
    public void visitRoot(RootReladomoTreeNode node)
    {
        Optional<Integer> maybeNumChildren = this.listener.enterRoot(node);
        this.listener.assertInvariants();
        this.visitChildren(node, maybeNumChildren);
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
        Optional<Integer> maybeNumChildren = this.listener.enterReferenceProperty(node);
        this.listener.assertInvariants();
        this.visitChildren(node, maybeNumChildren);
        this.listener.exitReferenceProperty(node);
        this.listener.assertInvariants();
    }

    @Override
    public void visit(ReferenceReladomoTreeNode node)
    {
        Optional<Integer> maybeNumChildren = this.listener.enterReference(node);
        this.listener.assertInvariants();
        this.visitChildren(node, maybeNumChildren);
        this.listener.exitReference(node);
        this.listener.assertInvariants();
    }

    private void visitChildren(ReladomoTreeNode node, Optional<Integer> maybeNumChildren)
    {
        if (maybeNumChildren.isEmpty())
        {
            node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
            return;
        }

        Integer numChildren = maybeNumChildren.get();
        for (int i = 0; i < numChildren; i++)
        {
            this.listener.enterListIndex(i);
            node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
            this.listener.exitListIndex(i);
        }
    }
}
