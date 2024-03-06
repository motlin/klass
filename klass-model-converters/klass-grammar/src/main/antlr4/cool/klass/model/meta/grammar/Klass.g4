grammar Klass;

compilationUnit: packageDeclaration topLevelDeclaration* EOF;

packageDeclaration: 'package' packageName;

packageName
	:	identifier
	|	packageName '.' identifier
	;

topLevelDeclaration
    : classDeclaration
    | enumerationDeclaration
    | associationDeclaration
    | projectionDeclaration
    | serviceGroupDeclaration
    ;

// class
classDeclaration: classOrUser identifier classServiceModifier* classModifier* classBody;
classOrUser: 'class' | 'user';
classServiceModifier: serviceCategoryModifier ('(' projectionReference ')')?;
serviceCategoryModifier: 'read' | 'write' | 'create' | 'update' | 'delete';
classBody: '{' classMember* '}';

// enumeration
enumerationDeclaration: 'enumeration' identifier enumerationBody;
enumerationBody: '{' (enumerationLiteral ',')* '}';
enumerationLiteral: identifier ('(' enumerationPrettyName ')')?;
enumerationPrettyName: StringLiteral;

// association
associationDeclaration: 'association' identifier associationBody;
associationBody: '{' associationEnd* relationship? '}';
associationEnd: associationEndModifier* identifier ':' classType orderByDeclaration?;
relationship: 'relationship' criteriaExpression;

// projection
projectionDeclaration: 'projection' identifier (parameterDeclarationList)? 'on' classReference projectionBody;
projectionBody: '{' (projectionMember ',')* '}';
projectionMember: projectionMemberWithBody | projectionPrimitiveMember;
// TODO: Call this an *Invocation instead of projection*?
projectionMemberWithBody: identifier argumentList? ':' projectionBody;
projectionPrimitiveMember: identifier ':' header;
header: StringLiteral;

// service
serviceGroupDeclaration: 'service' classReference '{' urlDeclaration* '}';
// url
urlDeclaration: url serviceDeclaration+;
url: urlPathSegment+ '/'? queryParameterList?;
urlPathSegment: '/' (urlConstant=identifier | urlParameterDeclaration);
queryParameterList: '?' urlParameterDeclaration ('&' urlParameterDeclaration)*;
urlParameterDeclaration: '{' parameterDeclaration '}';
// service
serviceDeclaration: verb '{' serviceMultiplicityDeclaration? serviceCriteriaDeclaration* serviceProjectionDispatch orderByDeclaration? '}';
serviceMultiplicityDeclaration: 'multiplicity' ':' serviceMultiplicity;
serviceMultiplicity: one='one' | many='many';
serviceCriteriaDeclaration: serviceCriteriaKeyword ':' criteriaExpression;
serviceCriteriaKeyword: 'criteria' | 'authorize' | 'validate' | 'conflict';
serviceProjectionDispatch: 'projection' ':' projectionReference argumentList?;
verb: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

// member
classMember: dataTypeProperty | parameterizedProperty;
dataTypeProperty : primitiveProperty | enumerationProperty;
primitiveProperty : propertyModifier* escapedIdentifier ':' primitiveType optionalMarker?;
enumerationProperty : propertyModifier* escapedIdentifier ':' enumerationReference optionalMarker?;
parameterizedProperty: escapedIdentifier '(' (parameterDeclaration (',' parameterDeclaration)*)? ')' ':' classType orderByDeclaration? '{' criteriaExpression '}';
optionalMarker: '?';

// parameter
parameterDeclaration: identifier (':' dataTypeDeclaration)?;
dataTypeDeclaration: dataType multiplicity;
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

primitiveType: 'ID' | 'Boolean' | 'Integer' | 'Long' | 'Double' | 'Float' | 'String' | 'Instant' | 'LocalDate';

// modifiers
classModifier: 'systemTemporal' | 'validTemporal' | 'bitemporal' | 'versioned' | 'audited' | 'optimisticallyLocked';
propertyModifier: 'key' | 'private';
associationEndModifier: 'owned';

