grammar Klass;

@lexer::members {
    public static final int WHITESPACE_CHANNEL = 1000;
    public static final int COMMENTS_CHANNEL = 2000;
    public static final int LINE_COMMENTS_CHANNEL = 3000;
}

compilationUnit: packageDeclaration topLevelDeclaration* EOF;
urlParameterDeclarationEOF: urlParameterDeclaration EOF;

packageDeclaration: 'package' packageName;

packageName
    :    identifier
    |    packageName '.' identifier
    ;

topLevelDeclaration
    : interfaceDeclaration
    | classDeclaration
    | enumerationDeclaration
    | associationDeclaration
    | projectionDeclaration
    | serviceGroupDeclaration
    ;

// TODO: Consider splitting separate interfaceModifiers from classifierModifiers
// interface
interfaceDeclaration: interfaceHeader interfaceBody;
interfaceHeader : 'interface' identifier implementsDeclaration? classifierModifier* ;
interfaceBody: '{' interfaceMember* '}';

// class
classDeclaration: classHeader classBody;
classHeader : classOrUser identifier abstractDeclaration? extendsDeclaration? implementsDeclaration? classServiceModifier* classifierModifier* ;
classOrUser: 'class' | 'user';
classServiceModifier: serviceCategoryModifier ('(' projectionReference ')')?;
serviceCategoryModifier: 'read' | 'write' | 'create' | 'update' | 'delete';
classBody: '{' classMember* '}';

// inheritance
abstractDeclaration: 'abstract' ('(' inheritanceType ')')?;
extendsDeclaration: 'extends' classReference;
inheritanceType: 'table-per-subclass' | 'table-for-all-subclasses' | 'table-per-class';
implementsDeclaration: 'implements' interfaceReference (',' interfaceReference)*;

// enumeration
enumerationDeclaration: 'enumeration' identifier enumerationBody;
enumerationBody: '{' enumerationLiteral* '}';
enumerationLiteral: identifier ('(' enumerationPrettyName ')')? ',';
enumerationPrettyName: StringLiteral;

// association
associationDeclaration: 'association' identifier associationBody;
associationBody: '{' associationEnd? associationEnd? relationship? '}';
associationEnd
    : identifier ':' classReference multiplicity associationEndModifier* orderByDeclaration? ';'
    | identifier ':' classReference multiplicity associationEndModifier* orderByDeclaration? {notifyErrorListeners("Missing semi-colon after association end declaration.");};
associationEndSignature: identifier ':' classifierReference multiplicity associationEndModifier* ';';
relationship: 'relationship' criteriaExpression;

// projection
projectionDeclaration: 'projection' identifier (parameterDeclarationList)? 'on' classifierReference projectionBody;
projectionBody: '{' projectionMember* '}';
projectionMember: projectionPrimitiveMember | projectionReferenceProperty | projectionParameterizedProperty | projectionProjectionReference;
// TODO: Rename projectionPrimitiveMember --> projectionDataTypeMember
projectionPrimitiveMember: (classifierReference '.')? identifier ':' header ',';
projectionReferenceProperty: (classifierReference '.')? identifier ':' projectionBody ',';
projectionProjectionReference: (classifierReference '.')? identifier ':' projectionReference ',';
projectionParameterizedProperty: (classifierReference '.')? identifier argumentList ':' projectionBody ',';
header: StringLiteral;

// service
serviceGroupDeclaration: 'service' classReference serviceGroupDeclarationBody;
serviceGroupDeclarationBody : '{' urlDeclaration* '}' ;
// url
urlDeclaration: url serviceDeclaration+;
url: urlPathSegment+ '/'? queryParameterList?;
urlPathSegment: '/' (urlConstant | urlParameterDeclaration);
urlConstant: identifier;
queryParameterList: '?' urlParameterDeclaration ('&' urlParameterDeclaration)*;
urlParameterDeclaration: '{' parameterDeclaration '}';
// service
serviceDeclaration: verb serviceDeclarationBody;
serviceDeclarationBody : '{' serviceMultiplicityDeclaration? serviceCriteriaDeclaration* serviceProjectionDispatch? serviceOrderByDeclaration? '}' ;
serviceMultiplicityDeclaration: 'multiplicity' ':' serviceMultiplicity ';'
    | 'multiplicity' ':' serviceMultiplicity {notifyErrorListeners("Missing semi-colon after service multiplicity declaration.");};
