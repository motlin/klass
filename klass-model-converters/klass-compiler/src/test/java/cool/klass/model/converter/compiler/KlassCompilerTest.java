package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.test.constants.KlassTestConstants;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class KlassCompilerTest
{
    private final CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
    private final KlassCompiler       compiler            = new KlassCompiler(this.compilerErrorHolder);

    @Test
    public void stackOverflow()
    {
        this.assertNoCompilerErrors(KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT);
    }

    @Test
    public void meta()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package klass.meta\n"
                + "\n"
                + "class Class\n"
                + "    transient\n"
                + "{\n"
                + "    name                          : String key;\n"
                + "    packageName                   : String private;\n"
                + "}\n"
                + "\n"
                + "enumeration PrimitiveType\n"
                + "{\n"
                + "    INTEGER(\"Integer\"),\n"
                + "    LONG(\"Long\"),\n"
                + "    DOUBLE(\"Double\"),\n"
                + "    FLOAT(\"Float\"),\n"
                + "    BOOLEAN(\"Boolean\"),\n"
                + "    STRING(\"String\"),\n"
                + "    INSTANT(\"Instant\"),\n"
                + "    LOCAL_DATE(\"LocalDate\"),\n"
                + "    TEMPORAL_INSTANT(\"TemporalInstant\"),\n"
                + "    TEMPORAL_RANGE(\"TemporalRange\"),\n"
                + "}\n"
                + "\n"
                + "enumeration Multiplicity\n"
                + "{\n"
                + "    ZERO_TO_ONE(\"0..1\"),\n"
                + "    ONE_TO_ONE(\"1..1\"),\n"
                + "    ZERO_TO_MANY(\"0..*\"),\n"
                + "    ONE_TO_MANY(\"1..*\"),\n"
                + "}\n"
                + "\n"
                + "class PrimitiveTypeProperty\n"
                + "    transient\n"
                + "{\n"
                + "    className                     : String key;\n"
                + "    name                          : String key;\n"
                + "    primitiveType                 : PrimitiveType;\n"
                + "    optional                      : Boolean;\n"
                + "\n"
                + "    // key is a keyword, needs backticks to use as identifier\n"
                + "    key                           : Boolean;\n"
                + "}\n"
                + "\n"
                + "association ClassHasPrimitiveTypeProperties\n"
                + "{\n"
                + "    owningClass: Class[1..1];\n"
                + "    primitiveTypeProperties: PrimitiveTypeProperty[0..*] owned;\n"
                + "\n"
                + "    relationship this.name == PrimitiveTypeProperty.className\n"
                + "}\n"
                + "\n"
                + "class Enumeration\n"
                + "    transient\n"
                + "{\n"
                + "    name                          : String key;\n"
                + "    packageName                   : String private;\n"
                + "}\n"
                + "\n"
                + "class EnumerationLiteral\n"
                + "    transient\n"
                + "{\n"
                + "    enumerationName               : String key;\n"
                + "    name                          : String key;\n"
                + "}\n"
                + "\n"
                + "association EnumerationHasLiterals\n"
                + "{\n"
                + "    enumeration: Enumeration[1..1];\n"
                + "    enumerationLiterals: EnumerationLiteral[1..*];\n"
                + "\n"
                + "    relationship this.name == EnumerationLiteral.enumerationName\n"
                + "}\n"
                + "\n"
                + "class EnumerationProperty\n"
                + "    transient\n"
                + "{\n"
                + "    className                     : String key;\n"
                + "    name                          : String key;\n"
                + "    optional                      : Boolean key;\n"
                + "    key                           : Boolean key;\n"
                + "    enumerationName               : String private;\n"
                + "}\n"
                + "\n"
                + "association EnumerationPropertyHasEnumeration\n"
                + "{\n"
                + "    enumerationProperty: EnumerationProperty[0..*];\n"
                + "    enumeration: Enumeration[1..1];\n"
                + "\n"
                + "    relationship this.enumerationName == Enumeration.name\n"
                + "}\n"
                + "\n"
                + "association ClassHasEnumerationProperties\n"
                + "{\n"
                + "    owningClass: Class[1..1];\n"
                + "    enumerationProperties: EnumerationProperty[0..*];\n"
                + "\n"
                + "    relationship this.name == EnumerationProperty.className\n"
                + "}\n"
                + "\n"
                + "enumeration AssociationEndDirection\n"
                + "{\n"
                + "    SOURCE(\"source\"),\n"
                + "    TARGET(\"target\"),\n"
                + "}\n"
                + "\n"
                + "class Association\n"
                + "    transient\n"
                + "{\n"
                + "    name                          : String key;\n"
                + "    packageName                   : String private;\n"
                + "\n"
                + "    source(): AssociationEnd[1..1]\n"
                + "    {\n"
                + "        this.name == AssociationEnd.associationName\n"
                + "            && AssociationEnd.direction == AssociationEndDirection.SOURCE\n"
                + "    }\n"
                + "\n"
                + "    target(): AssociationEnd[1..1]\n"
                + "    {\n"
                + "        this.name == AssociationEnd.associationName\n"
                + "            && AssociationEnd.direction == AssociationEndDirection.TARGET\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "class AssociationEnd\n"
                + "    transient\n"
                + "{\n"
                + "    owningClassName               : String key private;\n"
                + "    name                          : String key;\n"
                + "    associationName               : String;\n"
                + "    direction                     : AssociationEndDirection;\n"
                + "    multiplicity                  : Multiplicity;\n"
                + "    resultTypeName                : String private;\n"
                + "}\n"
                + "\n"
                + "// simplification, ideally we'd model an association as having exactly two ends\n"
                + "association AssociationHasEnds\n"
                + "{\n"
                + "    association: Association[1..1];\n"
                + "    associationEnds: AssociationEnd[0..*];\n"
                + "\n"
                + "    relationship this.name == AssociationEnd.associationName\n"
                + "}\n"
                + "\n"
                + "association ClassHasAssociationEnds\n"
                + "{\n"
                + "    owningType: Class[1..1];\n"
                + "    associationEnds: AssociationEnd[0..*];\n"
                + "\n"
                + "    relationship this.name == AssociationEnd.owningClassName\n"
                + "}\n"
                + "\n"
                + "association AssociationEndHasResultType\n"
                + "{\n"
                + "    associationEndsResultTypeOf: AssociationEnd[0..*];\n"
                + "    resultType: Class[1..1];\n"
                + "\n"
                + "    relationship this.resultTypeName == Class.name\n"
                + "}\n"
                + "\n"
                + "class ParameterizedProperty\n"
                + "    transient\n"
                + "{\n"
                + "    owningClassName               : String key private;\n"
                + "    name                          : String key;\n"
                + "    multiplicity                  : Multiplicity;\n"
                + "    resultTypeName                : String private;\n"
                + "}\n"
                + "\n"
                + "association ClassHasParameterizedProperties\n"
                + "{\n"
                + "    owningType: Class[1..1];\n"
                + "    parameterizedProperties: ParameterizedProperty[0..*] owned;\n"
                + "\n"
                + "    relationship this.name == ParameterizedProperty.owningClassName\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasResultType\n"
                + "{\n"
                + "    parameterizedPropertiesResultTypeOf: ParameterizedProperty[0..*];\n"
                + "    resultType: Class[1..1];\n"
                + "\n"
                + "    relationship this.resultTypeName == Class.name\n"
                + "}\n"
                + "\n"
                + "class AssociationEndOrdering\n"
                + "    transient\n"
                + "{\n"
                + "    associationName               : String key private;\n"
                + "    name                          : String;\n"
                + "    multiplicity                  : Multiplicity;\n"
                + "    direction                     : AssociationEndDirection key;\n"
                + "    orderingId                     : Long private;\n"
                + "}\n"
                + "\n"
                + "class ParameterizedPropertyOrdering\n"
                + "    transient\n"
                + "{\n"
                + "    owningClassName               : String private key;\n"
                + "    name                          : String key;\n"
                + "    orderingId                     : Long private;\n"
                + "}\n"
                + "\n"
                + "association AssociationEndHasOrdering\n"
                + "{\n"
                + "    associationEnd: AssociationEnd[1..1];\n"
                + "    associationEndOrdering: AssociationEndOrdering[0..1] owned;\n"
                + "\n"
                + "    relationship this.associationName == AssociationEndOrdering.associationName\n"
                + "            && this.direction == AssociationEndOrdering.direction\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasOrdering\n"
                + "{\n"
                + "    parameterizedProperty: ParameterizedProperty[1..1];\n"
                + "    parameterizedPropertyOrdering: ParameterizedPropertyOrdering[0..*];\n"
                + "\n"
                + "    relationship this.owningClassName == ParameterizedPropertyOrdering.owningClassName\n"
                + "            && this.name == ParameterizedPropertyOrdering.name\n"
                + "}\n"
                + "\n"
                + "association AssociationEndOrderingHasParts\n"
                + "{\n"
                + "    associationEndOrdering: AssociationEndOrdering[1..1];\n"
                + "    ordering: Ordering[1..*];\n"
                + "\n"
                + "    relationship this.orderingId == Ordering.id\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyOrderingHasParts\n"
                + "{\n"
                + "    parameterizedPropertyOrdering: ParameterizedPropertyOrdering[1..1] owned;\n"
                + "    ordering: Ordering[1..1];\n"
                + "\n"
                + "    // TODO: error when there's no \"this\"\n"
                + "    // relationship ParameterizedPropertyOrdering.orderingId == Ordering.id\n"
                + "    // TODO: error when the foreign key for a to-many is its entire primary key\n"
                + "    // relationship ParameterizedPropertyOrdering.orderingId == Ordering.id\n"
                + "    relationship this.orderingId == Ordering.id\n"
                + "}\n"
                + "\n"
                + "class Ordering\n"
                + "    transient\n"
                + "{\n"
                + "    id                            : Long id key;\n"
                + "}\n"
                + "\n"
                + "// Split into data type / enum type order bys?\n"
                + "class OrderingPart\n"
                + "    transient\n"
                + "{\n"
                + "    orderingId                     : Long key;\n"
                + "\n"
                + "    // TODO: order keyword for properties meant for sorting?\n"
                + "    ordinal                       : Integer key;\n"
                + "    className                     : String private;\n"
                + "    propertyName                  : String private;\n"
                + "    direction                     : OrderingDirection;\n"
                + "}\n"
                + "\n"
                + "association OrderingHasParts\n"
                + "{\n"
                + "    ordering: Ordering[1..1];\n"
                + "    orderingParts: OrderingPart[1..*]\n"
                + "        orderBy: this.ordinal;\n"
                + "\n"
                + "    relationship this.id == OrderingPart.orderingId\n"
                + "}\n"
                + "\n"
                + "enumeration OrderingDirection\n"
                + "{\n"
                + "    ASCENDING(\"ascending\"),\n"
                + "    DESCENDING(\"descending\"),\n"
                + "}\n"
                + "\n"
                + "association OrderingPartHasDataTypeProperty\n"
                + "{\n"
                + "    orderingPart: OrderingPart[0..*];\n"
                + "    dataTypeProperty: PrimitiveTypeProperty[1..1];\n"
                + "\n"
                + "    relationship this.className == PrimitiveTypeProperty.className\n"
                + "            && this.propertyName == PrimitiveTypeProperty.name\n"
                + "}\n"
                + "\n"
                + "association OrderingPartHasEnumerationProperty\n"
                + "{\n"
                + "    orderingPart: OrderingPart[0..*];\n"
                + "    enumerationProperty: EnumerationProperty[1..1];\n"
                + "\n"
                + "    relationship this.className == EnumerationProperty.className\n"
                + "            && this.propertyName == EnumerationProperty.name\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasParameters\n"
                + "{\n"
                + "    parameterizedProperty: ParameterizedProperty[1..1];\n"
                + "    parameters: ParameterizedPropertyParameter[0..*];\n"
                + "\n"
                + "    relationship this.owningClassName == ParameterizedPropertyParameter.parameterizedPropertyClassName\n"
                + "            && this.name == ParameterizedPropertyParameter.parameterizedPropertyName\n"
                + "}\n"
                + "\n"
                + "class ParameterizedPropertyParameter\n"
                + "    transient\n"
                + "{\n"
                + "    parameterizedPropertyClassName: String key private;\n"
                + "    parameterizedPropertyName     : String key;\n"
                + "    name                          : String;\n"
                + "}\n"
                + "\n"
                + "/*\n"
                + "association ParameterizedPropertyHasCriteria\n"
                + "{\n"
                + "\n"
                + "}\n"
                + "*/\n"
                + "\n"
                + "class Package\n"
                + "    transient\n"
                + "{\n"
                + "    fullyQualifiedName            : String key;\n"
                + "}\n"
                + "\n"
                + "association PackageHasClasses\n"
                + "{\n"
                + "    owningPackage: Package[1..1];\n"
                + "    classes: Class[0..*];\n"
                + "\n"
                + "    relationship this.fullyQualifiedName == Class.packageName\n"
                + "}\n"
                + "\n"
                + "association PackageHasEnumerations\n"
                + "{\n"
                + "    owningPackage: Package[1..1];\n"
                + "    enumerations: Enumeration[0..*];\n"
                + "\n"
                + "    relationship this.fullyQualifiedName == Enumeration.packageName\n"
                + "}\n"
                + "\n"
                + "association PackageHasAssociations\n"
                + "{\n"
                + "    owningPackage: Package[1..1];\n"
                + "    associations: Association[0..*];\n"
                + "\n"
                + "    relationship this.fullyQualifiedName == Association.packageName\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void doubleOwnedAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "association DoubleOwnedAssociation\n"
                + "{\n"
                + "    source: DoubleOwnedClass[1..1] owned;\n"
                + "    target: DoubleOwnedClass[1..1] owned;\n"
                + "    \n"
                + "    relationship this.id == DoubleOwnedClass.id\n"
                + "}\n"
                + "\n"
                + "class DoubleOwnedClass\n"
                + "{\n"
                + "    id: Long key;\n"
                + "}\n";
        //</editor-fold>

        String error =
                ""
                        + "File: example.klass Line: 5 Char: 36 Error: ERR_ASO_OWN: Both associations are owned in association 'DoubleOwnedAssociation'. At most one end may be owned.\n"
                        + "package dummy\n"
                        + "association DoubleOwnedAssociation\n"
                        + "{\n"
                        + "    source: DoubleOwnedClass[1..1] owned;\n"
                        + "                                   ^^^^^\n"
                        + "    target: DoubleOwnedClass[1..1] owned;\n"
                        + "                                   ^^^^^\n"
                        + "}\n";

        this.assertCompilerErrors(sourceCodeText, error);
    }

    @Test
    public void doubleVersionAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "association DoubleVersionAssociation\n"
                + "{\n"
                + "    source: ExampleSource[1..1] version;\n"
                + "    target: ExampleTarget[1..1] version;\n"
                + "\n"
                + "    relationship this.id == ExampleTarget.id\n"
                + "}\n"
                + "\n"
                + "class ExampleSource\n"
                + "{\n"
                + "    id: Long key;\n"
                + "}\n"
                + "\n"
                + "class ExampleTarget\n"
                + "{\n"
                + "    id: Long key;\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: example.klass Line: 5 Char: 26 Error: ERR_ASO_SYM: Association 'DoubleVersionAssociation' is perfectly symmetrical, so foreign keys cannot be inferred. To break the symmetry, make one end owned, or make one end required and the other end optional.\n"
                        + "package dummy\n"
                        + "association DoubleVersionAssociation\n"
                        + "{\n"
                        + "    source: ExampleSource[1..1] version;\n"
                        + "                         ^^^^^^\n"
                        + "    target: ExampleTarget[1..1] version;\n"
                        + "                         ^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 11 Char: 7 Error: ERR_VER_VER: Class 'ExampleSource' is a version and has a version.\n"
                        + "package dummy\n"
                        + "class ExampleSource\n"
                        + "      ^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 16 Char: 7 Error: ERR_VER_VER: Class 'ExampleTarget' is a version and has a version.\n"
                        + "package dummy\n"
                        + "class ExampleTarget\n"
                        + "      ^^^^^^^^^^^^^\n",
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void symmetricalAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    source: DummyClass[1..1];\n"
                + "    target: DummyClass[1..1];\n"
                + "    \n"
                + "    relationship this.id == DummyClass.id\n"
                + "}\n"
                + "\n"
                + "class DummyClass\n"
                + "{\n"
                + "    id: Long key;\n"
                + "}\n";
        //</editor-fold>

        String error = ""
                + "File: example.klass Line: 5 Char: 23 Error: ERR_ASO_SYM: Association 'DummyAssociation' is perfectly symmetrical, so foreign keys cannot be inferred. To break the symmetry, make one end owned, or make one end required and the other end optional.\n"
                + "package dummy\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    source: DummyClass[1..1];\n"
                + "                      ^^^^^^\n"
                + "    target: DummyClass[1..1];\n"
                + "                      ^^^^^^\n"
                + "}\n";

        this.assertCompilerErrors(sourceCodeText, error);
    }

    @Test
    public void duplicateNames()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "enumeration DuplicateTopLevelElement\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "}\n"
                + "\n"
                + "class DuplicateTopLevelElement\n"
                + "{\n"
                + "    duplicateMember: String key;\n"
                + "    duplicateMember: DuplicateTopLevelElement;\n"
                + "\n"
                + "    duplicateMember(duplicateParameter: String[1..1], duplicateParameter: String[1..1]): DuplicateTopLevelElement[1..1]\n"
                + "    {\n"
                + "        this.duplicateMember == duplicateParameter\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "association DuplicateTopLevelElement\n"
                + "{\n"
                + "    duplicateMember: DuplicateTopLevelElement[1..1];\n"
                + "    duplicateMember: DuplicateTopLevelElement[0..1];\n"
                + "}\n"
                + "\n"
                + "association DuplicateAssociationEnd\n"
                + "{\n"
                + "    duplicateAssociationEnd: DuplicateTopLevelElement[1..1];\n"
                + "    duplicateAssociationEnd: DuplicateTopLevelElement[0..1];\n"
                + "}\n"
                + "\n"
                + "projection DuplicateTopLevelElement on DuplicateTopLevelElement\n"
                + "{\n"
                + "    duplicateMember: \"Duplicate Header\",\n"
                + "    duplicateMember: \"Duplicate Header\",\n"
                + "}\n"
                + "\n"
                + "service DuplicateTopLevelElement\n"
                + "{\n"
                + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : DuplicateTopLevelElement;\n"
                + "        }\n"
                + "    /api/duplicate/duplicate/{different: String[1..1]}/{duplicate: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : DuplicateTopLevelElement;\n"
                + "        }\n"
                + "}";
        //</editor-fold>

        // TODO: sort compiler errors by source file name, line number
        // TODO: Duplicate duplicate errors
        // TODO: More error codes?

        String[] errors = {
                ""
                        + "File: example.klass Line: 3 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelElement'.\n"
                        + "package dummy\n"
                        + "enumeration DuplicateTopLevelElement\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 5 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                        + "package dummy\n"
                        + "enumeration DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 5 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                        + "package dummy\n"
                        + "enumeration DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 6 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                        + "package dummy\n"
                        + "enumeration DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 6 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                        + "package dummy\n"
                        + "enumeration DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 9 Char: 7 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelElement'.\n"
                        + "package dummy\n"
                        + "class DuplicateTopLevelElement\n"
                        + "      ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 11 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateMember'.\n"
                        + "package dummy\n"
                        + "class DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: String key;\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 12 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateMember'.\n"
                        + "package dummy\n"
                        + "class DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelElement;\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 14 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateMember'.\n"
                        + "package dummy\n"
                        + "class DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember(duplicateParameter: String[1..1], duplicateParameter: String[1..1]): DuplicateTopLevelElement[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 20 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelElement'.\n"
                        + "package dummy\n"
                        + "association DuplicateTopLevelElement\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 20 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'DuplicateTopLevelElement' must declare a relationship.\n"
                        + "package dummy\n"
                        + "association DuplicateTopLevelElement\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 22 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateMember'.\n"
                        + "package dummy\n"
                        + "association DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelElement[1..1];\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 23 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateMember'.\n"
                        + "package dummy\n"
                        + "association DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelElement[0..1];\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 26 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'DuplicateAssociationEnd' must declare a relationship.\n"
                        + "package dummy\n"
                        + "association DuplicateAssociationEnd\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 28 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateAssociationEnd'.\n"
                        + "package dummy\n"
                        + "association DuplicateAssociationEnd\n"
                        + "{\n"
                        + "    duplicateAssociationEnd: DuplicateTopLevelElement[1..1];\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 29 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'DuplicateTopLevelElement.duplicateAssociationEnd'.\n"
                        + "package dummy\n"
                        + "association DuplicateAssociationEnd\n"
                        + "{\n"
                        + "    duplicateAssociationEnd: DuplicateTopLevelElement[0..1];\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 32 Char: 12 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelElement'.\n"
                        + "package dummy\n"
                        + "projection DuplicateTopLevelElement on DuplicateTopLevelElement\n"
                        + "           ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 34 Char: 5 Error: ERR_DUP_PRJ: Duplicate member: 'duplicateMember'.\n"
                        + "package dummy\n"
                        + "projection DuplicateTopLevelElement on DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: \"Duplicate Header\",\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 35 Char: 5 Error: ERR_DUP_PRJ: Duplicate member: 'duplicateMember'.\n"
                        + "package dummy\n"
                        + "projection DuplicateTopLevelElement on DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    duplicateMember: \"Duplicate Header\",\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 38 Char: 9 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelElement'.\n"
                        + "package dummy\n"
                        + "service DuplicateTopLevelElement\n"
                        + "        ^^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 40 Char: 31 Error: ERR_DUP_PAR: Duplicate parameter: 'duplicate'.\n"
                        + "package dummy\n"
                        + "service DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                        + "                              ^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 40 Char: 57 Error: ERR_DUP_PAR: Duplicate parameter: 'duplicate'.\n"
                        + "package dummy\n"
                        + "service DuplicateTopLevelElement\n"
                        + "{\n"
                        + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                        + "                                                        ^^^^^^^^^\n"
                        + "}\n",
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void duplicateModifier()
    {
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key key;\n"
                + "}\n";

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void invalidNames()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package BADPACKAGE\n"
                + "\n"
                + "enumeration badEnumeration\n"
                + "{\n"
                + "    badEnumerationLiteral,\n"
                + "}\n"
                + "\n"
                + "class badClass\n"
                + "{\n"
                + "    BadPrimitiveProperty  : String key;\n"
                + "    BadEnumerationProperty: badEnumeration;\n"
                + "\n"
                + "    BadParameterizedProperty(BadParameter: String[1..1]): badClass[1..1]\n"
                + "    {\n"
                + "        this.BadPrimitiveProperty == BadParameter\n"
                + "    }\n"
                + "\n"
                + "    badTemporalProperty   : TemporalRange;\n"
                + "    badTemporalFrom       : TemporalInstant;\n"
                + "    badTemporalTo         : TemporalInstant;\n"
                + "    valid                 : String;\n"
                + "    validFrom             : String;\n"
                + "    validTo               : String;\n"
                + "    system                : String;\n"
                + "    systemFrom            : String;\n"
                + "    systemTo              : String;\n"
                + "}\n"
                + "\n"
                + "association badAssociation\n"
                + "{\n"
                + "    BadAssociationEndSource: badClass[1..1];\n"
                + "    BadAssociationEndTarget: badClass[0..1];\n"
                + "    \n"
                + "    relationship this.BadPrimitiveProperty == badClass.BadPrimitiveProperty\n"
                + "}\n"
                + "\n"
                + "projection badProjection on badClass\n"
                + "{\n"
                + "    BadPrimitiveProperty: \"Header\",\n"
                + "}\n"
                + "\n"
                + "service badClass\n"
                + "{\n"
                + "    /api/bad/{BadParameter: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : badProjection;\n"
                + "        }\n"
                + "}";
        //</editor-fold>

        // TODO: Package name still needs an error
        String[] errors = {
                ""
                        + "File: example.klass Line: 3 Char: 13 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][a-zA-Z0-9]*$ but was 'badEnumeration'.\n"
                        + "package BADPACKAGE\n"
                        + "enumeration badEnumeration\n"
                        + "            ^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 5 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$ but was 'badEnumerationLiteral'.\n"
                        + "package BADPACKAGE\n"
                        + "enumeration badEnumeration\n"
                        + "{\n"
                        + "    badEnumerationLiteral,\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 8 Char: 7 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][a-zA-Z0-9]*$ but was 'badClass'.\n"
                        + "package BADPACKAGE\n"
                        + "class badClass\n"
                        + "      ^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 10 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadPrimitiveProperty'.\n"
                        + "package BADPACKAGE\n"
                        + "class badClass\n"
                        + "{\n"
                        + "    BadPrimitiveProperty  : String key;\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 11 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadEnumerationProperty'.\n"
                        + "package BADPACKAGE\n"
                        + "class badClass\n"
                        + "{\n"
                        + "    BadEnumerationProperty: badEnumeration;\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 13 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadParameterizedProperty'.\n"
                        + "package BADPACKAGE\n"
                        + "class badClass\n"
                        + "{\n"
                        + "    BadParameterizedProperty(BadParameter: String[1..1]): badClass[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 13 Char: 30 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadParameter'.\n"
                        + "package BADPACKAGE\n"
                        + "class badClass\n"
                        + "{\n"
                        + "    BadParameterizedProperty(BadParameter: String[1..1]): badClass[1..1]\n"
                        + "                             ^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 29 Char: 13 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][a-zA-Z0-9]*$ but was 'badAssociation'.\n"
                        + "package BADPACKAGE\n"
                        + "association badAssociation\n"
                        + "            ^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 31 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadAssociationEndSource'.\n"
                        + "package BADPACKAGE\n"
                        + "association badAssociation\n"
                        + "{\n"
                        + "    BadAssociationEndSource: badClass[1..1];\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 32 Char: 5 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadAssociationEndTarget'.\n"
                        + "package BADPACKAGE\n"
                        + "association badAssociation\n"
                        + "{\n"
                        + "    BadAssociationEndTarget: badClass[0..1];\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 37 Char: 12 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][a-zA-Z0-9]*$ but was 'badProjection'.\n"
                        + "package BADPACKAGE\n"
                        + "projection badProjection on badClass\n"
                        + "           ^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 42 Char: 9 Error: ERR_NME_PAT: Name must match pattern ^[A-Z][a-zA-Z0-9]*$ but was 'badClass'.\n"
                        + "package BADPACKAGE\n"
                        + "service badClass\n"
                        + "        ^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 44 Char: 15 Error: ERR_NME_PAT: Name must match pattern ^[a-z][a-zA-Z0-9]*$ but was 'BadParameter'.\n"
                        + "package BADPACKAGE\n"
                        + "service badClass\n"
                        + "{\n"
                        + "    /api/bad/{BadParameter: String[1..1]}\n"
                        + "              ^^^^^^^^^^^^\n"
                        + "}\n",
        };
        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void unresolvedTypes()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedEnumerationProperty: UnresolvedEnumeration key;\n"
                + "\n"
                + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                + "    {\n"
                + "        this.unresolvedEnumerationProperty == UnresolvedEnumeration.unresolvedEnumerationLiteral\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "association AssociationWithUnresolved\n"
                + "{\n"
                + "    parent: UnresolvedClass[0..1];\n"
                + "    children: UnresolvedClass[0..*];\n"
                + "\n"
                + "    relationship this.unresolvedEnumerationProperty == UnresolvedClass.unresolvedEnumerationProperty\n"
                + "}\n"
                + "\n"
                + "projection EmptyProjection on ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedProjectionMember: \"Header\",\n"
                + "}\n"
                + "\n"
                + "projection ProjectionWithUnresolvedClass on UnresolvedClass\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "service ClassWithUnresolved\n"
                + "{\n"
                + "    /api/unresolved/{unresolvedParameterDeclaration: UnresolvedEnumeration[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : this.unresolvedEnumerationProperty == unresolvedParameter;\n"
                + "            projection  : UnresolvedProjection;\n"
                + "        }\n"
                + "    /api/fake1/\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : all;\n"
                + "            projection  : ProjectionWithUnresolvedClass;\n"
                + "        }\n"
                + "}\n"
                + "\n"
                + "service UnresolvedClass\n"
                + "{\n"
                + "    /api/fake/\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : all;\n"
                + "            projection  : EmptyProjection;\n"
                + "        }\n"
                + "}\n";
        //</editor-fold>

        // TODO: These compiler errors include the package name in the context. Many don't. When the filename is real, the package name doesn't really help. When the file is synthetic because it comes from a string or a compiler macro, the extra context may help.

        String[] errors = {
                ""
                        + "File: example.klass Line: 5 Char: 36 Error: ERR_ENM_PRP: Cannot find enumeration 'UnresolvedEnumeration'.\n"
                        + "package dummy\n"
                        + "class ClassWithUnresolved\n"
                        + "{\n"
                        + "    unresolvedEnumerationProperty: UnresolvedEnumeration key;\n"
                        + "                                   ^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 7 Char: 40 Error: ERR_PRP_TYP: Cannot find class 'UnresolvedClass'.\n"
                        + "package dummy\n"
                        + "class ClassWithUnresolved\n"
                        + "{\n"
                        + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                        + "                                       ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 15 Char: 13 Error: ERR_PRP_TYP: Cannot find class 'UnresolvedClass'.\n"
                        + "package dummy\n"
                        + "association AssociationWithUnresolved\n"
                        + "{\n"
                        + "    parent: UnresolvedClass[0..1];\n"
                        + "            ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 16 Char: 15 Error: ERR_PRP_TYP: Cannot find class 'UnresolvedClass'.\n"
                        + "package dummy\n"
                        + "association AssociationWithUnresolved\n"
                        + "{\n"
                        + "    children: UnresolvedClass[0..*];\n"
                        + "              ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 23 Char: 5 Error: ERR_PRJ_DTP: Cannot find member 'ClassWithUnresolved.unresolvedProjectionMember'.\n"
                        + "package dummy\n"
                        + "projection EmptyProjection on ClassWithUnresolved\n"
                        + "{\n"
                        + "    unresolvedProjectionMember: \"Header\",\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 26 Char: 45 Error: ERR_PRJ_TYP: Cannot find class 'UnresolvedClass'\n"
                        + "package dummy\n"
                        + "projection ProjectionWithUnresolvedClass on UnresolvedClass\n"
                        + "                                            ^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 32 Char: 54 Error: ERR_ENM_PAR: Cannot find enumeration 'UnresolvedEnumeration'.\n"
                        + "package dummy\n"
                        + "service ClassWithUnresolved\n"
                        + "{\n"
                        + "    /api/unresolved/{unresolvedParameterDeclaration: UnresolvedEnumeration[1..1]}\n"
                        + "                                                     ^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 36 Char: 65 Error: ERR_VAR_REF: Cannot find parameter 'unresolvedParameter'.\n"
                        + "package dummy\n"
                        + "service ClassWithUnresolved\n"
                        + "{\n"
                        + "    /api/unresolved/{unresolvedParameterDeclaration: UnresolvedEnumeration[1..1]}\n"
                        + "        GET\n"
                        + "        {\n"
                        + "            criteria    : this.unresolvedEnumerationProperty == unresolvedParameter;\n"
                        + "                                                                ^^^^^^^^^^^^^^^^^^^\n"
                        + "        }\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 37 Char: 27 Error: ERR_SER_PRJ: Cannot find projection 'UnresolvedProjection'\n"
                        + "package dummy\n"
                        + "service ClassWithUnresolved\n"
                        + "{\n"
                        + "    /api/unresolved/{unresolvedParameterDeclaration: UnresolvedEnumeration[1..1]}\n"
                        + "        GET\n"
                        + "        {\n"
                        + "            projection  : UnresolvedProjection;\n"
                        + "                          ^^^^^^^^^^^^^^^^^^^^\n"
                        + "        }\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 48 Char: 9 Error: ERR_SRG_TYP: Cannot find class 'UnresolvedClass'\n"
                        + "package dummy\n"
                        + "service UnresolvedClass\n"
                        + "        ^^^^^^^^^^^^^^^\n",
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void duplicateAssociationEndKeyword()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: Dummy[0..1];\n"
                + "    children: Dummy[0..*] owned owned;\n"
                + "\n"
                + "    relationship this.id == Dummy.id\n"
                + "}\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key;\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void manyOwnsOne()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key;\n"
                + "    parentId: Long private;\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: Dummy[0..1] owned;\n"
                + "    children: Dummy[0..*];\n"
                + "\n"
                + "    relationship this.id == Dummy.parentId\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation2\n"
                + "{\n"
                + "    children2: Dummy[0..*];\n"
                + "    parent2: Dummy[0..1] owned;\n"
                + "\n"
                + "    relationship this.parentId == Dummy.id\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: example.klass Line: 11 Char: 25 Error: ERR_OWN_ONE: Association end 'Dummy.children' is owned, but is on the to-one end of a one-to-many association.\n"
                        + "package dummy\n"
                        + "association DummyAssociation\n"
                        + "{\n"
                        + "    parent: Dummy[0..1] owned;\n"
                        + "                        ^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 20 Char: 26 Error: ERR_OWN_ONE: Association end 'Dummy.children2' is owned, but is on the to-one end of a many-to-one association.\n"
                        + "package dummy\n"
                        + "association DummyAssociation2\n"
                        + "{\n"
                        + "    parent2: Dummy[0..1] owned;\n"
                        + "                         ^^^^^\n"
                        + "}\n",
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void serviceErrors()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class DummyClass\n"
                + "{\n"
                + "    id: Long id key;\n"
                + "    parentId: Long private;\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: DummyClass[0..1];\n"
                + "    children: DummyClass[0..*];\n"
                + "\n"
                + "    relationship this.id == DummyClass.parentId\n"
                + "}\n"
                + "\n"
                + "projection DummyProjection on DummyClass\n"
                + "{\n"
                + "    id: \"Dummy ID\",\n"
                + "}\n"
                + "\n"
                + "service DummyClass\n"
                + "{\n"
                + "    /api/dummy/manyPathParam/{id: Long[1..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : this.id == 1;\n"
                + "            projection  : DummyProjection;\n"
                + "        }\n"
                + "    /api/equalMany?{id: Long[1..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : this.id == id;\n"
                + "            projection  : DummyProjection;\n"
                + "        }\n"
                + "    /api/inOne?{id: Long[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria    : this.id in id;\n"
                + "            projection  : DummyProjection;\n"
                + "        }\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Ignore("TODO: Implement projection parameterized properties")
    @Test
    public void errors()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.errors\n"
                + "\n"
                + "// TODO: Error annotators for all these errors\n"
                + "class DuplicateTopLevelDeclarationName\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "class DuplicateTopLevelDeclarationName\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "enumeration DuplicateTopLevelDeclarationName\n"
                + "{\n"
                + "    EXAMPLE_LITERAL,\n"
                + "}\n"
                + "\n"
                + "enumeration DuplicateTopLevelDeclarationName\n"
                + "{\n"
                + "    EXAMPLE_LITERAL,\n"
                + "}\n"
                + "\n"
                + "enumeration EmptyEnumeration\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "enumeration ExampleEnumeration\n"
                + "{\n"
                + "    EXAMPLE_LITERAL,\n"
                + "}\n"
                + "\n"
                + "enumeration DuplicateEnumerationLiteral\n"
                + "{\n"
                + "    DUPLICATE_LITERAL,\n"
                + "    DUPLICATE_LITERAL,\n"
                + "}\n"
                + "\n"
                + "class DuplicateMemberNames\n"
                + "{\n"
                + "    duplicateMemberName: String;\n"
                + "    duplicateMemberName: String;\n"
                + "    duplicateMemberName: ExampleEnumeration;\n"
                + "\n"
                + "    duplicateMemberName(): DuplicateMemberNames[1..1]\n"
                + "    {\n"
                + "        this.duplicateMemberName == DuplicateMemberNames.duplicateMemberName\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "class ExampleClassWithDuplicateAssociationEnd\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "association ExampleAssociationWithDuplicateAssociationEnd\n"
                + "{\n"
                + "    exampleClassWithDuplicateAssociationEnd: ExampleClassWithDuplicateAssociationEnd[1..1];\n"
                + "    exampleClassWithDuplicateAssociationEnd: ExampleClassWithDuplicateAssociationEnd[0..1];\n"
                + "}\n"
                + "\n"
                + "class ExampleClass\n"
                + "{\n"
                + "    integerProperty: Integer;\n"
                + "    longProperty: Long;\n"
                + "    stringProperty: String;\n"
                + "\n"
                + "    invalidParameterType(stringParameter: String[1..1]): ExampleClass[1..1]\n"
                + "    {\n"
                + "        this.integerProperty == ExampleClass.stringProperty\n"
                + "            && ExampleClass.integerProperty == stringParameter\n"
                + "    }\n"
                + "\n"
                + "    idParameterType(idParameter: Long[1..1]): ExampleClass[1..1]\n"
                + "    {\n"
                + "        this.longProperty  == idParameter\n"
                + "    }\n"
                + "\n"
                + "    invalidConstantType(): ExampleClass[1..1]\n"
                + "    {\n"
                + "        this.integerProperty == ExampleClass.integerProperty\n"
                + "            && ExampleClass.stringProperty == 1\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "projection ProjectionWithInvalidParameterType(invalidParameter: Integer[1..1]) on ExampleClass\n"
                + "{\n"
                + "    invalidParameterType(invalidParameter):\n"
                + "    {\n"
                + "        // Also this is empty which doesn't make a lot of sense\n"
                + "    },\n"
                + "}\n"
                + "\n"
                + "service ExampleClass\n"
                + "{\n"
                + "    /api/example/{invalidParameter: Boolean[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            // Better error messages for missing multiplicity, criteria, projection\n"
                + "            multiplicity: one;\n"
                + "            criteria: this.stringProperty == invalidParameter;\n"
                + "            projection: ProjectionWithInvalidParameterType(invalidParameter);\n"
                + "        }\n"
                + "    /api/example/singleParameterInClause?{id: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria: this.stringProperty in id;\n"
                + "\n"
                + "            // Also missing projection parameter\n"
                + "            projection: ProjectionWithInvalidParameterType;\n"
                + "        }\n"
                + "    // Duplicate urls\n"
                + "    /api/example/singleParameterInClause?{id: String[0..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            criteria: this.stringProperty == id;\n"
                + "            projection: ProjectionWithInvalidParameterType;\n"
                + "        }\n"
                + "    /api/example/{validParameter: Integer[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            // Better error messages for missing multiplicity, criteria, projection\n"
                + "            multiplicity: one;\n"
                + "            criteria: this.stringProperty == invalidParameter;\n"
                + "            projection: ProjectionWithInvalidParameterType(validParameter, validParameter);\n"
                + "        }\n"
                + "    /api/example/{validParameter: Integer[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            // Better error messages for missing multiplicity, criteria, projection\n"
                + "            multiplicity: one;\n"
                + "            criteria: this.stringProperty == invalidParameter;\n"
                + "            projection: ProjectionWithInvalidParameterType(invalidParameter);\n"
                + "        }\n"
                + "}\n"
                + "\n"
                + "projection EmptyProjection on ExampleClass\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "service ExampleClass\n"
                + "{\n"
                + "    // empty\n"
                + "}\n"
                + "\n"
                + "enumeration String\n"
                + "{\n"
                + "    DUMMY,\n"
                + "}\n"
                + "\n"
                + "enumeration ID\n"
                + "{\n"
                + "    DUMMY,\n"
                + "}\n"
                + "\n"
                + "class style\n"
                + "{\n"
                + "    Style: String;\n"
                + "}\n"
                + "\n"
                + "enumeration styleenum\n"
                + "{\n"
                + "    style,\n"
                + "}\n"
                + "\n"
                + "// TODO many owns one\n";
        //</editor-fold>

        String error = "";

        this.assertCompilerErrors(sourceCodeText, error);
    }

    @Test
    public void emoji()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.emoji\n"
                + "\n"
                + "// 😃\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    private void assertCompilerErrors(@Nonnull String sourceCodeText, String... expectedErrors)
    {
        DomainModel domainModel = this.compile(sourceCodeText);
        assertThat("Expected a compile error.", domainModel, nullValue());
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(true));
        ImmutableList<String> compilerErrors = this.compilerErrorHolder.getCompilerErrors().collect(CompilerError::toString);

    @Nonnull
    private String wrapSourceCode(String unwrappedSourceCode)
    {
        // https://stackoverflow.com/questions/11125459/java-regex-negative-lookahead
        return StringEscapeUtils.escapeJava(unwrappedSourceCode)
                .replaceAll("\\\\n(?!$)", "\\\\n\"\n                        + \"");
    }

    private void assertNoCompilerErrors(String sourceCodeText)
    {
        DomainModel           domainModel    = this.compile(sourceCodeText);
        ImmutableList<String> compilerErrors = this.compilerErrorHolder.getCompilerErrors().collect(CompilerError::toString);
    }

    private DomainModel compile(@Nonnull String sourceCodeText)
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        return this.compiler.compile(compilationUnit);
    }

    @Test
    public void serviceProjectionType()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.errors\n"
                + "\n"
                + "class Class1\n"
                + "{\n"
                + "    key: String key;\n"
                + "}\n"
                + "\n"
                + "class Class2\n"
                + "{\n"
                + "    key: String key;\n"
                + "}\n"
                + "\n"
                + "projection ExampleProjection on Class1\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "service Class2\n"
                + "{\n"
                + "    /api/example\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one;\n"
                + "            projection  : ExampleProjection;\n"
                + "        }\n"
                + "}";
        //</editor-fold>

        String error = ""
                + "File: example.klass Line: 23 Char: 27 Error: ERR_SRV_PRJ: Expected projection referencing 'Class2' but projection 'ExampleProjection' references 'Class1'.\n"
                + "package com.errors\n"
                + "service Class2\n"
                + "{\n"
                + "    /api/example\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : ExampleProjection;\n"
                + "                          ^^^^^^^^^^^^^^^^^\n"
                + "        }\n"
                + "}\n";

        this.assertCompilerErrors(sourceCodeText, error);
    }

    @Test
    public void duplicateVersionClass()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.errors\n"
                + "\n"
                + "class ExampleClass systemTemporal versioned\n"
                + "{\n"
                + "    id: Long key;\n"
                + "}\n"
                + "\n"
                + "class SecondClassVersion systemTemporal\n"
                + "{\n"
                + "    id: Long key;\n"
                // TODO: 🔢 Check that version properties are integers
                // TODO: 🔢 Check that a class has only one version property
                // TODO: 🔢 Check that version associationEnds point at classes which are versions
                + "    number: Integer version;\n"
                + "}\n"
                + "\n"
                + "association ExampleClassHasVersion\n"
                + "{\n"
                + "    exampleClass: ExampleClass[1..1];\n"
                // TODO: Check that version associationEnds are owned, or infer owned
                + "    version: SecondClassVersion[1..1] owned version;\n"
                + "\n"
                + "    relationship this.id == SecondClassVersion.id\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 3 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'ExampleClassHasVersion'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^\n",

                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 6 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'ExampleClass.version'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    version: ExampleClassVersion[1..1] owned version;\n"
                        + "    ^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 6 Char: 46 Error: ERR_VER_END: Multiple version properties on 'ExampleClass'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    version: ExampleClassVersion[1..1] owned version;\n"
                        + "                                             ^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 14 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'ExampleClassHasVersion'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^\n",

                ""
                        + "File: example.klass Line: 17 Char: 5 Error: ERR_DUP_PRP: Duplicate member: 'ExampleClass.version'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    version: SecondClassVersion[1..1] owned version;\n"
                        + "    ^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 17 Char: 45 Error: ERR_VER_END: Multiple version properties on 'ExampleClass'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    version: SecondClassVersion[1..1] owned version;\n"
                        + "                                            ^^^^^^^\n"
                        + "}\n",
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void duplicateVersionAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.errors\n"
                + "\n"
                + "class ExampleClass systemTemporal versioned\n"
                + "{\n"
                + "    id: Long id key;\n"
                + "}\n"
                + "\n"
                + "association ExampleClassVersionAgain\n"
                + "{\n"
                + "    duplicateExampleClass: ExampleClass[1..1];\n"
                + "    duplicateExampleClassVersion: ExampleClassVersion[1..1] owned version;\n"
                + "\n"
                + "    relationship this.id == ExampleClassVersion.id\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 5 Char: 5 Error: ERR_VER_END: Multiple versioned properties on 'ExampleClassVersion'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    exampleClass: ExampleClass[1..1];\n"
                        + "    ^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 6 Char: 46 Error: ERR_VER_END: Multiple version properties on 'ExampleClass'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassHasVersion\n"
                        + "{\n"
                        + "    version: ExampleClassVersion[1..1] owned version;\n"
                        + "                                             ^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 10 Char: 5 Error: ERR_VER_END: Multiple versioned properties on 'ExampleClassVersion'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassVersionAgain\n"
                        + "{\n"
                        + "    duplicateExampleClass: ExampleClass[1..1];\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 11 Char: 67 Error: ERR_VER_END: Multiple version properties on 'ExampleClass'.\n"
                        + "package com.errors\n"
                        + "association ExampleClassVersionAgain\n"
                        + "{\n"
                        + "    duplicateExampleClassVersion: ExampleClassVersion[1..1] owned version;\n"
                        + "                                                                  ^^^^^^^\n"
                        + "}\n"
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }
}