// order by
orderByDeclaration: 'orderBy' ':' orderByList;
orderByList: orderByProperty (',' orderByProperty)*;
orderByProperty: thisMemberReference orderByDirection?;
orderByDirection: 'ascending' | 'descending';

// criteria
criteriaExpression
    : criteriaExpression '&&' criteriaExpression             #CriteriaExpressionAnd
    | criteriaExpression '||' criteriaExpression             #CriteriaExpressionOr
    | '(' criteriaExpression ')'                             #CriteriaExpressionGroup
    | source=expressionValue operator target=expressionValue #CriteriaOperator
    | 'native' '(' identifier ')'                            #CriteriaNative
    ;
expressionValue
    : literal
    | literalList
    | typeMemberReference
    | nativeLiteral
    | variableReference
    ;
literalList: '(' literal (',' literal)* ')';
nativeLiteral: 'user';
operator: '==' | '!=' | '<' | '>' | '<=' | '>=' | 'in' | 'contains' | 'startsWith' | 'endsWith';

// Type references
classType: classReference multiplicity;
dataType: primitiveType | enumerationReference;

// references
classReference: identifier;
enumerationReference: identifier;
projectionReference: identifier;
memberReference: identifier;
variableReference: identifier;

thisMemberReference: 'this' '.' memberReference;
typeMemberReference: typeReference '.' memberReference;
typeReference: 'this' | classReference;

identifier
    : keywordValidAsIdentifier
    | Identifier
    ;

escapedIdentifier: identifier | '`' keywordValidAsIdentifier '`';

keywordValidAsIdentifier
    : 'package'
    | 'class' | 'enumeration' | 'association' | 'projection' | 'service' | 'user'
    | 'systemTemporal' | 'validTemporal' | 'bitemporal' | 'versioned' | 'audited' | 'optimisticallyLocked'
    | 'key' | 'private' | 'owned'
    | 'read' | 'write' | 'create' | 'update' | 'delete'
    | 'in' | 'contains' | 'startsWith' | 'endsWith'
    | 'native'
    | 'relationship'
    | 'multiplicity' | 'orderBy'
    // TODO: Split these keywords out, since they're really only ok as enumeration literals
    | 'ID' | 'Boolean' | 'Integer' | 'Long' | 'Double' | 'Float' | 'String' | 'Instant' | 'LocalDate'
    ;

literal
	:	IntegerLiteral
	|	FloatingPointLiteral
	|	BooleanLiteral
	// TODO: Character Reladomo types and character literals?
	|	CharacterLiteral
	|	StringLiteral
	|	NullLiteral
	;

// §3.10.1 Integer Literals

IntegerLiteral
	:	DecimalIntegerLiteral
	|	HexIntegerLiteral
	|	OctalIntegerLiteral
	|	BinaryIntegerLiteral
	;

fragment
DecimalIntegerLiteral
	:	DecimalNumeral IntegerTypeSuffix?
	;

fragment
HexIntegerLiteral
	:	HexNumeral IntegerTypeSuffix?
	;

fragment
OctalIntegerLiteral
	:	OctalNumeral IntegerTypeSuffix?
	;

fragment
BinaryIntegerLiteral
	:	BinaryNumeral IntegerTypeSuffix?
	;

fragment
IntegerTypeSuffix
	:	[lL]
	;

fragment
DecimalNumeral
	:	'0'
	|	NonZeroDigit (Digits? | Underscores Digits)
	;

fragment
Digits
	:	Digit (DigitsAndUnderscores? Digit)?
	;

fragment
Digit
	:	'0'
	|	NonZeroDigit
	;

fragment
NonZeroDigit
	:	[1-9]
	;

fragment
DigitsAndUnderscores
	:	DigitOrUnderscore+
	;

fragment
DigitOrUnderscore
	:	Digit
	|	'_'
	;

fragment
Underscores
	:	'_'+
	;

fragment
HexNumeral
	:	'0' [xX] HexDigits
	;

fragment
HexDigits
	:	HexDigit (HexDigitsAndUnderscores? HexDigit)?
	;

