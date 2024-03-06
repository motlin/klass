package cool.klass.generator.klass.html;

import java.util.Optional;

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
                .collect(each -> KlassSourceCodeHtmlGenerator.getSourceCode(
                        each,
                        domainModel,
                        Optional.empty(),
                        Optional.empty()))
                .makeString("");

        //language=HTML
        String prefix = """
                <html>
                <head>
                    <link rel="stylesheet" type="text/css" href="/static/css/light.css" media="(prefers-color-scheme: light)">
                    <link rel="stylesheet" type="text/css" href="/static/css/dark.css" media="(prefers-color-scheme: dark)">
                    <link rel="stylesheet" type="text/css" href="/static/css/klass-syntax.css">
                    <script type="module" src="https://unpkg.com/dark-mode-toggle"></script>
                    <style>
                        :root {
                            font-family: "Lucida Console", Courier, monospace;
                            font-size: 16;
                        }
                    </style>
                </head>
                <body class="klass">
                <aside>
                  <dark-mode-toggle class="slider" legend="Dark Mode" appearance="toggle"></dark-mode-toggle>
                </aside>
                <pre>
                """;
        return prefix
               + body
               + "</pre>\n"
               + "</body>\n"
               + "</html>\n";
    }
}
