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

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
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
    public static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_HYPHEN);

    private final ImmutableList<SourceCode> sourceCodes;

    public KlassHtmlGenerator(ImmutableList<SourceCode> sourceCodes)
    {
        this.sourceCodes = Objects.requireNonNull(sourceCodes);
    }

    public void writeHtmlFiles(@Nonnull Path outputPath)
    {
        this.sourceCodes
                // TODO: Graft in macros
                .select(sourceCode -> !sourceCode.getMacroSourceCode().isPresent())
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
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./klass-theme-light.css\">\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./klass-theme-dark.css\">\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./klass-syntax.css\">\n"
                + "    <style>\n"
                + "        :root {\n"
                + "            font-family: \"Lucida Console\", Courier, monospace;\n"
                + "            font-size: 16;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body class=\"klass-theme-light\">"
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
        return "<span class='klass-" + CONVERTER.convert(tokenCategory.name()) + "'>" + escapedText + "</span>";
    }

    private static String getSourceCodeWithoutCategory(Token token)
    {
        String escapedText = StringEscapeUtils.escapeHtml4(token.getText());
        //language=HTML
        return "<span>" + escapedText + "</span>";
    }
}