fragment
HexDigit
	:	[0-9a-fA-F]
	;

fragment
HexDigitsAndUnderscores
	:	HexDigitOrUnderscore+
	;

fragment
HexDigitOrUnderscore
	:	HexDigit
	|	'_'
	;

fragment
OctalNumeral
	:	'0' Underscores? OctalDigits
	;

fragment
OctalDigits
	:	OctalDigit (OctalDigitsAndUnderscores? OctalDigit)?
	;

fragment
OctalDigit
	:	[0-7]
	;

fragment
OctalDigitsAndUnderscores
	:	OctalDigitOrUnderscore+
	;

fragment
OctalDigitOrUnderscore
	:	OctalDigit
	|	'_'
	;

fragment
BinaryNumeral
	:	'0' [bB] BinaryDigits
	;

fragment
BinaryDigits
	:	BinaryDigit (BinaryDigitsAndUnderscores? BinaryDigit)?
	;

fragment
BinaryDigit
	:	[01]
	;

fragment
BinaryDigitsAndUnderscores
	:	BinaryDigitOrUnderscore+
	;

fragment
BinaryDigitOrUnderscore
	:	BinaryDigit
	|	'_'
	;

// §3.10.2 Floating-Point Literals

FloatingPointLiteral
	:	DecimalFloatingPointLiteral
	|	HexadecimalFloatingPointLiteral
	;

fragment
DecimalFloatingPointLiteral
	:	Digits '.' Digits ExponentPart? FloatTypeSuffix?
	|	'.' Digits ExponentPart? FloatTypeSuffix?
	|	Digits ExponentPart FloatTypeSuffix?
	|	Digits FloatTypeSuffix
	;

fragment
ExponentPart
	:	ExponentIndicator SignedInteger
	;

fragment
ExponentIndicator
	:	[eE]
	;

fragment
SignedInteger
	:	Sign? Digits
	;

fragment
Sign
	:	[+-]
	;

fragment
FloatTypeSuffix
	:	[fFdD]
	;

fragment
HexadecimalFloatingPointLiteral
	:	HexSignificand BinaryExponent FloatTypeSuffix?
	;

fragment
HexSignificand
	:	HexNumeral '.'?
	|	'0' [xX] HexDigits? '.' HexDigits
	;

fragment
BinaryExponent
	:	BinaryExponentIndicator SignedInteger
	;

fragment
BinaryExponentIndicator
	:	[pP]
	;

// §3.10.3 Boolean Literals

BooleanLiteral
	:	'true'
	|	'false'
	;

// §3.10.4 Character Literals

CharacterLiteral
	:	'\'' SingleCharacter '\''
	|	'\'' EscapeSequence '\''
	;

fragment
SingleCharacter
	:	~['\\\r\n]
	;
// §3.10.5 String Literals
StringLiteral
	:	'"' StringCharacters? '"'
	;
fragment
StringCharacters
	:	StringCharacter+
	;
fragment
StringCharacter
	:	~["\\\r\n]
	|	EscapeSequence
	;
// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
	:	'\\' [btnfr"'\\]
	|	OctalEscape
    |   UnicodeEscape // This is not in the spec but prevents having to preprocess the input
	;

fragment
OctalEscape
	:	'\\' OctalDigit
	|	'\\' OctalDigit OctalDigit
	|	'\\' ZeroToThree OctalDigit OctalDigit
	;

fragment
ZeroToThree
	:	[0-3]
	;

// This is not in the spec but prevents having to preprocess the input
fragment
UnicodeEscape
    :   '\\' 'u'+  HexDigit HexDigit HexDigit HexDigit
    ;

// §3.10.7 The Null Literal

NullLiteral
	:	'null'
	;

// §3.11 Separators

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
SEMI: ';';
COMMA: ',';
DOT: '.';

// §3.8 Identifiers (must appear after all keywords in the grammar)

Identifier
	:	JavaLetter JavaLetterOrDigit*
	;

fragment
JavaLetter
	:	[a-zA-Z$_] // these are the "java letters" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment
JavaLetterOrDigit
	:	[a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;
