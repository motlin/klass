package cool.klass.model.converter.compiler;

import java.util.Set;
import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class KlassCompilerTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassCompilerTest.class);

    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

    private final CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
    private final KlassCompiler       compiler            = new KlassCompiler(this.compilerErrorHolder);

    @Test
    public void compile()
    {
        // TODO: Just create the error holder inside the constructor?

        Set<String> klassLocations = this.getResourceNames("com.test");

        DomainModel domainModel = this.compiler.compile(klassLocations);
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());
    }

    protected Set<String> getResourceNames(String packageName)
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(DOT_PATTERN.matcher(packageName).replaceAll("/")))
                .setScanners(new ResourcesScanner()).filterInputsBy(path -> path.startsWith(packageName)));

        return reflections.getResources(Pattern.compile(".*\\.klass"));
    }

    @Test
    public void errors()
    {
        Set<String> klassLocations = this.getResourceNames("errors");
        this.compiler.compile(klassLocations);
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(true));
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(true));
        // TODO assertions
    }

    @Test
    public void doubleOwnedAssociation()
    {
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "association DoubleOwnedAssociation\n"
                + "{\n"
                + "    owned source: DoubleOwnedClass[1..1]\n"
                + "    owned target: DoubleOwnedClass[1..1]\n"
                + "}\n"
                + "\n"
                + "class DoubleOwnedClass\n"
                + "{\n"
                + "}\n";

        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel     domainModel     = this.compiler.compile(compilationUnit);
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());
    }

    public void assertCompilerErrors(String sourceCodeText, String... expectedErrors)
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel     domainModel     = this.compiler.compile(compilationUnit);
        assertThat(domainModel, nullValue());
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(true));
        ImmutableList<String> compilerErrors = this.compilerErrorHolder.getCompilerErrors().collect(CompilerError::toString);

        assertThat(compilerErrors, is(Lists.immutable.with(expectedErrors)));
    }

    @Test
    public void duplicateNames()
    {
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
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
                + "}\n";

        // TODO: sort compiler errors by source file name, line number
        // TODO: Duplicate duplicate errors
        // TODO: More error codes?
        String error1 = ""
                + "File: example.klass Line: 3 Char: 13 Error: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "package dummy\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^\n"
                + "";
        String error2 = ""
                + "File: example.klass Line: 3 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^\n";
        String error3 = ""
                + "File: example.klass Line: 5 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error4 = ""
                + "File: example.klass Line: 5 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error5 = ""
                + "File: example.klass Line: 6 Char: 5 Error: ERR_DUP_ENM: Duplicate enumeration literal: 'DUPLICATE_ENUM_LITERAL'.\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "    ^^^^^^^^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error6 = ""
                + "File: example.klass Line: 6 Char: 28 Error: ERR_DUP_LIT: Duplicate enumeration pretty name: 'Duplicate pretty name'.\n"
                + "enumeration DuplicateTopLevelItem\n"
                + "{\n"
                + "    DUPLICATE_ENUM_LITERAL(\"Duplicate pretty name\"),\n"
                + "                           ^^^^^^^^^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error7 = ""
                + "File: example.klass Line: 9 Char: 7 Error: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "package dummy\n"
                + "class DuplicateTopLevelItem\n"
                + "      ^^^^^^^^^^^^^^^^^^^^^\n";
        String error8 = ""
                + "File: example.klass Line: 9 Char: 7 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "class DuplicateTopLevelItem\n"
                + "      ^^^^^^^^^^^^^^^^^^^^^\n";
        String error9 = ""
                + "File: example.klass Line: 11 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                + "class DuplicateTopLevelItem\n"
                + "{\n"
                + "    duplicateMember: String\n"
                + "    ^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error10 = ""
                + "File: example.klass Line: 12 Char: 5 Error: ERR_DUP_MEM: Duplicate member: 'duplicateMember'.\n"
                + "class DuplicateTopLevelItem\n"
                + "{\n"
                + "    duplicateMember: DuplicateTopLevelItem\n"
                + "    ^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error11 = ""
                + "File: example.klass Line: 20 Char: 13 Error: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "package dummy\n"
                + "association DuplicateTopLevelItem\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^\n";
        String error12 = ""
                + "File: example.klass Line: 20 Char: 13 Error: ERR_DUP_TOP: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "association DuplicateTopLevelItem\n"
                + "            ^^^^^^^^^^^^^^^^^^^^^\n";
        String error13 = ""
                + "File: example.klass Line: 32 Char: 12 Error: Duplicate top level item name: 'DuplicateTopLevelItem'.\n"
                + "package dummy\n"
                + "projection DuplicateTopLevelItem on DuplicateTopLevelItem\n"
                + "           ^^^^^^^^^^^^^^^^^^^^^\n";

        this.assertCompilerErrors(
                sourceCodeText,
                error1,
                error2,
                error3,
                error4,
                error5,
                error6,
                error7,
                error8,
                error9,
                error10,
                error11,
                error12,
                error13);
    }

    @Test
    public void duplicatePropertyKeyword()
    {
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    key key id: ID\n"
                + "}\n";

        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel     domainModel     = this.compiler.compile(compilationUnit);
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());
    }

    @Test
    public void invalidNames()
    {
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
                + "}\n"
                + "\n"
                + "projection badProjection on badClass\n"
                + "{\n"
                + "    BadPrimitiveProperty: \"Header\",\n"
                + "}\n";

        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel     domainModel     = this.compiler.compile(compilationUnit);
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());
    }

    @Test
    public void unresolvedTypes()
    {
        //language=Klass
        String sourceCodeText = ""
                + "package dummy\n"
                + "\n"
                + "class ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedEnumerationProperty: UnresolvedEnumeration\n"
                + "\n"
                + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                + "    {\n"
                + "        this.unresolvedEnumerationProperty == UnresolvedEnumeration.unresolvedEnumerationLiteral\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "association AssociationWithUnresolved\n"
                + "{\n"
                + "    parent: UnresolvedClass[0..1]\n"
                + "    children: UnresolvedClass[0..*]\n"
                + "}\n";

        String error1 = ""
                + "File: example.klass Line: 5 Char: 36 Error: Cannot find enumeration 'UnresolvedEnumeration'\n"
                + "package dummy\n"
                + "class ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedEnumerationProperty: UnresolvedEnumeration\n"
                + "                                   ^^^^^^^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error2 = ""
                + "File: example.klass Line: 7 Char: 40 Error: Cannot find class 'UnresolvedClass'\n"
                + "package dummy\n"
                + "class ClassWithUnresolved\n"
                + "{\n"
                + "    unresolvedParameterizedProperty(): UnresolvedClass[1..1]\n"
                + "                                       ^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error3 = ""
                + "File: example.klass Line: 15 Char: 13 Error: Cannot find class 'UnresolvedClass'\n"
                + "package dummy\n"
                + "association AssociationWithUnresolved\n"
                + "{\n"
                + "    parent: UnresolvedClass[0..1]\n"
                + "            ^^^^^^^^^^^^^^^\n"
                + "}\n";
        String error4 = ""
                + "File: example.klass Line: 16 Char: 15 Error: Cannot find class 'UnresolvedClass'\n"
                + "package dummy\n"
                + "association AssociationWithUnresolved\n"
                + "{\n"
                + "    children: UnresolvedClass[0..*]\n"
                + "              ^^^^^^^^^^^^^^^\n"
                + "}\n";

        this.assertCompilerErrors(sourceCodeText, error1, error2, error3, error4);
    }

    @Test
    public void duplicateAssociationEndKeyword()
    {
        //language=Klass
        String sourceCodeText = "package dummy\n"
                + "\n"
                + "class Dummy\n"
                + "{\n"
                + "    key id: ID\n"
                + "}\n"
                + "\n"
                + "association DummyAssociation\n"
                + "{\n"
                + "    owned owned parent: Dummy[0..1]\n"
                + "    owned owned children: Dummy[0..*]\n"
                + "}";

        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel     domainModel     = this.compiler.compile(compilationUnit);
        for (CompilerError compilerError : this.compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());
    }
}
