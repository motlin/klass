package cool.klass.model.converter.compiler.syntax.highlighter;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;

public final class TokenCategoryToColor
{
    private TokenCategoryToColor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Color getColor(
            TokenCategory tokenCategory,
            ColorScheme colorScheme)
    {
        switch (tokenCategory)
        {
            case BLOCK_COMMENT:
            {
                return colorScheme.getBlockComment();
            }
            case LINE_COMMENT:
            {
                return colorScheme.getLineComment();
            }
            case KEYWORD:
            case WORD_OPERATOR:
            case OPERATOR_IN:
            case OPERATOR_STRING:
            {
                return colorScheme.getKeyword();
            }
            case PACKAGE_KEYWORD:
            {
                return colorScheme.getPackageKeyword();
            }
            case KEYWORD_ENUMERATION:
            case KEYWORD_INTERFACE:
            case KEYWORD_USER:
            case KEYWORD_CLASS:
            case KEYWORD_PROJECTION:
            case KEYWORD_SERVICE:
            case KEYWORD_ABSTRACT:
            case KEYWORD_EXTENDS:
            case KEYWORD_IMPLEMENTS:
            case KEYWORD_INHERITANCE_TYPE:
            case KEYWORD_ASSOCIATION:
            case KEYWORD_RELATIONSHIP:
            case KEYWORD_ORDER_BY:
            case KEYWORD_ORDER_BY_DIRECTION:
            case KEYWORD_ON:
            case KEYWORD_MULTIPLICITY:
            case KEYWORD_MULTIPLICITY_CHOICE:
            case KEYWORD_SERVICE_CRITERIA:
            {
                return colorScheme.getKeyword();
            }
            case PRIMITIVE_TYPE:
            {
                return colorScheme.getPrimitiveType();
            }
            case VERB:
            case VERB_GET:
            case VERB_POST:
            case VERB_PUT:
            case VERB_PATCH:
            case VERB_DELETE:
            {
                return colorScheme.getVerb();
            }
            case MODIFIER:
            {
                return colorScheme.getModifier();
            }
            case CLASSIFIER_MODIFIER:
            {
                return colorScheme.getClassifierModifier();
            }
            case DATA_TYPE_PROPERTY_MODIFIER:
            {
                return colorScheme.getDataTypePropertyModifier();
            }
            case ASSOCIATION_END_MODIFIER:
            {
                return colorScheme.getAssociationEndModifier();
            }
            case PARAMETERIZED_PROPERTY_MODIFIER:
            {
                return colorScheme.getParameterizedPropertyModifier();
            }
            case PARAMETER_MODIFIER:
            {
                return colorScheme.getParameterModifier();
            }
            case VALIDATION_MODIFIER:
            {
                return colorScheme.getValidationModifier();
            }
            case SERVICE_CATEGORY_MODIFIER:
            {
                return colorScheme.getServiceCategoryModifier();
            }
            case IDENTIFIER:
            {
                return colorScheme.getIdentifier();
            }
            case PACKAGE_NAME:
            {
                return colorScheme.getPackageName();
            }
            case TOP_LEVEL_ELEMENT_NAME:
            {
                return colorScheme.getTopLevelElementName();
            }
            case ENUMERATION_NAME:
            {
                return colorScheme.getEnumerationName();
            }
            case CLASSIFIER_NAME:
            {
                return colorScheme.getClassifierName();
            }
            case INTERFACE_NAME:
            {
                return colorScheme.getInterfaceName();
            }
            case CLASS_NAME:
            {
                return colorScheme.getClassName();
            }
            case ASSOCIATION_NAME:
            {
                return colorScheme.getAssociationName();
            }
            case PROJECTION_NAME:
            {
                return colorScheme.getProjectionName();
            }
            case ENUMERATION_LITERAL_NAME:
            {
                return colorScheme.getEnumerationLiteralName();
            }
            case PARAMETER_NAME:
            {
                return colorScheme.getParameterName();
            }
            case PROPERTY_NAME:
            {
                return colorScheme.getPropertyName();
            }
            case DATA_TYPE_PROPERTY_NAME:
            {
                return colorScheme.getDataTypePropertyName();
            }
            case PRIMITIVE_PROPERTY_NAME:
            {
                return colorScheme.getPrimitivePropertyName();
            }
            case ENUMERATION_PROPERTY_NAME:
            {
                return colorScheme.getEnumerationPropertyName();
            }
            case REFERENCE_PROPERTY_NAME:
            {
                return colorScheme.getReferencePropertyName();
            }
            case PARAMETERIZED_PROPERTY_NAME:
            {
                return colorScheme.getParameterizedPropertyName();
            }
            case ASSOCIATION_END_NAME:
            {
                return colorScheme.getAssociationEndName();
            }
            case ENUMERATION_REFERENCE:
            {
                return colorScheme.getEnumerationReference();
            }
            case INTERFACE_REFERENCE:
            {
                return colorScheme.getInterfaceReference();
            }
            case CLASS_REFERENCE:
            {
                return colorScheme.getClassReference();
            }
            case PROJECTION_REFERENCE:
            {
                return colorScheme.getProjectionReference();
            }
            case DATA_TYPE_PROPERTY_REFERENCE:
            {
                return colorScheme.getDataTypePropertyReference();
            }
            case ASSOCIATION_END_REFERENCE:
            {
                return colorScheme.getAssociationEndReference();
            }
            case PARAMETERIZED_PROPERTY_REFERENCE:
            {
                return colorScheme.getParameterizedPropertyReference();
            }
            case PROPERTY_REFERENCE:
            {
                return colorScheme.getPropertyReference();
            }
            case PARAMETER_REFERENCE:
            {
                return colorScheme.getParameterReference();
            }
            case LITERAL:
            case LITERAL_THIS:
            case LITERAL_NATIVE:
            {
                return colorScheme.getLiteral();
            }
            case STRING_LITERAL:
            {
                return colorScheme.getStringLiteral();
            }
            case INTEGER_LITERAL:
            case ASTERISK_LITERAL:
            {
                return colorScheme.getIntegerLiteral();
            }
            case BOOLEAN_LITERAL:
            {
                return colorScheme.getBooleanLiteral();
            }
            case CHARACTER_LITERAL:
            {
                return colorScheme.getCharacterLiteral();
            }
            case FLOATING_POINT_LITERAL:
            {
                return colorScheme.getFloatingPointLiteral();
            }
            case PUNCTUATION:
            case COLON:
            case SLASH:
            case QUESTION:
            case PAIRED_PUNCTUATION:
            case PARENTHESES:
            case PARENTHESIS_LEFT:
            case PARENTHESIS_RIGHT:
            case CURLY_BRACES:
            case CURLY_LEFT:
            case CURLY_RIGHT:
            case SQUARE_BRACKETS:
            case SQUARE_BRACKET_LEFT:
            case SQUARE_BRACKET_RIGHT:
            {
                return colorScheme.getPunctuation();
            }
            case COMMA:
            {
                return colorScheme.getComma();
            }
            case DOT:
            {
                return colorScheme.getDot();
            }
            case DOTDOT:
            {
                return colorScheme.getDotDot();
            }
            case SEMICOLON:
            {
                return colorScheme.getSemi();
            }
            case OPERATOR:
            case OPERATOR_EQ:
            case OPERATOR_NE:
            case OPERATOR_LT:
            case OPERATOR_GT:
            case OPERATOR_LE:
            case OPERATOR_GE:
            {
                return colorScheme.getOperator();
            }
            default:
            {
                throw new AssertionError(tokenCategory);
            }
        }
    }
}
