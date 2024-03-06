package cool.klass.generator.klass.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class KlassHtmlGenerator
{
    private final ImmutableList<SourceCode> sourceCodes;

    public KlassHtmlGenerator(ImmutableList<SourceCode> sourceCodes)
    {
        this.sourceCodes = Objects.requireNonNull(sourceCodes);
    }

    public void writeHtmlFiles(@Nonnull Path outputPath)
    {
        this.sourceCodes
                // TODO: Graft in macros
                .select(x -> !x.getMacroSourceCode().isPresent())
                .forEachWith(KlassHtmlGenerator::writeHtmlFile, outputPath);
    }

    private static void writeHtmlFile(SourceCode sourceCode, Path outputPath)
    {
        Path   htmlOutputPath = KlassHtmlGenerator.getOutputPath(outputPath, sourceCode);
        String sourceCodeText = KlassHtmlGenerator.getSourceCode(sourceCode);
        KlassHtmlGenerator.printStringToFile(htmlOutputPath, sourceCodeText);
    }

    @Nonnull
    private static Path getOutputPath(
            @Nonnull Path outputPath,
            @Nonnull SourceCode sourceCode)
    {
        Path parentPath         = KlassHtmlGenerator.getParentPath(sourceCode);
        Path absoluteParentPath = outputPath.resolve(parentPath);
        Objects.requireNonNull(absoluteParentPath);
        File absoluteParentFile = absoluteParentPath.toFile();
        Objects.requireNonNull(absoluteParentFile);
        absoluteParentFile.mkdirs();

        String fileName = sourceCode.getSourceName() + ".html";

        return outputPath.resolve(fileName);
    }

    private static Path getParentPath(SourceCode sourceCode)
    {
        return sourceCode
                .getMacroSourceCode()
                .map(KlassHtmlGenerator::getParentPath)
                .orElseGet(() -> KlassHtmlGenerator.getRootParentPath(sourceCode));
    }

    private static Path getRootParentPath(SourceCode sourceCode)
    {
        String sourceName = sourceCode.getSourceName();
        File   sourceFile = new File(sourceName);
        return sourceFile.toPath().getParent();
    }

    private static void printStringToFile(@Nonnull Path path, String contents)
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private static String getSourceCode(SourceCode sourceCode)
    {
        BufferedTokenStream tokenStream = sourceCode.getTokenStream();
        MutableList<Token>  tokens      = ListAdapter.adapt(tokenStream.getTokens());

        //language=HTML
        return ""
                + "<html>\n"
                + "<head>\n"
                + "    <style>\n"
                + "        :root {\n"
                + "            --color-foreground: Black;\n"
                + "            --color-comment: Gray;\n"
                + "            --color-keyword: Purple;\n"
                + "            --color-identifier: Black;\n"
                + "            --color-package-name: Maroon;\n"
                + "            --color-top-level-element-name: Maroon;\n"
                + "            /* DeepSkyBlue */\n"
                + "            --color-interface-name: #0080C0;\n"
                + "            /* DarkBlue */\n"
                + "            --color-enumeration-literal-name: #000080;\n"
                + "            /* Chocolate */\n"
                + "            --color-parameter-name: #b55700;\n"
                + "            --color-property-name: #5555ff;\n"
                + "            --color-literal: Blue;\n"
                + "            --color-string-literal: Green;\n"
                + "\n"
                + "            /* DarkBlue */\n"
                + "            --color-url-constant: #000572;\n"
                + "\n"
                + "            /* Silver */\n"
                + "            --color-semicolon: #cccccc;\n"
                + "        }\n"
                + "\n"
                + "        .COMMENT {\n"
                + "            color: var(--color-comment, --color-foreground);\n"
                + "        }\n"
                + "\n"
                + "        .BLOCK_COMMENT {\n"
                + "            color: var(--color-block-comment, var(--color-comment, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .LINE_COMMENT {\n"
                + "            color: var(--color-line-comment, var(--color-comment, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD {\n"
                + "            color: var(--color-keyword, --color-foreground);\n"
                + "        }\n"
                + "\n"
                + "        .PACKAGE_KEYWORD {\n"
                + "            color: var(--color-package-keyword, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ENUMERATION {\n"
                + "            color: var(--color-keyword-enumeration, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_INTERFACE {\n"
                + "            color: var(--color-keyword-interface, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_USER {\n"
                + "            color: var(--color-keyword-user, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_CLASS {\n"
                + "            color: var(--color-keyword-class, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_PROJECTION {\n"
                + "            color: var(--color-keyword-projection, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_SERVICE {\n"
                + "            color: var(--color-keyword-service, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ABSTRACT {\n"
                + "            color: var(--color-keyword-abstract, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_EXTENDS {\n"
                + "            color: var(--color-keyword-extends, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_IMPLEMENTS {\n"
                + "            color: var(--color-keyword-implements, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_INHERITANCE_TYPE {\n"
                + "            color: var(--color-keyword-inheritance-type, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ASSOCIATION {\n"
                + "            color: var(--color-keyword-association, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_RELATIONSHIP {\n"
                + "            color: var(--color-keyword-relationship, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ORDER_BY {\n"
                + "            color: var(--color-keyword-order-by, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ORDER_BY_DIRECTION {\n"
                + "            color: var(--color-keyword-order-by-direction, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_ON {\n"
                + "            color: var(--color-keyword-on, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_MULTIPLICITY {\n"
                + "            color: var(--color-keyword-multiplicity, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_MULTIPLICITY_CHOICE {\n"
                + "            color: var(--color-keyword-multiplicity-choice, var(--color-keyword-multiplicity, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .KEYWORD_SERVICE_CRITERIA {\n"
                + "            color: var(--color-keyword-service-criteria, var(--color-keyword-multiplicity, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PRIMITIVE_TYPE {\n"
                + "            color: var(--color-primitive-type, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .VERB {\n"
                + "            color: var(--color-verb, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .VERB_GET {\n"
                + "            color: var(--color-verb-get, var(--color-verb, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .VERB_POST {\n"
                + "            color: var(--color-verb-post, var(--color-verb, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .VERB_PUT {\n"
                + "            color: var(--color-verb-put, var(--color-verb, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .VERB_PATCH {\n"
                + "            color: var(--color-verb-patch, var(--color-verb, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .VERB_DELETE {\n"
                + "            color: var(--color-verb-delete, var(--color-verb, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .MODIFIER {\n"
                + "            color: var(--color-modifier, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .CLASS_MODIFIER {\n"
                + "            color: var(--color-class-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .DATA_TYPE_PROPERTY_MODIFIER {\n"
                + "            color: var(--color-data-type-property-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .ASSOCIATION_END_MODIFIER {\n"
                + "            color: var(--color-association-end-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETERIZED_PROPERTY_MODIFIER {\n"
                + "            color: var(--color-parameterized-property-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETER_MODIFIER {\n"
                + "            color: var(--color-parameter-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .VALIDATION_MODIFIER {\n"
                + "            color: var(--color-validation-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .SERVICE_CATEGORY_MODIFIER {\n"
                + "            color: var(--color-service-category-modifier, var(--color-modifier, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .IDENTIFIER {\n"
                + "            color: var(--color-identifier, --color-foreground);\n"
                + "        }\n"
                + "\n"
                + "        .PACKAGE_NAME {\n"
                + "            color: var(--color-package-name, var(--color-identifier, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .TOP_LEVEL_ELEMENT_NAME {\n"
                + "            color: var(--color-top-level-element-name, var(--color-identifier, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .ENUMERATION_NAME {\n"
                + "            color: var(--color-enumeration-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .CLASSIFIER_NAME {\n"
                + "            color: var(--color-classifier-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .INTERFACE_NAME {\n"
                + "            color: var(--color-interface-name, var(--color-classifier-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .CLASS_NAME {\n"
                + "            color: var(--color-class-name, var(--color-classifier-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .ASSOCIATION_NAME {\n"
                + "            color: var(--color-association-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PROJECTION_NAME {\n"
                + "            color: var(--color-projection-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .ENUMERATION_LITERAL_NAME {\n"
                + "            color: var(--color-enumeration-literal-name, var(--color-identifier, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETER_NAME {\n"
                + "            color: var(--color-parameter-name, var(--color-identifier, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .PROPERTY_NAME {\n"
                + "            color: var(--color-property-name, var(--color-identifier, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .DATA_TYPE_PROPERTY_NAME {\n"
                + "            color: var(--color-data-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PRIMITIVE_PROPERTY_NAME {\n"
                + "            color: var(--color-primitive-property-name, var(--color-data-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .ENUMERATION_PROPERTY_NAME {\n"
                + "            color: var(--color-enumeration-property-name, var(--color-data-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .REFERENCE_TYPE_PROPERTY_NAME {\n"
                + "            color: var(--color-reference-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETERIZED_PROPERTY_NAME {\n"
                + "            color: var(--color-parameterized-property-name, var(--color-reference-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .ASSOCIATION_END_NAME {\n"
                + "            color: var(--color-association-end-name, var(--color-reference-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .ENUMERATION_REFERENCE {\n"
                + "            color: var(--color-enumeration-reference, var(--color-enumeration-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .INTERFACE_REFERENCE {\n"
                + "            color: var(--color-interface-reference, var(--color-interface-name, var(--color-classifier-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)))));\n"
                + "        }\n"
                + "\n"
                + "        .CLASS_REFERENCE {\n"
                + "            color: var(--color-class-reference, var(--color-class-name, var(--color-classifier-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground)))));\n"
                + "        }\n"
                + "\n"
                + "        .PROJECTION_REFERENCE {\n"
                + "            color: var(--color-projection-reference, var(--color-projection-name, var(--color-top-level-element-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .DATA_TYPE_PROPERTY_REFERENCE {\n"
                + "            color: var(--color-data-type-property-reference, var(--color-data-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .ASSOCIATION_END_REFERENCE {\n"
                + "            color: var(--color-association-end-reference, var(--color-association-end-name, var(--color-reference-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground)))));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETERIZED_PROPERTY_REFERENCE {\n"
                + "            color: var(--color-parameterized-property-reference, var(--color-parameterized-property-name, var(--color-reference-type-property-name, var(--color-property-name, var(--color-identifier, --color-foreground)))));\n"
                + "        }\n"
                + "\n"
                + "        .PROPERTY_REFERENCE {\n"
                + "            color: var(--color-property-reference, var(--color-property-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PARAMETER_REFERENCE {\n"
                + "            color: var(--color-parameter-reference, var(--color-parameter-name, var(--color-identifier, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .LITERAL {\n"
                + "            color: var(--color-literal, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .LITERAL_THIS {\n"
                + "            color: var(--color-literal-this, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .LITERAL_NATIVE {\n"
                + "            color: var(--color-literal-native, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .STRING_LITERAL {\n"
                + "            color: var(--color-string-literal, var(--color-literal, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .INTEGER_LITERAL {\n"
                + "            color: var(--color-integer-literal, var(--color-literal, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .BOOLEAN_LITERAL {\n"
                + "            color: var(--color-boolean-literal, var(--color-literal, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .CHARACTER_LITERAL {\n"
                + "            color: var(--color-character-literal, var(--color-literal, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .FLOATING_POINT_LITERAL {\n"
                + "            color: var(--color-floating-point-literal, var(--color-literal, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .ASTERISK_LITERAL {\n"
                + "            color: var(--color-asterisk-literal, var(--color-integer-literal, var(--color-literal, var(--color-keyword, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .PUNCTUATION {\n"
                + "            color: var(--color-punctuation, --color-foreground);\n"
                + "        }\n"
                + "\n"
                + "        .COMMA {\n"
                + "            color: var(--color-comma, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .DOT {\n"
                + "            color: var(--color-dot, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .DOTDOT {\n"
                + "            color: var(--color-dotdot, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .SEMICOLON {\n"
                + "            color: var(--color-semicolon, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR {\n"
                + "            color: var(--color-operator, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_EQ {\n"
                + "            color: var(--color-operator-eq, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_NE {\n"
                + "            color: var(--color-operator-ne, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_LT {\n"
                + "            color: var(--color-operator-lt, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_GT {\n"
                + "            color: var(--color-operator-gt, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_LE {\n"
                + "            color: var(--color-operator-le, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_GE {\n"
                + "            color: var(--color-operator-ge, var(--color-operator, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .WORD_OPERATOR {\n"
                + "            color: var(--color-word-operator, var(--color-keyword, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_IN {\n"
                + "            color: var(--color-operator-in, var(--color-word-operator, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .OPERATOR_STRING {\n"
                + "            color: var(--color-operator-string, var(--color-word-operator, var(--color-keyword, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .COLON {\n"
                + "            color: var(--color-colon, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .SLASH {\n"
                + "            color: var(--color-slash, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .QUESTION {\n"
                + "            color: var(--color-question, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .PAIRED_PUNCTUATION {\n"
                + "            color: var(--color-paired-punctuation, var(--color-punctuation, --color-foreground));\n"
                + "        }\n"
                + "\n"
                + "        .PARENTHESES {\n"
                + "            color: var(--color-parentheses, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .PARENTHESIS_LEFT {\n"
                + "            color: var(--color-parenthesis-left, var(--color-parentheses, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .PARENTHESIS_RIGHT {\n"
                + "            color: var(--color-parenthesis-right, var(--color-parentheses, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .CURLY_BRACES {\n"
                + "            color: var(--color-curly-braces, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .CURLY_LEFT {\n"
                + "            color: var(--color-curly-left, var(--color-curly-braces, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .CURLY_RIGHT {\n"
                + "            color: var(--color-curly-right, var(--color-curly-braces, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .SQUARE_BRACKETS {\n"
                + "            color: var(--color-square-brackets, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground)));\n"
                + "        }\n"
                + "\n"
                + "        .SQUARE_BRACKET_LEFT {\n"
                + "            color: var(--color-square-bracket-left, var(--color-square-brackets, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "\n"
                + "        .SQUARE_BRACKET_RIGHT {\n"
                + "            color: var(--color-square-bracket-right, var(--color-square-brackets, var(--color-paired-punctuation, var(--color-punctuation, --color-foreground))));\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "<pre>\n"
                + tokens
                    .reject(token -> token.getType() == Token.EOF)
                    .collectWith(KlassHtmlGenerator::getSourceCode, sourceCode).makeString("")
                + "</pre>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private static String getSourceCode(Token token, Function<Token, Optional<TokenCategory>> tokenCategorizer)
    {
        Optional<TokenCategory> maybeTokenCategory = tokenCategorizer.apply(token);
        return maybeTokenCategory.map(tokenCategory -> KlassHtmlGenerator.getSourceCode(token, tokenCategory))
                .orElseGet(() -> KlassHtmlGenerator.getSourceCodeWithoutCategory(token));
    }

    private static String getSourceCode(Token token, TokenCategory tokenCategory)
    {
        String escapedText = StringEscapeUtils.escapeHtml4(token.getText());
        //language=HTML
        return "<span class='" + tokenCategory.name() + "'>" + escapedText + "</span>";
    }

    private static String getSourceCodeWithoutCategory(Token token)
    {
        String escapedText = StringEscapeUtils.escapeHtml4(token.getText());
        //language=HTML
        return "<span>" + escapedText + "</span>";
    }
}
