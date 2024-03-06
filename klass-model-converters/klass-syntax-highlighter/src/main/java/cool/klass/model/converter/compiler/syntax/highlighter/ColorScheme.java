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

    Color getIdentifier();

    default Color getPackageName()
    {
        return this.getIdentifier();
    }

    default Color getTypeName()
    {
        return this.getIdentifier();
    }

    default Color getClassName()
    {
        return this.getTypeName();
    }

    default Color getEnumerationName()
    {
        return this.getTypeName();
    }

    default Color getInterfaceName()
    {
        return this.getTypeName();
    }

    default Color getProjectionName()
    {
        return this.getTypeName();
    }

    default Color getAssociationName()
    {
        return this.getTypeName();
    }

    default Color getEnumerationLiteralName()
    {
        return this.getIdentifier();
    }

    default Color getParameterName()
    {
        return this.getIdentifier();
    }

    default Color getModifier()
    {
        return this.getKeyword();
    }

    default Color getClassModifier()
    {
        return this.getModifier();
    }

    default Color getPropertyModifier()
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

    default Color getVerb()
    {
        return this.getKeyword();
    }

    default Color getPropertyName()
    {
        return this.getIdentifier();
    }

    default Color getPrimitivePropertyName()
    {
        return this.getPropertyName();
    }

    default Color getEnumerationPropertyName()
    {
        return this.getPropertyName();
    }

    default Color getParameterizedPropertyName()
    {
        return this.getPropertyName();
    }

    default Color getAssociationEndName()
    {
        return this.getPropertyName();
    }

    default Color getClassReference()
    {
        return this.getClassName();
    }

    default Color getInterfaceReference()
    {
        return this.getInterfaceName();
    }

    default Color getProjectionReference()
    {
        return this.getProjectionName();
    }

    default Color getEnumerationReference()
    {
        return this.getEnumerationName();
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

    default Color getMemberReference()
    {
        return this.getPropertyName();
    }

    default Color getParameterReference()
    {
        return this.getParameterName();
    }
}