serviceMultiplicity: one='one' | many='many';
serviceCriteriaDeclaration: serviceCriteriaKeyword ':' criteriaExpression ';'
    | serviceCriteriaKeyword ':' criteriaExpression {notifyErrorListeners("Missing semi-colon after service criteria declaration.");};
// TODO: Optional criteria
serviceCriteriaKeyword: 'criteria' | 'authorize' | 'validate' | 'conflict';
serviceProjectionDispatch: 'projection' ':' projectionReference argumentList? ';'
    | 'projection' ':' projectionReference argumentList? {notifyErrorListeners("Missing semi-colon after service projection dispatch.");};
serviceOrderByDeclaration: orderByDeclaration ';'
    | orderByDeclaration {notifyErrorListeners("Missing semi-colon after service order-by declaration.");};
verb: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

// member
interfaceMember: dataTypeProperty | parameterizedPropertySignature | associationEndSignature;
classMember: dataTypeProperty | parameterizedProperty | associationEndSignature;
dataTypeProperty: primitiveProperty | enumerationProperty;
primitiveProperty: identifier ':' primitiveType optionalMarker? dataTypePropertyModifier* dataTypePropertyValidation* ';'
    | identifier ':' primitiveType optionalMarker? dataTypePropertyModifier* dataTypePropertyValidation* {notifyErrorListeners("Missing semi-colon after primitive property declaration.");};
enumerationProperty: identifier ':' enumerationReference optionalMarker? dataTypePropertyModifier* dataTypePropertyValidation* ';'
    | identifier ':' enumerationReference optionalMarker? dataTypePropertyModifier* dataTypePropertyValidation* {notifyErrorListeners("Missing semi-colon after enumeration property declaration.");};
parameterizedProperty: identifier '(' (parameterDeclaration (',' parameterDeclaration)*)? ')' ':' classReference multiplicity parameterizedPropertyModifier* orderByDeclaration? '{' criteriaExpression '}';
parameterizedPropertySignature: identifier '(' (parameterDeclaration (',' parameterDeclaration)*)? ')' ':' classifierReference multiplicity parameterizedPropertyModifier* ';';
optionalMarker: '?';

dataTypePropertyValidation
    : minLengthValidation
    | maxLengthValidation
    | minValidation
    | maxValidation
    ;

minLengthValidation: minLengthValidationKeyword integerValidationParameter;
maxLengthValidation: maxLengthValidationKeyword integerValidationParameter;
minValidation: minValidationKeyword integerValidationParameter;
maxValidation: maxValidationKeyword integerValidationParameter;

integerValidationParameter : '(' integerLiteral ')' ;

minLengthValidationKeyword : ('minLength' | 'minimumLength') ;
maxLengthValidationKeyword : ('maxLength' | 'maximumLength') ;
minValidationKeyword : ('min' | 'minimum') ;
maxValidationKeyword : ('max' | 'maximum') ;


// parameter
parameterDeclaration: primitiveParameterDeclaration | enumerationParameterDeclaration | invalidParameterDeclaration;
primitiveParameterDeclaration: identifier ':' primitiveType multiplicity parameterModifier*;
enumerationParameterDeclaration: identifier ':' enumerationReference multiplicity parameterModifier*;
invalidParameterDeclaration: identifier {notifyErrorListeners("Missing type after parameter declaration.");};
parameterDeclarationList: '(' parameterDeclaration (',' parameterDeclaration)* ')';

// argument
argumentList: '(' (argument (',' argument)*)? ')';
argument
    : literal
    | literalList
    | nativeLiteral
    | variableReference
    ;

// multiplicity
multiplicity: '[' multiplicityBody ']';
multiplicityBody: lowerBound=IntegerLiteral '..' upperBound=(IntegerLiteral | '*');

