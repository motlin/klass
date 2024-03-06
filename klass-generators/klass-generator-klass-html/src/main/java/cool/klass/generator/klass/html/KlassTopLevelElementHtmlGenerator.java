package cool.klass.generator.klass.html;

import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public final class KlassTopLevelElementHtmlGenerator
{
    public static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_HYPHEN);

    private KlassTopLevelElementHtmlGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String writeHtml(PackageableElement packageableElement)
    {
        Optional<SourceCode> sourceCodeObject = packageableElement.getSourceCodeObject();
        AbstractElement      abstractElement  = (AbstractElement) packageableElement;
        ParserRuleContext    elementContext   = abstractElement.getElementContext();
        Token                start            = elementContext.getStart();
        Token                stop             = elementContext.getStop();

        SourceCode          sourceCode    = sourceCodeObject.get();
        BufferedTokenStream tokenStream   = sourceCode.getTokenStream();
        MutableList<Token>  tokens        = ListAdapter.adapt(tokenStream.getTokens());
        boolean             containsStart = tokens.contains(start);
        boolean             containsStop  = tokens.contains(stop);

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
                .collectWith(KlassTopLevelElementHtmlGenerator::getSourceCode, sourceCode).makeString("")
                + "</pre>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private static String getSourceCode(Token token, Function<Token, Optional<TokenCategory>> tokenCategorizer)
    {
        Optional<TokenCategory> maybeTokenCategory = tokenCategorizer.apply(token);
        return maybeTokenCategory.map(tokenCategory -> KlassTopLevelElementHtmlGenerator.getSourceCode(
                token,
                tokenCategory))
                .orElseGet(() -> KlassTopLevelElementHtmlGenerator.getSourceCodeWithoutCategory(token));
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
