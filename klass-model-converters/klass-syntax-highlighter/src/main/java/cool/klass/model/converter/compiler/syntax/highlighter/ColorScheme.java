package cool.klass.model.converter.compiler.syntax.highlighter;

public interface ColorScheme
{
    Color getBackground();

    Color getBlockComment();

    default Color getLineComment()
    {
        return this.getBlockComment();
    }

    Color getKeyword();

    default Color getPackageKeyword()
    {
        return this.getKeyword();
    }

    default Color getPrimitiveType()
    {
        return this.getKeyword();
    }

    default Color getVerb()
    {
        return this.getKeyword();
    }

    default Color getModifier()
    {
        return this.getKeyword();
    }

    default Color getClassifierModifier()
    {
        return this.getModifier();
    }

    default Color getDataTypePropertyModifier()
    {
        return this.getModifier();
    }

    default Color getAssociationEndModifier()
    {
        return this.getModifier();
    }

    default Color getParameterizedPropertyModifier()
    {
        return this.getModifier();
    }

    default Color getParameterModifier()
    {
        return this.getModifier();
    }

    default Color getValidationModifier()
    {
        return this.getModifier();
    }

    default Color getServiceCategoryModifier()
    {
        return this.getModifier();
    }

    Color getIdentifier();

    default Color getPackageName()
    {
        return this.getIdentifier();
    }

    default Color getTopLevelElementName()
    {
        return this.getIdentifier();
    }

    default Color getEnumerationName()
    {
        return this.getTopLevelElementName();
    }

    default Color getClassifierName()
    {
        return this.getTopLevelElementName();
    }

    default Color getClassName()
    {
        return this.getClassifierName();
    }

    default Color getInterfaceName()
    {
        return this.getClassifierName();
    }

    default Color getAssociationName()
    {
        return this.getTopLevelElementName();
    }

    default Color getProjectionName()
    {
        return this.getTopLevelElementName();
    }

    default Color getEnumerationLiteralName()
    {
        return this.getIdentifier();
    }

    default Color getParameterName()
    {
        return this.getIdentifier();
    }

    default Color getPropertyName()
    {
        return this.getIdentifier();
    }

    default Color getDataTypePropertyName()
    {
        return this.getPropertyName();
    }

    default Color getPrimitivePropertyName()
    {
        return this.getDataTypePropertyName();
    }

    default Color getEnumerationPropertyName()
    {
        return this.getDataTypePropertyName();
    }

    default Color getReferencePropertyName()
    {
        return this.getPropertyName();
    }

    default Color getParameterizedPropertyName()
    {
        return this.getReferencePropertyName();
    }

    default Color getAssociationEndName()
    {
        return this.getReferencePropertyName();
    }

    default Color getEnumerationReference()
    {
        return this.getEnumerationName();
    }

    default Color getInterfaceReference()
    {
        return this.getInterfaceName();
    }

    default Color getClassReference()
    {
        return this.getClassName();
    }

    default Color getProjectionReference()
    {
        return this.getProjectionName();
    }

    default Color getDataTypePropertyReference()
    {
        return this.getPropertyName();
    }

    default Color getAssociationEndReference()
    {
        return this.getAssociationEndName();
    }

    default Color getParameterizedPropertyReference()
    {
        return this.getParameterizedPropertyName();
    }

    default Color getPropertyReference()
    {
        return this.getPropertyName();
    }

    default Color getParameterReference()
    {
        return this.getParameterName();
    }

    Color getLiteral();

    default Color getStringLiteral()
    {
        return this.getLiteral();
    }

    default Color getIntegerLiteral()
    {
        return this.getLiteral();
    }

    default Color getBooleanLiteral()
    {
        return this.getLiteral();
    }

    default Color getCharacterLiteral()
    {
        return this.getLiteral();
    }

    default Color getFloatingPointLiteral()
    {
        return this.getLiteral();
    }

    Color getPunctuation();

    default Color getComma()
    {
        return this.getPunctuation();
    }

    default Color getDot()
    {
        return this.getPunctuation();
    }

    default Color getDotDot()
    {
        return this.getDot();
    }

    default Color getSemi()
    {
        return this.getPunctuation();
    }

    default Color getOperator()
    {
        return this.getPunctuation();
    }
}