primitiveType: 'Boolean' | 'Integer' | 'Long' | 'Double' | 'Float' | 'String' | 'Instant' | 'LocalDate' | 'TemporalInstant' | 'TemporalRange';

// modifiers
classifierModifier: 'systemTemporal' | 'validTemporal' | 'bitemporal' | 'versioned' | 'audited' | 'transient'
    | identifier {notifyErrorListeners("Unrecognized Classifier modifier: " + _input.LT(-1).getText());};
dataTypePropertyModifier: 'key' | 'private' | 'userId' | 'id' | 'valid' | 'system' | 'from' | 'to' | 'createdBy' | 'createdOn' | 'lastUpdatedBy' | 'version' | 'derived' | 'final'
    | identifier {notifyErrorListeners("Unrecognized DataTypeProperty modifier: " + _input.LT(-1).getText());};
associationEndModifier: 'owned' | 'final' | 'version' | 'private' | 'createdBy' | 'lastUpdatedBy'
    | identifier {notifyErrorListeners("Unrecognized AssociationEnd modifier: " + _input.LT(-1).getText());};
parameterizedPropertyModifier: 'createdBy' | 'lastUpdatedBy'
    | identifier {notifyErrorListeners("Unrecognized ParameterizedProperty modifier: " + _input.LT(-1).getText());};
parameterModifier: 'version' | 'userId' | 'id'
    | identifier {notifyErrorListeners("Unrecognized Parameter modifier: " + _input.LT(-1).getText());};

// order by
orderByDeclaration: 'orderBy' ':' orderByMemberReferencePath (',' orderByMemberReferencePath)*;
orderByMemberReferencePath: thisMemberReferencePath orderByDirection?;
orderByDirection: 'ascending' | 'descending';

// criteria
criteriaExpression
    : left=criteriaExpression '&&' right=criteriaExpression  #CriteriaExpressionAnd
    | left=criteriaExpression '||' right=criteriaExpression  #CriteriaExpressionOr
    | '(' criteriaExpression ')'                             #CriteriaExpressionGroup
    | 'all'                                                  #CriteriaAll
    | source=expressionValue operator target=expressionValue #CriteriaOperator
    | expressionMemberReference 'equalsEdgePoint'            #CriteriaEdgePoint
    | 'native' '(' identifier ')'                            #CriteriaNative
    ;
expressionValue
    : literal
    | literalList
    | thisMemberReferencePath
    | typeMemberReferencePath
    | nativeLiteral
    | variableReference
    ;
expressionMemberReference: thisMemberReferencePath | typeMemberReferencePath;
literalList: '(' literal (',' literal)* ')';
nativeLiteral: 'user';
operator
    : equalityOperator
    | inequalityOperator
    | inOperator
    | stringOperator
    ;
equalityOperator: '==' | '!=';
inequalityOperator: '<' | '>' | '<=' | '>=';
inOperator: 'in';
stringOperator: 'contains' | 'startsWith' | 'endsWith';

// references
interfaceReference: identifier;
classReference: identifier;
classifierReference: identifier;
enumerationReference: identifier;
projectionReference: identifier;
memberReference: identifier;
associationEndReference: identifier;
variableReference: identifier;

thisMemberReferencePath: 'this' ('.' associationEndReference)* '.' memberReference;
typeMemberReferencePath: classReference ('.' associationEndReference)* '.' memberReference;

identifier
    : Identifier
    | keywordValidAsIdentifier
    ;

