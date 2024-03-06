package cool.klass.generator.klass.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class KlassSourceCodeHtmlGenerator
{
    public static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_HYPHEN);

    private final DomainModelWithSourceCode domainModel;

    public KlassSourceCodeHtmlGenerator(DomainModelWithSourceCode domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public static String getSourceCode(
            Token token,
            DomainModelWithSourceCode domainModel)
    {
        Optional<TokenCategory>         maybeTokenCategory        = domainModel.getTokenCategory(token);
        Optional<ElementWithSourceCode> maybeElementByReference   = domainModel.getElementByReference(token);
        Optional<ElementWithSourceCode> maybeElementByDeclaration = domainModel.getElementByDeclaration(token);
        String cssClass = maybeTokenCategory
                .map(Enum::name)
                .map(CONVERTER::convert)
                .map(tokenCategory -> " class='klass-" + tokenCategory + "'")
                .orElse("");
        String escapedText = StringEscapeUtils.escapeHtml4(token.getText());
        String anchor = maybeElementByDeclaration
                .map(value -> "<a id=\""
                        + KlassSourceCodeHtmlGenerator.getIdForElement(value)
                        + "\" href=\""
                        + KlassSourceCodeHtmlGenerator.getLinkForElement(value)
                        + "\">"
                        + StringEscapeUtils.escapeHtml4(((NamedElement) value).getName())
                        + "</a>")
                .orElse(escapedText);
        String text = maybeElementByReference
                .map(element -> "<a href=\""
                        + KlassSourceCodeHtmlGenerator.getLinkForElement(element)
                        + "\">"
                        + escapedText
                        + "</a>")
                .orElse("");

        String result = "<span" + cssClass + ">" + anchor + text + "</span>";
        return result;
    }

    public static String getSourceCode(
            @Nonnull DomainModelWithSourceCode domainModel,
            @Nonnull SourceCode sourceCode,
            @Nonnull Optional<TopLevelElementWithSourceCode> topLevelElement,
            @Nonnull Optional<String> memberName)
    {
        Objects.requireNonNull(domainModel);
        Objects.requireNonNull(sourceCode);
        Objects.requireNonNull(topLevelElement);
        Objects.requireNonNull(memberName);

        BufferedTokenStream tokenStream = sourceCode.getTokenStream();
        MutableList<Token>  tokens      = ListAdapter.adapt(tokenStream.getTokens());

        String body = tokens
                .reject(token -> token.getType() == Token.EOF)
                .collect(token -> getSourceCode(token, domainModel))
                .makeString("");

        //language=HTML
        return ""
                + "<html>\n"
                + "<head>\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/klass-theme-light.css\">\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/klass-theme-dark.css\">\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/klass-syntax.css\">\n"
                + "    <style>\n"
                + "        :root {\n"
                + "            font-family: \"Lucida Console\", Courier, monospace;\n"
                + "            font-size: 16;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body class=\"klass-theme-light\">"
                + "<pre>\n"
                + body
                + "</pre>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private void writeHtmlFile(SourceCode sourceCode, Path outputPath)
    {
        Path htmlOutputPath = KlassSourceCodeHtmlGenerator.getOutputPath(outputPath, sourceCode);
        String sourceCodeText = this.getSourceCode(
                this.domainModel,
                sourceCode,
                Optional.empty(),
                Optional.empty());
        KlassSourceCodeHtmlGenerator.printStringToFile(htmlOutputPath, sourceCodeText);
    }

    @Nonnull
    private static Path getOutputPath(
            @Nonnull Path outputPath,
            @Nonnull SourceCode sourceCode)
    {
        Path parentPath         = KlassSourceCodeHtmlGenerator.getParentPath(sourceCode);
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
                .map(KlassSourceCodeHtmlGenerator::getParentPath)
                .orElseGet(() -> KlassSourceCodeHtmlGenerator.getRootParentPath(sourceCode));
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

    public void writeHtmlFiles(@Nonnull Path outputPath)
    {
        this
                .domainModel
                .getSourceCodes()
                // TODO: Graft in macros
                .select(sourceCode -> sourceCode.getMacroSourceCode().isEmpty())
                .forEachWith(this::writeHtmlFile, outputPath);
    }

    private static String getBody(Token token, DomainModelWithSourceCode domainModel)
    {
        Optional<ElementWithSourceCode> maybeElementByDeclaration = domainModel.getElementByDeclaration(token);
        Optional<ElementWithSourceCode> maybeElementByReference   = domainModel.getElementByReference(token);

        if (maybeElementByDeclaration.isPresent() && maybeElementByReference.isPresent())
        {
            throw new AssertionError();
        }

        String escapedText = StringEscapeUtils.escapeHtml4(token.getText());
        if (maybeElementByDeclaration.isEmpty() && maybeElementByReference.isEmpty())
        {
            return escapedText;
        }

        if (maybeElementByDeclaration.isPresent())
        {
            ElementWithSourceCode element = maybeElementByDeclaration.get();

            String idForElement       = getIdForElement(element);
            String linkForElement     = getLinkForElement(element);
            String elementName        = ((NamedElement) element).getName();
            String escapedElementName = StringEscapeUtils.escapeHtml4(elementName);

            return String.format(
                    "<a id=\"%s\" href=\"%s\">%s</a>",
                    idForElement,
                    linkForElement,
                    escapedElementName);
        }

        if (maybeElementByReference.isPresent())
        {
            ElementWithSourceCode element = maybeElementByReference.get();

            String linkForElement = getLinkForElement(element);
            return String.format("<a href=\"%s\">%s</a>", linkForElement, escapedText);
        }

        throw new AssertionError();
    }

    @Nullable
    private static String getIdForElement(ElementWithSourceCode value)
    {
        if (value instanceof TopLevelElement)
        {
            TopLevelElement topLevelElement = (TopLevelElement) value;
            return StringEscapeUtils.escapeHtml4(topLevelElement.getName());
        }
        if (value instanceof Property)
        {
            Property property = (Property) value;
            return StringEscapeUtils.escapeHtml4(property.getOwningClassifier().getName() + "." + property.getName());
        }
        if (value instanceof EnumerationLiteral)
        {
            EnumerationLiteral enumerationLiteral = (EnumerationLiteral) value;
            return StringEscapeUtils.escapeHtml4(enumerationLiteral.getType().getName()
                    + "."
                    + enumerationLiteral.getName());
        }
        throw new AssertionError(value);
    }

    @Nonnull
    private static String getLinkForElement(ElementWithSourceCode element)
    {
        if (element instanceof TopLevelElement)
        {
            TopLevelElement topLevelElement = (TopLevelElement) element;
            return String.format("/api/meta/code/element/%s#%s", topLevelElement.getName(), topLevelElement.getName());
        }
        if (element instanceof Property)
        {
            Property property = (Property) element;
            return MessageFormat.format(
                    "/api/meta/code/element/{0}/{1}#{0}.{1}",
                    property.getOwningClassifier().getName(),
                    property.getName());
        }
        if (element instanceof EnumerationLiteral)
        {
            EnumerationLiteral enumerationLiteral = (EnumerationLiteral) element;
            return MessageFormat.format(
                    "/api/meta/code/element/{0}/{1}#{0}.{1}",
                    enumerationLiteral.getType().getName(),
                    enumerationLiteral.getName());
        }
        throw new AssertionError(element);
    }
}
