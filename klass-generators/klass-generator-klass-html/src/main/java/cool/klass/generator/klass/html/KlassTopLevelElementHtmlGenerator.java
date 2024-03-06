package cool.klass.generator.klass.html;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public final class KlassTopLevelElementHtmlGenerator
{
    private KlassTopLevelElementHtmlGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String writeHtml(DomainModelWithSourceCode domainModel, TopLevelElementWithSourceCode topLevelElement)
    {
        SourceCode        sourceCode      = topLevelElement.getSourceCodeObject();
        AbstractElement   abstractElement = (AbstractElement) topLevelElement;
        ParserRuleContext elementContext  = abstractElement.getElementContext();
        Token             start           = elementContext.getStart();
        Token             stop            = elementContext.getStop();

        BufferedTokenStream tokenStream   = sourceCode.getTokenStream();
        MutableList<Token>  tokens        = ListAdapter.adapt(tokenStream.getTokens());
        boolean             containsStart = tokens.contains(start);
        boolean             containsStop  = tokens.contains(stop);

        String body = tokens
                .reject(token -> token.getType() == Token.EOF)
                .collectWith(KlassSourceCodeHtmlGenerator::getSourceCode, domainModel)
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
                + "<body class=\"klass-theme-dark\">"
                + "<pre>\n"
                + body
                + "</pre>\n"
                + "</body>\n"
                + "</html>\n";
    }
}