keywordValidAsIdentifier
    : 'package'
    | 'enumeration' | 'interface' | 'class' | 'association' | 'projection' | 'service' | 'user'
    | 'native'
    | 'relationship'
    | 'multiplicity' | 'orderBy'
    | 'criteria'
    // classifierModifier
    | 'systemTemporal' | 'validTemporal' | 'bitemporal' | 'versioned' | 'audited' | 'transient'
    // dataTypePropertyModifier
    | 'key' | 'private' | 'userId' | 'id' | 'valid' | 'system' | 'from' | 'to' | 'createdBy' | 'createdOn' | 'lastUpdatedBy' | 'version' | 'derived'
    // associationEndModifier
    | 'owned' | 'final' | 'version'
    // parameterizedPropertyModifier
    | 'createdBy' | 'lastUpdatedBy'
    // parameterModifier
    | 'version' | 'userId' | 'id'
    | verb
    | serviceCategoryModifier
    | inOperator | stringOperator
    // TODO: Split these primitive type keywords out, since they're really only ok as enumeration literals
    | primitiveType
    ;

literal
    :    integerLiteral
    |    floatingPointLiteral
    |    booleanLiteral
    // TODO: Character Reladomo types and character literals?
    |    characterLiteral
    |    stringLiteral
    |    nullLiteral
    ;

integerLiteral       : IntegerLiteral;
floatingPointLiteral : FloatingPointLiteral;
booleanLiteral       : BooleanLiteral;
characterLiteral     : CharacterLiteral;
stringLiteral        : StringLiteral;
nullLiteral          : LITERAL_NULL;

// Lexer rules
KEYWORD_PACKAGE           : 'package';
KEYWORD_ENUMERATION       : 'enumeration';
KEYWORD_INTERFACE         : 'interface';
KEYWORD_CLASS             : 'class';
KEYWORD_ASSOCIATION       : 'association';
KEYWORD_PROJECTION        : 'projection';
KEYWORD_SERVICE           : 'service';
KEYWORD_USER              : 'user';
KEYWORD_NATIVE            : 'native';
KEYWORD_RELATIONSHIP      : 'relationship';
KEYWORD_MULTIPLICITY      : 'multiplicity';
KEYWORD_ORDER_BY          : 'orderBy';
KEYWORD_ASCENDING         : 'ascending';
KEYWORD_DESCENDING        : 'descending';
KEYWORD_CRITERIA          : 'criteria';
KEYWORD_ON                : 'on';
KEYWORD_ABSTRACT          : 'abstract';
KEYWORD_EXTENDS           : 'extends';
KEYWORD_IMPLEMENTS        : 'implements';
KEYWORD_ALL               : 'all';
KEYWORD_EQUALS_EDGE_POINT : 'equalsEdgePoint';

KEYWORD_TABLE_PER_SUBCLASS       : 'table-per-subclass';
KEYWORD_TABLE_FOR_ALL_SUBCLASSES : 'table-for-all-subclasses';
KEYWORD_TABLE_PER_CLASS          : 'table-per-class';


// Primitive types
PRIMITIVE_TYPE_BOOLEAN          : 'Boolean';
PRIMITIVE_TYPE_INTEGER          : 'Integer';
PRIMITIVE_TYPE_LONG             : 'Long';
PRIMITIVE_TYPE_DOUBLE           : 'Double';
PRIMITIVE_TYPE_FLOAT            : 'Float';
PRIMITIVE_TYPE_STRING           : 'String';
PRIMITIVE_TYPE_INSTANT          : 'Instant';
PRIMITIVE_TYPE_LOCAL_DATE       : 'LocalDate';
PRIMITIVE_TYPE_TEMPORAL_INSTANT : 'TemporalInstant';
PRIMITIVE_TYPE_TEMPORAL_RANGE   : 'TemporalRange';

// classifierModifiers
MODIFIER_CLASSIFIER_SYSTEM_TEMPORAL       : 'systemTemporal';
MODIFIER_CLASSIFIER_VALID_TEMPORAL        : 'validTemporal';
MODIFIER_CLASSIFIER_BITEMPORAL            : 'bitemporal';
MODIFIER_CLASSIFIER_VERSIONED             : 'versioned';
MODIFIER_CLASSIFIER_AUDITED               : 'audited';
MODIFIER_CLASSIFIER_TRANSIENT             : 'transient';

