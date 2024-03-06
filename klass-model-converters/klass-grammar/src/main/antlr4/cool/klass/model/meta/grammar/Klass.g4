grammar Klass;

compilationUnit: packageDeclaration topLevelDeclaration* EOF;
urlParameterDeclarationEOF: urlParameterDeclaration EOF;

packageDeclaration: 'package' packageName;

packageName
	:	identifier
	|	packageName '.' identifier
	;

topLevelDeclaration
    : interfaceDeclaration
    | classDeclaration
    | enumerationDeclaration
    | associationDeclaration
    | projectionDeclaration
    | serviceGroupDeclaration
    ;

// TODO: Consider splitting separate interfaceModifiers from classModifiers
// interface
interfaceDeclaration: 'interface' identifier implementsDeclaration? classModifier* interfaceBody;
interfaceBody: '{' interfaceMember* '}';

// class
classDeclaration: classOrUser identifier abstractDeclaration? extendsDeclaration? implementsDeclaration? classServiceModifier* classModifier* classBody;
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
enumerationBody: '{' (enumerationLiteral ',')* '}';
enumerationLiteral: identifier ('(' enumerationPrettyName ')')?;
enumerationPrettyName: StringLiteral;

// association
associationDeclaration: 'association' identifier associationBody;
associationBody: '{' associationEnd? associationEnd? relationship? '}';
associationEnd: identifier ':' classType associationEndModifier* orderByDeclaration? ';'
    | identifier ':' classType associationEndModifier* orderByDeclaration? {notifyErrorListeners("Missing semi-colon after association end declaration.");};
associationEndSignature: identifier ':' classifierReference multiplicity associationEndModifier* ';';
relationship: 'relationship' criteriaExpression;

// projection
projectionDeclaration: 'projection' identifier (parameterDeclarationList)? 'on' classReference projectionBody;
projectionBody: '{' (projectionMember ',')* '}';
projectionMember: projectionPrimitiveMember | projectionAssociationEnd | projectionParameterizedProperty | projectionProjectionReference;
projectionPrimitiveMember: (classifierReference '.')? identifier ':' header;
projectionAssociationEnd: (classifierReference '.')? identifier ':' projectionBody;
projectionProjectionReference: (classifierReference '.')? identifier ':' projectionReference;
projectionParameterizedProperty: (classifierReference '.')? identifier argumentList ':' projectionBody;
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
serviceDeclarationBody : '{' serviceMultiplicityDeclaration? serviceCriteriaDeclaration* serviceProjectionDispatch serviceOrderByDeclaration? '}' ;
serviceMultiplicityDeclaration: 'multiplicity' ':' serviceMultiplicity ';'
    | 'multiplicity' ':' serviceMultiplicity {notifyErrorListeners("Missing semi-colon after service multiplicity declaration.");};
serviceMultiplicity: one='one' | many='many';
serviceCriteriaDeclaration: serviceCriteriaKeyword ':' criteriaExpression ';'
    | serviceCriteriaKeyword ':' criteriaExpression {notifyErrorListeners("Missing semi-colon after service criteria declaration.");};
serviceCriteriaKeyword: 'criteria' | 'authorize' | 'validate' | 'conflict' | 'version';
serviceProjectionDispatch: 'projection' ':' projectionReference argumentList? ';'
    | 'projection' ':' projectionReference argumentList? {notifyErrorListeners("Missing semi-colon after service projection dispatch.");};
serviceOrderByDeclaration: orderByDeclaration ';'
    | orderByDeclaration {notifyErrorListeners("Missing semi-colon after service order-by declaration.");};
verb: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

// member
interfaceMember: dataTypeProperty | parameterizedPropertySignature | associationEndSignature;
classMember: dataTypeProperty | parameterizedProperty;
dataTypeProperty: primitiveProperty | enumerationProperty;
primitiveProperty: identifier ':' primitiveType optionalMarker? propertyModifier* dataTypePropertyValidation* ';'
    | identifier ':' primitiveType optionalMarker? propertyModifier* dataTypePropertyValidation* {notifyErrorListeners("Missing semi-colon after primitive property declaration.");};
enumerationProperty: identifier ':' enumerationReference optionalMarker? propertyModifier* dataTypePropertyValidation* ';'
    | identifier ':' enumerationReference optionalMarker? propertyModifier* dataTypePropertyValidation* {notifyErrorListeners("Missing semi-colon after enumeration property declaration.");};
parameterizedProperty: identifier '(' (parameterDeclaration (',' parameterDeclaration)*)? ')' ':' classType parameterizedPropertyModifier* orderByDeclaration? '{' criteriaExpression '}';
parameterizedPropertySignature: identifier '(' (parameterDeclaration (',' parameterDeclaration)*)? ')' ':' classifierReference multiplicity parameterizedPropertyModifier* ';';
optionalMarker: '?';

dataTypePropertyValidation
    : minLengthValidation
    | maxLengthValidation
    | minValidation
    | maxValidation
    ;

minLengthValidation: ('minLength' | 'minimumLength') '(' integerLiteral ')';
maxLengthValidation: ('maxLength' | 'maximumLength') '(' integerLiteral ')';
minValidation: ('min' | 'minimum') '(' integerLiteral ')';
maxValidation: ('max' | 'maximum') '(' integerLiteral ')';

// parameter
parameterDeclaration: primitiveParameterDeclaration | enumerationParameterDeclaration;
primitiveParameterDeclaration: identifier ':' primitiveType multiplicity parameterModifier*;
enumerationParameterDeclaration: identifier ':' enumerationReference multiplicity parameterModifier*;
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
classModifier: 'systemTemporal' | 'validTemporal' | 'bitemporal' | 'versioned' | 'audited' | 'optimisticallyLocked' | 'transient';
propertyModifier: 'key' | 'private' | 'userId' | 'id' | 'valid' | 'system' | 'from' | 'to' | 'createdBy' | 'createdOn' | 'lastUpdatedBy' | 'version';
associationEndModifier: 'owned' | 'final' | 'version';
parameterizedPropertyModifier: 'createdBy' | 'lastUpdatedBy';
parameterModifier: 'version' | 'userId' | 'id';

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

// Type references
classType: classReference multiplicity;

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
    | classModifier
    | propertyModifier
    | parameterModifier
    | associationEndModifier
    | verb
    | serviceCategoryModifier
    | inOperator | stringOperator
    // TODO: Split these primitive type keywords out, since they're really only ok as enumeration literals
    | primitiveType
    ;

literal
	:	integerLiteral
	|	floatingPointLiteral
	|	booleanLiteral
	// TODO: Character Reladomo types and character literals?
	|	characterLiteral
	|	stringLiteral
	|	nullLiteral
	;

integerLiteral: IntegerLiteral;
floatingPointLiteral: FloatingPointLiteral;
booleanLiteral: BooleanLiteral;
characterLiteral: CharacterLiteral;
stringLiteral: StringLiteral;
nullLiteral: NullLiteral;

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
