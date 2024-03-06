package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.test.constants.KlassTestConstants;
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
                + "{\n"
                + "    name: String key\n"
                + "}\n"
                + "\n"
                + "enumeration DataType\n"
                + "{\n"
                + "    Boolean,\n"
                + "    Integer,\n"
                + "    Double,\n"
                + "    Float,\n"
                + "    Long,\n"
                + "    String,\n"
                + "    Instant,\n"
                + "    LocalDate,\n"
                + "}\n"
                + "\n"
                + "enumeration Multiplicity\n"
                + "{\n"
                + "    ZERO_TO_ONE,\n"
                + "    ONE_TO_ONE,\n"
                + "    ZERO_TO_MANY,\n"
                + "    ONE_TO_MANY,\n"
                + "}\n"
                + "\n"
                + "class DataTypeProperty\n"
                + "{\n"
                + "    className: String key\n"
                + "    name: String key\n"
                + "    dataType: DataType\n"
                + "    optional: Boolean\n"
                + "\n"
                + "    // key is a keyword, needs backticks to use as identifier\n"
                + "    key: Boolean\n"
                + "}\n"
                + "\n"
                + "association ClassHasDataTypeProperties\n"
                + "{\n"
                + "    class: Class[1..1]\n"
                + "    dataTypeProperties: DataTypeProperty[0..*]\n"
                + "\n"
                + "    relationship this.name == DataTypeProperty.className\n"
                + "}\n"
                + "\n"
                + "class Enumeration\n"
                + "{\n"
                + "    name: String key\n"
                + "}\n"
                + "\n"
                + "class EnumerationLiteral\n"
                + "{\n"
                + "    enumerationName: String key\n"
                + "    name: String key\n"
                + "}\n"
                + "\n"
                + "association EnumerationHasLiterals\n"
                + "{\n"
                + "    enumeration: Enumeration[1..1]\n"
                + "    enumerationLiterals: EnumerationLiteral[1..*]\n"
                + "\n"
                + "    relationship Enumeration.name == EnumerationLiteral.enumerationName\n"
                + "}\n"
                + "\n"
                + "class EnumerationProperty\n"
                + "{\n"
                + "    className: String key\n"
                + "    name: String key\n"
                + "    optional: Boolean key\n"
                + "    key: Boolean key\n"
                + "    enumerationName: String private\n"
                + "}\n"
                + "\n"
                + "association EnumerationPropertyHasEnumeration\n"
                + "{\n"
                + "    enumerationProperty: EnumerationProperty[0..*]\n"
                + "    enumeration: Enumeration[1..1]\n"
                + "\n"
                + "    relationship EnumerationProperty.enumerationName == Enumeration.name\n"
                + "}\n"
                + "\n"
                + "association ClassHasEnumerationProperties\n"
                + "{\n"
                + "    class: Class[1..1]\n"
                + "    enumerationProperties: EnumerationProperty[0..*]\n"
                + "\n"
                + "    relationship Class.name == EnumerationProperty.className\n"
                + "}\n"
                + "\n"
                + "enumeration AssociationEndDirection\n"
                + "{\n"
                + "    SOURCE(\"source\"),\n"
                + "    TARGET(\"target\"),\n"
                + "}\n"
                + "\n"
                + "class Association\n"
                + "{\n"
                + "    name: String key\n"
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
                + "{\n"
                + "    name: String\n"
                + "    associationName: String key\n"
                + "    direction: AssociationEndDirection key\n"
                + "    multiplicity: Multiplicity\n"
                + "}\n"
                + "\n"
                + "// simplification, ideally we'd model an association as having exactly two ends\n"
                + "association AssociationHasEnds\n"
                + "{\n"
                + "    association: Association[1..1]\n"
                + "    associationEnds: AssociationEnd[0..*] owned\n"
                + "\n"
                + "    relationship Association.name == AssociationEnd.associationName\n"
                + "}\n"
                + "\n"
                + "association AssociationEndHasResultTypeClass\n"
                + "{\n"
                + "    associationEndsResultTypeOf: AssociationEnd[0..*]\n"
                + "    resultType: Class[1..1]\n"
                + "}\n"
                + "\n"
                + "association ClassHasAssociationEnds\n"
                + "{\n"
                + "    owningType: Class[1..1]\n"
                + "    associationEnds: AssociationEnd[0..*]\n"
                + "}\n"
                + "\n"
                + "class ParameterizedProperty\n"
                + "{\n"
                + "    owningClassName: String key\n"
                + "    name: String key\n"
                + "    optional: Boolean\n"
                + "    key: Boolean\n"
                + "    private resultTypeName: String\n"
                + "}\n"
                + "\n"
                + "association ClassHasParameterizedProperties\n"
                + "{\n"
                + "    owningType: Class[1..1]\n"
                + "    parameterizedProperties: ParameterizedProperty[0..*] owned\n"
                + "\n"
                + "    relationship Class.name == ParameterizedProperty.owningClassName\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasResultType\n"
                + "{\n"
                + "    parameterizedProperty: ParameterizedProperty[0..*]\n"
                + "    resultType: Class[1..1]\n"
                + "\n"
                + "    relationship ParameterizedProperty.resultTypeName == Class.name\n"
                + "}\n"
                + "\n"
                + "class AssociationEndOrderBy\n"
                + "{\n"
                + "    associationName: String key private\n"
                + "    name: String\n"
                + "    multiplicity: Multiplicity\n"
                + "    direction: AssociationEndDirection key\n"
                + "    orderById: Long private\n"
                + "}\n"
                + "\n"
                + "class ParameterizedPropertyOrderBy\n"
                + "{\n"
                + "    owningClassName: String private key\n"
                + "    name: String key\n"
                + "    orderById: Long private\n"
                + "}\n"
                + "\n"
                + "association AssociationEndHasOrderBy\n"
                + "{\n"
                + "    associationEnd: AssociationEnd[1..1]\n"
                + "    associationEndOrderBy: AssociationEndOrderBy[0..1]\n"
                + "\n"
                + "    relationship AssociationEnd.associationName == AssociationEndOrderBy.associationName\n"
                + "            && AssociationEnd.direction == AssociationEndOrderBy.direction\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasOrderBy\n"
                + "{\n"
                + "    parameterizedProperty: ParameterizedProperty[1..1]\n"
                + "    parameterizedPropertyOrderBy: ParameterizedPropertyOrderBy[0..1]\n"
                + "\n"
                + "    relationship ParameterizedProperty.owningClassName == ParameterizedPropertyOrderBy.owningClassName\n"
                + "            && ParameterizedProperty.name == ParameterizedPropertyOrderBy.name\n"
                + "}\n"
                + "\n"
                + "association AssociationEndOrderByHasParts\n"
                + "{\n"
                + "    associationEndOrderBy: AssociationEndOrderBy[1..1]\n"
                + "    orderBy: OrderBy[1..*]\n"
                + "\n"
                + "    relationship AssociationEndOrderBy.orderById == OrderBy.id\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyOrderByHasParts\n"
                + "{\n"
                + "    parameterizedPropertyOrderBy: ParameterizedPropertyOrderBy[1..1]\n"
                + "    orderBy: OrderBy[1..*]\n"
                + "\n"
                + "    relationship ParameterizedPropertyOrderBy.orderById == OrderBy.id\n"
                + "}\n"
                + "\n"
                + "class OrderBy\n"
                + "{\n"
                + "    id: Long id key\n"
                + "}\n"
                + "\n"
                + "// Split into data type / enum type order bys?\n"
                + "class OrderByPart\n"
                + "{\n"
                + "    orderById: Long key\n"
                + "\n"
                + "    // TODO: order keyword for properties meant for sorting?\n"
                + "    ordinal: Integer key\n"
                + "    className: String private\n"
                + "    propertyName: String private\n"
                + "    direction: OrderByDirection\n"
                + "}\n"
                + "\n"
                + "association OrderByHasParts\n"
                + "{\n"
                + "    orderBy: OrderBy[1..1]\n"
                + "    orderByParts: OrderByPart[1..*]\n"
                + "        orderBy: this.ordinal\n"
                + "\n"
                + "    relationship OrderBy.id == OrderByPart.orderById\n"
                + "}\n"
                + "\n"
                + "enumeration OrderByDirection\n"
                + "{\n"
                + "    ASCENDING(\"ascending\"),\n"
                + "    DESCENDING(\"descending\"),\n"
                + "}\n"
                + "\n"
                + "association OrderByPartHasDataTypeProperty\n"
                + "{\n"
                + "    orderByPart: OrderByPart[0..*]\n"
                + "    dataTypeProperty: DataTypeProperty[1..1]\n"
                + "\n"
                + "    relationship OrderByPart.className == DataTypeProperty.className\n"
                + "            && OrderByPart.propertyName == DataTypeProperty.name\n"
                + "}\n"
                + "\n"
                + "association OrderByPartHasEnumerationProperty\n"
                + "{\n"
                + "    orderByPart: OrderByPart[0..*]\n"
                + "    enumerationProperty: EnumerationProperty[1..1]\n"
                + "\n"
                + "    relationship OrderByPart.className == EnumerationProperty.className\n"
                + "            && OrderByPart.propertyName == EnumerationProperty.name\n"
                + "}\n"
                + "\n"
                + "association ParameterizedPropertyHasParameters\n"
                + "{\n"
                + "    parameterizedProperty: ParameterizedProperty[1..1]\n"
                + "    parameters: Parameter[0..*]\n"
                + "}\n"
                + "\n"
                + "class Parameter\n"
                + "{\n"
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
                + "{\n"
                + "    id: Long key\n"
                + "    name: String\n"
                + "    parentId: Long? private\n"
                + "}\n"
                + "\n"
                + "association PackageHasChildPackages\n"
                + "{\n"
                + "    package: Package[0..1]\n"
                + "    childPackages: Package[0..*]\n"
                + "\n"
                + "    relationship this.id == Package.parentId\n"
                + "}\n"
                + "\n"
                + "association PackageHasClasses\n"
                + "{\n"
                + "    package: Package[1..1]\n"
                + "    classes: Class[0..*]\n"
                + "}\n"
                + "\n"
                + "association PackageHasEnumerations\n"
                + "{\n"
                + "    package: Package[1..1]\n"
                + "    enumerations: Enumeration[0..*]\n"
                + "}\n"
                + "\n"
                + "association PackageHasAssociations\n"
                + "{\n"
                + "    package: Package[1..1]\n"
                + "    associations: Association[0..*]\n"
                + "}\n";
        //</editor-fold>

        String error1 = ""
                + "File: example.klass Line: 131 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'AssociationEndHasResultTypeClass' must declare a relationship.\n"
                + "association AssociationEndHasResultTypeClass\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";
        String error2 = ""
                + "File: example.klass Line: 137 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'ClassHasAssociationEnds' must declare a relationship.\n"
                + "association ClassHasAssociationEnds\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^^^\n";
        String error3 = ""
                + "File: example.klass Line: 268 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'ParameterizedPropertyHasParameters' must declare a relationship.\n"
                + "association ParameterizedPropertyHasParameters\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";
        String error4 = ""
                + "File: example.klass Line: 300 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'PackageHasClasses' must declare a relationship.\n"
                + "association PackageHasClasses\n"
                + "            ^^^^^^^^^^^^^^^^^\n";
        String error5 = ""
                + "File: example.klass Line: 306 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'PackageHasEnumerations' must declare a relationship.\n"
                + "association PackageHasEnumerations\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^^\n";
        String error6 = ""
                + "File: example.klass Line: 312 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'PackageHasAssociations' must declare a relationship.\n"
                + "association PackageHasAssociations\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^^\n";

        this.assertCompilerErrors(sourceCodeText, error1, error2, error3, error4, error5, error6);
    }

    @Test
    public void doubleOwnedAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "association DoubleOwnedAssociation\n"
                + "{\n"
                + "    source: DoubleOwnedClass[1..1] owned\n"
                + "    target: DoubleOwnedClass[1..1] owned\n"
                + "    \n"
                + "    relationship this.id == DoubleOwnedClass.id\n"
                + "}\n"
                + "\n"
                + "class DoubleOwnedClass\n"
                + "{\n"
                + "    id: Long key\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void duplicateNames()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "}\n"
                + "\n"
                + "class DuplicateTopLevelItem\n"
                + "{\n"
                + "    duplicateMember: String\n"
                + "    duplicateMember: DuplicateTopLevelItem\n"
                + "\n"
                + "    duplicateMember(duplicateParameter: String[1..1], duplicateParameter: String[1..1]): DuplicateTopLevelItem[1..1]\n"
                + "    {\n"
                + "        this.duplicateMember == duplicateParameter\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "association DuplicateTopLevelItem\n"
                + "{\n"
                + "    duplicateMember: DuplicateTopLevelItem[1..1]\n"
                + "    duplicateMember: DuplicateTopLevelItem[1..1]\n"
                + "}\n"
                + "\n"
                + "association DuplicateAssociationEnd\n"
                + "{\n"
                + "    duplicateAssociationEnd: DuplicateTopLevelItem[1..1]\n"
                + "    duplicateAssociationEnd: DuplicateTopLevelItem[1..1]\n"
                + "}\n"
                + "\n"
                + "projection DuplicateTopLevelItem on DuplicateTopLevelItem\n"
                + "{\n"
                + "    duplicateMember: \"Duplicate Header\",\n"
                + "    duplicateMember: \"Duplicate Header\",\n"
                + "}\n"
                + "\n"
                + "service DuplicateTopLevelItem\n"
                + "{\n"
                + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : DuplicateTopLevelItem\n"
                + "        }\n"
                + "    /api/duplicate/duplicate/{different: String[1..1]}/{duplicate: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : DuplicateTopLevelItem\n"
                + "        }\n"
                + "}";
        //</editor-fold>

        // TODO: sort compiler errors by source file name, line number
        // TODO: Duplicate duplicate errors
        // TODO: More error codes?

        String[] expectedErrors = {
                ""
                        + "File: example.klass Line: 3 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                        + "enumeration DuplicateTopLevelItem\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 5 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                        + "enumeration DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 5 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                        + "enumeration DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 6 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                        + "enumeration DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 6 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                        + "enumeration DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                        + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 9 Char: 7 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                        + "class DuplicateTopLevelItem\n"
                        + "      ^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 11 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                        + "class DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: String\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 12 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                        + "class DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelItem\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 20 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                        + "association DuplicateTopLevelItem\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 20 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'DuplicateTopLevelItem' must declare a relationship.\n"
                        + "association DuplicateTopLevelItem\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 22 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                        + "association DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelItem[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 23 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                        + "association DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: DuplicateTopLevelItem[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 26 Char: 13 Error: ERR_REL_INF: Relationship inference not yet supported. 'DuplicateAssociationEnd' must declare a relationship.\n"
                        + "association DuplicateAssociationEnd\n"
                        + "            ^^^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 28 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateAssociationEnd'.\n"
                        + "association DuplicateAssociationEnd\n"
                        + "{\n"
                        + "    duplicateAssociationEnd: DuplicateTopLevelItem[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 29 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateAssociationEnd'.\n"
                        + "association DuplicateAssociationEnd\n"
                        + "{\n"
                        + "    duplicateAssociationEnd: DuplicateTopLevelItem[1..1]\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 32 Char: 12 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                        + "projection DuplicateTopLevelItem on DuplicateTopLevelItem\n"
                        + "           ^^^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 34 Char: 5 Error: ERR_DUP_PRJ: Duplicate member: 'duplicateMember'.\n"
                        + "projection DuplicateTopLevelItem on DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: \"Duplicate Header\",\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 35 Char: 5 Error: ERR_DUP_PRJ: Duplicate member: 'duplicateMember'.\n"
                        + "projection DuplicateTopLevelItem on DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    duplicateMember: \"Duplicate Header\",\n"
                        + "    ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 40 Char: 31 Error: ERR_DUP_PAR: Duplicate parameter: 'duplicate'.\n"
                        + "service DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                        + "                              ^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 40 Char: 57 Error: ERR_DUP_PAR: Duplicate parameter: 'duplicate'.\n"
                        + "service DuplicateTopLevelItem\n"
                        + "{\n"
                        + "    /api/duplicate/duplicate/{duplicate: String[1..1]}/{duplicate: String[1..1]}\n"
                        + "                                                        ^^^^^^^^^\n"
                        + "}\n",
        };

        this.assertCompilerErrors(
                sourceCodeText,
                expectedErrors);
    }

    @Test
    public void duplicatePropertyKeyword()
    {
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key key\n"
                + "}\n";

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void invalidNames()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package BADPACKAGE\n"
                + "\n"
                + "enumeration badEnumeration\n"
                + "{\n"
                + "    badEnumerationLiteral,\n"
                + "}\n"
                + "\n"
                + "class badClass\n"
                + "{\n"
                + "    BadPrimitiveProperty  : String\n"
                + "    BadEnumerationProperty: badEnumeration\n"
                + "\n"
                + "    BadParameterizedProperty(BadParameter: String[1..1]): badClass[1..1]\n"
                + "    {\n"
                + "        this.BadPrimitiveProperty == BadParameter\n"
                + "    }\n"
                + "\n"
                + "    badTemporalProperty   : TemporalRange\n"
                + "    badTemporalFrom       : TemporalInstant\n"
                + "    badTemporalTo         : TemporalInstant\n"
                + "    valid                 : String\n"
                + "    validFrom             : String\n"
                + "    validTo               : String\n"
                + "    system                : String\n"
                + "    systemFrom            : String\n"
                + "    systemTo              : String\n"
                + "}\n"
                + "\n"
                + "association badAssociation\n"
                + "{\n"
                + "    BadAssociationEndSource: badClass[1..1]\n"
                + "    BadAssociationEndTarget: badClass[1..1]\n"
                + "    \n"
                + "    relationship this.BadPrimitiveProperty == badClass.BadPrimitiveProperty\n"
                + "}\n"
                + "\n"
                + "projection badProjection on badClass\n"
                + "{\n"
                + "    BadPrimitiveProperty: \"Header\",\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void unresolvedTypes()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "class ClassWithUnresolved versions(UnresolvedVersioned)\n"
                + "{\n"
                + "    unresolvedEnumerationProperty: UnresolvedEnumeration\n"
                + "\n"
                + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                + "    {\n"
                + "        this.unresolvedEnumerationProperty == UnresolvedEnumeration.unresolvedEnumerationLiteral\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "association AssociationWithUnresolved versions(UnresolvedVersioned)\n"
                + "{\n"
                + "    parent: UnresolvedClass[0..1]\n"
                + "    children: UnresolvedClass[0..*]\n"
                + "\n"
                + "    relationship this.unresolvedEnumerationProperty == UnresolvedClass.unresolvedEnumerationProperty\n"
                + "}\n"
                + "\n"
                + "projection EmptyProjection on ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedProjectionMember: \"Header\",\n"
                + "}\n"
                + "\n"
                + "service ClassWithUnresolved\n"
                + "{\n"
                + "    /api/unresolved/{id: Long[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.unresolvedEnumerationProperty == unresolvedParameter\n"
                + "            projection  : EmptyProjection\n"
                + "        }\n"
                + "}\n";
        //</editor-fold>

        // TODO: These compiler errors include the package name in the context. Many don't. When the filename is real, the package name doesn't really help. When the file is synthetic because it comes from a string or a compiler macro, the extra context may help.

        String[] errors = {
                ""
                        + "File: example.klass Line: 3 Char: 36 Error: Cannot find class 'UnresolvedVersioned'\n"
                        + "package dummy\n"
                        + "class ClassWithUnresolved versions(UnresolvedVersioned)\n"
                        + "                                   ^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 5 Char: 36 Error: Cannot find enumeration 'UnresolvedEnumeration'\n"
                        + "package dummy\n"
                        + "class ClassWithUnresolved versions(UnresolvedVersioned)\n"
                        + "{\n"
                        + "    unresolvedEnumerationProperty: UnresolvedEnumeration\n"
                        + "                                   ^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 7 Char: 40 Error: Cannot find class 'UnresolvedClass'\n"
                        + "package dummy\n"
                        + "class ClassWithUnresolved versions(UnresolvedVersioned)\n"
                        + "{\n"
                        + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                        + "                                       ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 13 Char: 48 Error: Cannot find class 'UnresolvedVersioned'\n"
                        + "package dummy\n"
                        + "association AssociationWithUnresolved versions(UnresolvedVersioned)\n"
                        + "                                               ^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 15 Char: 13 Error: ERR_ASO_TYP: Cannot find class 'UnresolvedClass'.\n"
                        + "package dummy\n"
                        + "association AssociationWithUnresolved versions(UnresolvedVersioned)\n"
                        + "{\n"
                        + "    parent: UnresolvedClass[0..1]\n"
                        + "            ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 16 Char: 15 Error: ERR_ASO_TYP: Cannot find class 'UnresolvedClass'.\n"
                        + "package dummy\n"
                        + "association AssociationWithUnresolved versions(UnresolvedVersioned)\n"
                        + "{\n"
                        + "    children: UnresolvedClass[0..*]\n"
                        + "              ^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 23 Char: 5 Error: Cannot find member 'ClassWithUnresolved.unresolvedProjectionMember'.\n"
                        + "projection EmptyProjection on ClassWithUnresolved\n"
                        + "{\n"
                        + "    unresolvedProjectionMember: \"Header\",\n"
                        + "    ^^^^^^^^^^^^^^^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 32 Char: 65 Error: ERR_VAR_REF: Cannot find parameter 'unresolvedParameter'.\n"
                        + "service ClassWithUnresolved\n"
                        + "{\n"
                        + "    /api/unresolved/{id: Long[1..1]}\n"
                        + "        GET\n"
                        + "        {\n"
                        + "            criteria    : this.unresolvedEnumerationProperty == unresolvedParameter\n"
                        + "                                                                ^^^^^^^^^^^^^^^^^^^\n"
                        + "        }\n"
                        + "}\n"
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void duplicateAssociationEndKeyword()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: Dummy[0..1] owned owned\n"
                + "    children: Dummy[0..*] owned owned\n"
                + "\n"
                + "    relationship this.id == Dummy.id\n"
                + "}\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void manyOwnsOne()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    id: Long id key\n"
                + "    parentId: Long private\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: Dummy[0..1] owned\n"
                + "    children: Dummy[0..*]\n"
                + "\n"
                + "    relationship this.id == Dummy.parentId\n"
                + "}\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void serviceErrors()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "class DummyClass\n"
                + "{\n"
                + "    id: Long id key\n"
                + "    parentId: Long private\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    parent: DummyClass[0..1] owned\n"
                + "    children: DummyClass[0..*]\n"
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
                + "            multiplicity: one\n"
                + "            criteria    : this.id == 1\n"
                + "            projection  : DummyProjection\n"
                + "        }\n"
                + "    /api/equalMany?{id: Long[1..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id == id\n"
                + "            projection  : DummyProjection\n"
                + "        }\n"
                + "    /api/inOne?{id: Long[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id in id\n"
                + "            projection  : DummyProjection\n"
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
        String sourceCodeText = "package com.errors\n"
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
                + "    duplicateMemberName: String\n"
                + "    duplicateMemberName: String\n"
                + "    duplicateMemberName: ExampleEnumeration\n"
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
                + "    exampleClassWithDuplicateAssociationEnd: ExampleClassWithDuplicateAssociationEnd[1..1]\n"
                + "    exampleClassWithDuplicateAssociationEnd: ExampleClassWithDuplicateAssociationEnd[1..1]\n"
                + "}\n"
                + "\n"
                + "class ExampleClass\n"
                + "{\n"
                + "    integerProperty: Integer\n"
                + "    longProperty: Long\n"
                + "    stringProperty: String\n"
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
                + "            multiplicity: one\n"
                + "            criteria: this.stringProperty == invalidParameter\n"
                + "            projection: ProjectionWithInvalidParameterType(invalidParameter)\n"
                + "        }\n"
                + "    /api/example/singleParameterInClause?{id: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria: this.stringProperty in id\n"
                + "\n"
                + "            // Also missing projection parameter\n"
                + "            projection: ProjectionWithInvalidParameterType\n"
                + "        }\n"
                + "    // Duplicate urls\n"
                + "    /api/example/singleParameterInClause?{id: String[0..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria: this.stringProperty == id\n"
                + "            projection: ProjectionWithInvalidParameterType\n"
                + "        }\n"
                + "    /api/example/{validParameter: Integer[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            // Better error messages for missing multiplicity, criteria, projection\n"
                + "            multiplicity: one\n"
                + "            criteria: this.stringProperty == invalidParameter\n"
                + "            projection: ProjectionWithInvalidParameterType(validParameter, validParameter)\n"
                + "        }\n"
                + "    /api/example/{validParameter: Integer[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            // Better error messages for missing multiplicity, criteria, projection\n"
                + "            multiplicity: one\n"
                + "            criteria: this.stringProperty == invalidParameter\n"
                + "            projection: ProjectionWithInvalidParameterType(invalidParameter)\n"
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
                + "    Style: String\n"
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
        String sourceCodeText = "package com.emoji\n"
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

        assertThat(compilerErrors, is(Lists.immutable.with(expectedErrors)));
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
                + "}\n"
                + "\n"
                + "class Class2\n"
                + "{\n"
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
                + "            multiplicity: one\n"
                + "            projection  : ExampleProjection\n"
                + "        }\n"
                + "}";
        //</editor-fold>

        String error = ""
                + "File: example.klass Line: 21 Char: 27 Error: Expected projection referencing 'Class2' but projection 'ExampleProjection' references 'Class1'.\n"
                + "service Class2\n"
                + "{\n"
                + "    /api/example\n"
                + "        GET\n"
                + "        {\n"
                + "            projection  : ExampleProjection\n"
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
        String sourceCodeText = "package com.errors\n"
                + "\n"
                + "class ExampleClass systemTemporal versioned\n"
                + "{\n"
                + "}\n"
                + "\n"
                + "class SecondClassVersion systemTemporal versions(ExampleClass)\n"
                + "{\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: VersionClassInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 3 Char: 51 Error: ERR_VER_CLS: Multiple version classes on 'ExampleClass'.\n"
                        + "class ExampleClassVersion systemTemporal versions(ExampleClass)\n"
                        + "                                                  ^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 7 Char: 50 Error: ERR_VER_CLS: Multiple version classes on 'ExampleClass'.\n"
                        + "class SecondClassVersion systemTemporal versions(ExampleClass)\n"
                        + "                                                 ^^^^^^^^^^^^\n"
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }

    @Test
    public void duplicateVersionAssociation()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "package com.errors\n"
                + "\n"
                + "class ExampleClass systemTemporal versioned\n"
                + "{\n"
                + "    id: Long id key\n"
                + "}\n"
                + "\n"
                + "association ExampleClassVersionAgain versions(ExampleClassVersion)\n"
                + "{\n"
                + "    exampleClass: ExampleClass[1..1]\n"
                + "    exampleClassVersion: ExampleClassVersion[1..1]\n"
                + "\n"
                + "    relationship this.id == ExampleClassVersion.id\n"
                + "}\n";
        //</editor-fold>

        String[] errors = {
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 3 Char: 45 Error: ERR_MUL_VER_ASSO: Multiple version associations on 'ExampleClassVersion'.\n"
                        + "association ExampleClassHasVersion versions(ExampleClassVersion)\n"
                        + "                                            ^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: VersionAssociationInferencePhase compiler macro (File: example.klass Line: 3 Char: 35) Line: 5 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'exampleClass'.\n"
                        + "association ExampleClassHasVersion versions(ExampleClassVersion)\n"
                        + "{\n"
                        + "    exampleClass: ExampleClass[1..1]\n"
                        + "    ^^^^^^^^^^^^\n"
                        + "}\n",
                ""
                        + "File: example.klass Line: 8 Char: 47 Error: ERR_MUL_VER_ASSO: Multiple version associations on 'ExampleClassVersion'.\n"
                        + "association ExampleClassVersionAgain versions(ExampleClassVersion)\n"
                        + "                                              ^^^^^^^^^^^^^^^^^^^\n",
                ""
                        + "File: example.klass Line: 10 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'exampleClass'.\n"
                        + "association ExampleClassVersionAgain versions(ExampleClassVersion)\n"
                        + "{\n"
                        + "    exampleClass: ExampleClass[1..1]\n"
                        + "    ^^^^^^^^^^^^\n"
                        + "}\n"
        };

        this.assertCompilerErrors(sourceCodeText, errors);
    }
}