// propertyModifiers
MODIFIER_PROPERTY_KEY             : 'key';
MODIFIER_PROPERTY_PRIVATE         : 'private';
MODIFIER_PROPERTY_VALID           : 'valid';
MODIFIER_PROPERTY_SYSTEM          : 'system';
MODIFIER_PROPERTY_FROM            : 'from';
MODIFIER_PROPERTY_TO              : 'to';
MODIFIER_PROPERTY_CREATED_BY      : 'createdBy';
MODIFIER_PROPERTY_CREATED_ON      : 'createdOn';
MODIFIER_PROPERTY_LAST_UPDATED_BY : 'lastUpdatedBy';
MODIFIER_PROPERTY_DERIVED         : 'derived';

// associationEndModifiers
MODIFIER_ASSOCIATION_END_OWNED   : 'owned';
MODIFIER_ASSOCIATION_END_FINAL   : 'final';

// other modifiers (property AND parameter shared)
MODIFIER_VERSION : 'version';
MODIFIER_USER_ID : 'userId';
MODIFIER_ID      : 'id';

// validation modifiers
VALIDATION_MIN_LENGTH     : 'minLength';
VALIDATION_MINIMUM_LENGTH : 'minimumLength';
VALIDATION_MAX_LENGTH     : 'maxLength';
VALIDATION_MAXIMUM_LENGTH : 'maximumLength';
VALIDATION_MIN            : 'min';
VALIDATION_MINIMUM        : 'minimum';
VALIDATION_MAX            : 'max';
VALIDATION_MAXIMUM        : 'maximum';

// Punctuation
PUNCTUATION_LPAREN : '(';
PUNCTUATION_RPAREN : ')';
PUNCTUATION_LBRACE : '{';
PUNCTUATION_RBRACE : '}';
PUNCTUATION_LBRACK : '[';
PUNCTUATION_RBRACK : ']';
PUNCTUATION_SEMI   : ';';
PUNCTUATION_COLON  : ':';
PUNCTUATION_COMMA  : ',';
PUNCTUATION_DOT    : '.';
PUNCTUATION_DOTDOT : '..';
PUNCTUATION_SLASH  : '/';
PUNCTUATION_ASTERISK: '*';
PUNCTUATION_QUESTION: '?';

// Verbs
VERB_GET    : 'GET';
VERB_POST   : 'POST';
VERB_PUT    : 'PUT';
VERB_PATCH  : 'PATCH';
VERB_DELETE : 'DELETE';

// Literals
LITERAL_NULL  : 'null';
LITERAL_THIS  : 'this';

// Operators
OPERATOR_EQ          : '==';
OPERATOR_NE          : '!=';
OPERATOR_LT          : '<';
OPERATOR_GT          : '>';
OPERATOR_LE          : '<=';
OPERATOR_GE          : '>=';
OPERATOR_IN          : 'in';
OPERATOR_CONTAINS    : 'contains';
OPERATOR_STARTS_WITH : 'startsWith';
OPERATOR_ENDS_WITH   : 'endsWith';

// §3.10.1 Integer Literals

IntegerLiteral
    :    DecimalIntegerLiteral
    |    HexIntegerLiteral
    |    OctalIntegerLiteral
    |    BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :    DecimalNumeral IntegerTypeSuffix?
    ;

fragment
HexIntegerLiteral
    :    HexNumeral IntegerTypeSuffix?
    ;

fragment
OctalIntegerLiteral
    :    OctalNumeral IntegerTypeSuffix?
    ;

fragment
BinaryIntegerLiteral
    :    BinaryNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :    [lL]
    ;

fragment
DecimalNumeral
    :    '0'
    |    NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :    Digit (DigitsAndUnderscores? Digit)?
    ;

fragment
Digit
    :    '0'
    |    NonZeroDigit
    ;

fragment
NonZeroDigit
    :    [1-9]
    ;

fragment
DigitsAndUnderscores
    :    DigitOrUnderscore+
    ;

fragment
DigitOrUnderscore
    :    Digit
    |    '_'
    ;

fragment
Underscores
    :    '_'+
    ;

fragment
HexNumeral
    :    '0' [xX] HexDigits
    ;

