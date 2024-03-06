package cool.klass.model.reladomo.tree;

public interface ReladomoTreeNodeVisitor
{
    void visitRoot(RootReladomoTreeNode node);

    void visitDataTypeProperty(DataTypePropertyReladomoTreeNode node);

    void visitSuperClass(SuperClassReladomoTreeNode node);

    void visitSubClass(SubClassReladomoTreeNode node);

    void visitReferenceProperty(ReferencePropertyReladomoTreeNode node);

    void visit(ReferenceReladomoTreeNode node);
}
