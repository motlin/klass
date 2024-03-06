lexer grammar JavaLexer;

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