fragment
HexDigits
    :    HexDigit (HexDigitsAndUnderscores? HexDigit)?
    ;

fragment
HexDigit
    :    [0-9a-fA-F]
    ;

fragment
HexDigitsAndUnderscores
    :    HexDigitOrUnderscore+
    ;

fragment
HexDigitOrUnderscore
    :    HexDigit
    |    '_'
    ;

fragment
OctalNumeral
    :    '0' Underscores? OctalDigits
    ;

fragment
OctalDigits
    :    OctalDigit (OctalDigitsAndUnderscores? OctalDigit)?
    ;

fragment
OctalDigit
    :    [0-7]
    ;

fragment
OctalDigitsAndUnderscores
    :    OctalDigitOrUnderscore+
    ;

fragment
OctalDigitOrUnderscore
    :    OctalDigit
    |    '_'
    ;

fragment
BinaryNumeral
    :    '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :    BinaryDigit (BinaryDigitsAndUnderscores? BinaryDigit)?
    ;

fragment
BinaryDigit
    :    [01]
    ;

fragment
BinaryDigitsAndUnderscores
    :    BinaryDigitOrUnderscore+
    ;

fragment
BinaryDigitOrUnderscore
    :    BinaryDigit
    |    '_'
    ;

// §3.10.2 Floating-Point Literals

FloatingPointLiteral
    :    DecimalFloatingPointLiteral
    |    HexadecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :    Digits '.' Digits ExponentPart? FloatTypeSuffix?
    |    '.' Digits ExponentPart? FloatTypeSuffix?
    |    Digits ExponentPart FloatTypeSuffix?
    |    Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :    ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :    [eE]
    ;

fragment
SignedInteger
    :    Sign? Digits
    ;

fragment
Sign
    :    [+-]
    ;

fragment
FloatTypeSuffix
    :    [fFdD]
    ;

fragment
HexadecimalFloatingPointLiteral
    :    HexSignificand BinaryExponent FloatTypeSuffix?
    ;

fragment
HexSignificand
    :    HexNumeral '.'?
    |    '0' [xX] HexDigits? '.' HexDigits
    ;

fragment
BinaryExponent
    :    BinaryExponentIndicator SignedInteger
    ;

fragment
BinaryExponentIndicator
    :    [pP]
    ;

// §3.10.3 Boolean Literals

BooleanLiteral
    :    LITERAL_TRUE
    |    LITERAL_FALSE
    ;

LITERAL_TRUE  : 'true';
LITERAL_FALSE : 'false';

// §3.10.4 Character Literals

CharacterLiteral
    :    '\'' SingleCharacter '\''
    |    '\'' EscapeSequence '\''
    ;

fragment
SingleCharacter
    :    ~['\\\r\n]
    ;
// §3.10.5 String Literals
StringLiteral
    :    '"' StringCharacters? '"'
    ;
fragment
StringCharacters
    :    StringCharacter+
    ;
fragment
StringCharacter
    :    ~["\\\r\n]
    |    EscapeSequence
    ;
// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
    :    '\\' [btnfr"'\\]
    |    OctalEscape
    |   UnicodeEscape // This is not in the spec but prevents having to preprocess the input
    ;

fragment
OctalEscape
    :    '\\' OctalDigit
    |    '\\' OctalDigit OctalDigit
    |    '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
ZeroToThree
    :    [0-3]
    ;

// This is not in the spec but prevents having to preprocess the input
fragment
UnicodeEscape
    :   '\\' 'u'+  HexDigit HexDigit HexDigit HexDigit
    ;

// §3.8 Identifiers (must appear after all keywords in the grammar)

Identifier
    :    JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :    [a-zA-Z$_] // these are the "java letters" below 0x7F
    |    // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |    // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :    [a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
    |    // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |    // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

//
// Whitespace and comments
//

WHITESPACE  : [ \t\u000C]+  -> channel(1000);
NEWLINE     : [\r\n]        -> channel(1000);
COMMENT     : '/*' .*? '*/' -> channel(2000);
LINE_COMMENT: '//' ~[\r\n]* -> channel(3000);
