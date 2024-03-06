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
        Object            before           = this.listener.getStateToAssertInvariants();
        Optional<Integer> maybeNumChildren = this.listener.enterRoot(node);
        this.visitChildren(node, maybeNumChildren);
        this.listener.exitRoot(node);
        Object after = this.listener.getStateToAssertInvariants();

        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
    }

    @Override
    public void visitDataTypeProperty(DataTypePropertyReladomoTreeNode node)
    {
        Object before = this.listener.getStateToAssertInvariants();
        this.listener.enterDataTypeProperty(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitDataTypeProperty(node);
        Object after = this.listener.getStateToAssertInvariants();

        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
    }

    @Override
    public void visitSuperClass(SuperClassReladomoTreeNode node)
    {
        Object before = this.listener.getStateToAssertInvariants();
        this.listener.enterSuperClass(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSuperClass(node);
        Object after = this.listener.getStateToAssertInvariants();

        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
    }

    @Override
    public void visitSubClass(SubClassReladomoTreeNode node)
    {
        Object before = this.listener.getStateToAssertInvariants();
        this.listener.enterSubClass(node);
        node.getChildren().forEachKeyValue((name, child) -> child.visit(this));
        this.listener.exitSubClass(node);
        Object after = this.listener.getStateToAssertInvariants();

        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
    }

    @Override
    public void visitReferenceProperty(ReferencePropertyReladomoTreeNode node)
    {
        Object            before           = this.listener.getStateToAssertInvariants();
        Optional<Integer> maybeNumChildren = this.listener.enterReferenceProperty(node);
        this.visitChildren(node, maybeNumChildren);
        this.listener.exitReferenceProperty(node);
        Object after = this.listener.getStateToAssertInvariants();

        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
    }

    @Override
    public void visit(ReferenceReladomoTreeNode node)
    {
        Object            before           = this.listener.getStateToAssertInvariants();
        Optional<Integer> maybeNumChildren = this.listener.enterReference(node);
        this.visitChildren(node, maybeNumChildren);
        this.listener.exitReference(node);
        Object after = this.listener.getStateToAssertInvariants();
        if (!Objects.equals(before, after))
        {
            throw new IllegalStateException("State changed during visit");
        }
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
