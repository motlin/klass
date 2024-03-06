package cool.klass.model.meta.domain.reference;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.AssociationEndWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.source.value.ThisMemberReferencePathWithSourceCode;
import cool.klass.model.meta.domain.api.source.value.TypeMemberReferencePathWithSourceCode;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.BooleanLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class DomainModelReferencesExpressionValueVisitor
        implements ExpressionValueVisitor
{
    private final DomainModelReferences domainModelReferences;

    public DomainModelReferencesExpressionValueVisitor(@Nonnull DomainModelReferences domainModelReferences)
    {
        this.domainModelReferences = Objects.requireNonNull(domainModelReferences);
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        TypeMemberReferencePathWithSourceCode elementWithSourceCode = (TypeMemberReferencePathWithSourceCode) typeMemberExpressionValue;
        TypeMemberReferencePathContext        elementContext        = elementWithSourceCode.getElementContext();

        ClassReferenceContext classReference = elementContext.classReference();
        KlassWithSourceCode   klass          = elementWithSourceCode.getKlass();
        this.domainModelReferences.addClassReference(classReference, klass);

        List<AssociationEndReferenceContext> associationEndReferenceContexts = elementContext.associationEndReference();
        ImmutableList<AssociationEnd>        associationEnds                 = typeMemberExpressionValue.getAssociationEnds();

        for (int i = 0; i < associationEndReferenceContexts.size(); i++)
        {
            AssociationEndReferenceContext associationEndReference = associationEndReferenceContexts.get(i);
            AssociationEnd                 associationEnd          = associationEnds.get(i);
            this.domainModelReferences.addAssociationEndReference(
                    associationEndReference,
                    (AssociationEndWithSourceCode) associationEnd);
        }

        IdentifierContext              propertyReference = elementContext.memberReference().identifier();
        DataTypePropertyWithSourceCode property          = elementWithSourceCode.getProperty();
        this.domainModelReferences.addDataTypePropertyReference(propertyReference, property);
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        ThisMemberReferencePathWithSourceCode elementWithSourceCode = (ThisMemberReferencePathWithSourceCode) thisMemberExpressionValue;
        ThisMemberReferencePathContext        elementContext        = elementWithSourceCode.getElementContext();

        List<AssociationEndReferenceContext> associationEndReferenceContexts = elementContext.associationEndReference();
        ImmutableList<AssociationEnd>        associationEnds                 = thisMemberExpressionValue.getAssociationEnds();

        for (int i = 0; i < associationEndReferenceContexts.size(); i++)
        {
            AssociationEndReferenceContext associationEndReference = associationEndReferenceContexts.get(i);
            AssociationEnd                 associationEnd          = associationEnds.get(i);
            this.domainModelReferences.addAssociationEndReference(
                    associationEndReference,
                    (AssociationEndWithSourceCode) associationEnd);
        }

        IdentifierContext              propertyReference = elementContext.memberReference().identifier();
        DataTypePropertyWithSourceCode property          = elementWithSourceCode.getProperty();
        this.domainModelReferences.addDataTypePropertyReference(propertyReference, property);
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        // TODO: Implement more references
    }

    @Override
    public void visitBooleanLiteral(@Nonnull BooleanLiteralValue booleanLiteralValue)
    {
        // Deliberately empty
    }

    @Override
    public void visitIntegerLiteral(@Nonnull IntegerLiteralValue integerLiteralValue)
    {
        // Deliberately empty
    }

    @Override
    public void visitStringLiteral(@Nonnull StringLiteralValue stringLiteralValue)
    {
        // Deliberately empty
    }

    @Override
    public void visitLiteralList(@Nonnull LiteralListValue literalListValue)
    {
        // Deliberately empty
    }

    @Override
    public void visitUserLiteral(@Nonnull UserLiteral userLiteral)
    {
        ElementWithSourceCode elementWithSourceCode = (ElementWithSourceCode) userLiteral;
        ParserRuleContext     reference             = elementWithSourceCode.getElementContext();
        KlassWithSourceCode   element               = (KlassWithSourceCode) userLiteral.getUserClass();
        this.domainModelReferences.addUserReference(reference, element);
    }

    @Override
    public void visitNullLiteral(@Nonnull NullLiteral nullLiteral)
    {
        // Deliberately empty
    }
}
