package cool.klass.model.converter.compiler.error;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
import org.fusesource.jansi.Ansi.Color;

import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

public abstract class AbstractCompilerError
{
    @Nonnull
    protected final CompilationUnit                  compilationUnit;
    @Nonnull
    private final   Optional<CauseCompilerError>     macroCause;
    // TODO: Change type of offendingContexts to also be SourceContexts
    @Nonnull
    private final   ImmutableList<ParserRuleContext> offendingContexts;
    @Nonnull
    private final   ImmutableList<IAntlrElement>     sourceContexts;

    protected AbstractCompilerError(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<IAntlrElement> sourceContexts)
    {
        this.macroCause        = Objects.requireNonNull(macroCause);
        this.compilationUnit   = Objects.requireNonNull(compilationUnit);
        this.offendingContexts = Objects.requireNonNull(offendingContexts);
        this.sourceContexts    = Objects.requireNonNull(sourceContexts).select(IAntlrElement::isContext);

        if (offendingContexts.isEmpty())
        {
            throw new AssertionError();
        }
        if (!offendingContexts.noneSatisfy(offendingContext -> offendingContext.getStart() == null))
        {
            throw new AssertionError();
        }

        this.applyListenerToStack();
    }

    @Nonnull
    protected CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }

    protected String getContextString()
    {
        ImmutableList<AbstractContextString> contextStrings = this.applyListenerToStack();

        int    maxLine         = contextStrings.getLast().getLine();
        String maxLineString   = String.valueOf(maxLine);
        int    lineNumberWidth = maxLineString.length();

        String entireContext = contextStrings
                .collect(contextString -> contextString.toString(lineNumberWidth))
                .makeString("", "\n", "\n");
        return ansi().a(entireContext).reset().toString();
    }

    @Nonnull
    private String getFilenameWithoutDirectory()
    {
        String sourceName = this.compilationUnit.getSourceName();
        return this.macroCause
                .map(ignore -> sourceName)
                .orElseGet(() -> sourceName.substring(sourceName.lastIndexOf('/') + 1));
    }

    protected String getShortLocationString()
    {
        return this.macroCause
                .map(ignore -> this.getLocationWithoutLine())
                .orElseGet(this::getLocationWithLine);
    }

    private String getLocationWithoutLine()
    {
        return String.format("(%s)", this.compilationUnit);
    }

    private String getLocationWithLine()
    {
        return String.format(
                "(%s:%d)",
                // TODO: This should be part of the source information
                this.getFilenameWithoutDirectory(),
                this.getLine());
    }

    private ImmutableList<AbstractContextString> applyListenerToStack()
    {
        MutableSet<Token> contextTokens = SetAdapter.adapt(new LinkedHashSet<>());

        this.sourceContexts
                .asReversed()
                .collect(IAntlrElement::getContextBefore)
                .flatCollect(this::getTokenRange)
                .into(contextTokens);

        this.sourceContexts
                .asLazy()
                .collect(IAntlrElement::getContextAfter)
                .reject(Objects::isNull)
                .flatCollect(this::getTokenRange)
                .into(contextTokens);

        MutableSet<Token> underlinedTokens = this.offendingContexts
                .asLazy()
                .flatCollect(this::getUnderlinedTokenRange)
                .into(SetAdapter.adapt(new LinkedHashSet<>()));

        MutableSet<Integer> contextLines = contextTokens
                .asLazy()
                .collect(Token::getLine)
                .into(SetAdapter.adapt(new LinkedHashSet<>()));

        MutableSet<Integer> underlinedLines = underlinedTokens
                .asLazy()
                .collect(Token::getLine)
                .into(SetAdapter.adapt(new LinkedHashSet<>()));

        if (!contextTokens.containsAll(underlinedTokens))
        {
            throw new AssertionError();
        }

        if (!contextLines.containsAll(underlinedLines))
        {
            throw new AssertionError();
        }

        ImmutableList<TokenLine>           tokenLines     = this.getTokenLines(contextTokens);
        MutableList<AbstractContextString> contextStrings = Lists.mutable.empty();

        for (TokenLine tokenLine : tokenLines)
        {
            String string = tokenLine.getTokens().collect(LexicalColorizer::colorize).makeString("");
            if (!string.endsWith("\n"))
            {
                string += "\n";
            }
            contextStrings.add(new ContextString(tokenLine.getLine(), string));
            if (underlinedLines.contains(tokenLine.getLine()))
            {
                String underline = this.getUnderline(tokenLine, underlinedTokens);
                contextStrings.add(new UnderlineContextString(tokenLine.getLine(), underline));
            }
        }

        return contextStrings.toImmutable();
    }

    private ImmutableList<TokenLine> getTokenLines(MutableSet<Token> contextTokens)
    {
        Iterator<Token>        contextTokenIterator = contextTokens.iterator();
        Deque<Token>           currentLine          = new ArrayDeque<>();
        Token                  currentToken         = null;
        MutableList<TokenLine> tokenLines           = Lists.mutable.empty();
        while (contextTokenIterator.hasNext())
        {
            Token nextToken = contextTokenIterator.next();
            if (currentLine.isEmpty())
            {
                this.startLine(currentLine, nextToken);
                currentToken = nextToken;
            }
            else if (currentToken.getTokenSource() == nextToken.getTokenSource()
                    && currentToken.getLine() == nextToken.getLine())
            {
                this.endLine(currentLine, nextToken);
                currentToken = nextToken;
            }
            else
            {
                tokenLines.add(new TokenLine(currentToken.getLine(), Lists.immutable.withAll(currentLine)));
                currentLine.clear();

                this.startLine(currentLine, nextToken);
                currentToken = nextToken;
            }
        }
        tokenLines.add(new TokenLine(currentToken.getLine(), Lists.immutable.withAll(currentLine)));
        return tokenLines.toImmutable();
    }

    private void startLine(Deque<Token> currentLine, Token nextToken)
    {
        int               beginTokenIndex = this.getBeginTokenIndex(nextToken.getLine(), nextToken.getTokenIndex());
        CommonTokenStream tokenStream     = (CommonTokenStream) this.compilationUnit.getTokenStream();
        List<Token> tokenRange = tokenStream.get(
                beginTokenIndex,
                nextToken.getTokenIndex() - 1);
        MutableList<Token> tokens = tokenRange == null ? Lists.mutable.empty() : ListAdapter.adapt(tokenRange);
        currentLine.addAll(tokens);
        currentLine.add(nextToken);
    }

    private void endLine(Deque<Token> currentLine, Token nextToken)
    {
        int               endTokenIndex = this.getEndTokenIndex(nextToken.getLine(), nextToken.getTokenIndex());
        CommonTokenStream tokenStream   = (CommonTokenStream) this.compilationUnit.getTokenStream();
        List<Token> tokenRange = tokenStream.get(
                endTokenIndex,
                nextToken.getTokenIndex() - 1);
        MutableList<Token> tokens = tokenRange == null ? Lists.mutable.empty() : ListAdapter.adapt(tokenRange);
        currentLine.add(nextToken);
        currentLine.addAll(tokens);
    }

    private int getBeginTokenIndex(
            int startLine,
            int startTokenIndex)
    {
        TokenStream tokenStream     = this.compilationUnit.getTokenStream();
        int         beginTokenIndex = startTokenIndex;
        while (beginTokenIndex > 0 && tokenStream.get(beginTokenIndex - 1).getLine() == startLine)
        {
            beginTokenIndex -= 1;
        }
        return beginTokenIndex;
    }

    private int getEndTokenIndex(
            int stopLine,
            int stopTokenIndex)
    {
        TokenStream tokenStream   = this.compilationUnit.getTokenStream();
        int         endTokenIndex = stopTokenIndex;
        while (endTokenIndex + 1 < tokenStream.size() && tokenStream.get(endTokenIndex + 1).getLine() == stopLine)
        {
            endTokenIndex += 1;
        }
        return endTokenIndex;
    }

    private String getUnderline(
            TokenLine tokenLine,
            MutableSet<Token> underlinedTokens)
    {
        String uncoloredString = tokenLine
                .getTokens()
                .collectWith(this::getSpaceOrUnderline, underlinedTokens)
                .makeString("")
                .stripTrailing();

        Color caretColor = this instanceof RootCompilerError ? RED : GREEN;
        String underlineString = ansi()
                .fg(caretColor).a(uncoloredString + "\n").toString();

        return underlineString;
    }

    private String getSpaceOrUnderline(Token token, MutableSet<Token> underlinedTokens)
    {
        String character = underlinedTokens.contains(token) ? "^" : " ";
        int    length    = token.getText().length();
        return character.repeat(length);
    }

    private ImmutableList<Token> getUnderlinedTokenRange(@Nonnull ParserRuleContext ctx)
    {
        Token startToken = ctx.getStart();
        Token stopToken  = ctx.getStop();

        return this.getTokenRange(startToken, stopToken);
    }

    private ImmutableList<Token> getTokenRange(Token startToken, Token stopToken)
    {
        int startTokenIndex = startToken.getTokenIndex();
        int stopTokenIndex  = stopToken.getTokenIndex();

        CommonTokenStream tokenStream = (CommonTokenStream) this.compilationUnit.getTokenStream();

        List<Token> tokens = tokenStream.get(startTokenIndex, stopTokenIndex);
        return Lists.immutable.withAll(tokens);
    }

    private ImmutableList<Token> getTokenRange(Pair<Token, Token> pair)
    {
        return this.getTokenRange(pair.getOne(), pair.getTwo());
    }

    protected int getLine()
    {
        return this.getOffendingToken().getLine();
    }

    protected int getCharPositionInLine()
    {
        return this.getOffendingToken().getCharPositionInLine();
    }

    private Token getOffendingToken()
    {
        return this.offendingContexts.getFirst().getStart();
    }

    @Nonnull
    protected String getCauseString()
    {
        return this.macroCause
                .map(CauseCompilerError::toString)
                .orElse("");
    }

    @Nonnull
    protected String getOptionalLocationMessage()
    {
        return this.macroCause
                .map(ignored -> "")
                .orElseGet(this::getLocationMessage);
    }

    @Nonnull
    private String getLocationMessage()
    {
        return ansi().a("\n")
                .fg(CYAN).a("Location:  ").reset().a(this.getFilenameWithoutDirectory()).a(":").a(this.getLine()).reset().a("\n")
                .fg(CYAN).a("File:      ").reset().a(this.compilationUnit).reset().a("\n")
                .fg(CYAN).a("Line:      ").reset().a(this.getLine()).reset().a("\n")
                .fg(CYAN).a("Character: ").reset().a(this.getCharPositionInLine() + 1)
                .toString();
    }

    public AbstractCompilerError getUltimateCause()
    {
        return this.macroCause
                .map(AbstractCompilerError::getUltimateCause)
                .orElse(this);
    }

    @Nonnull
    public String getSourceName()
    {
        return this.compilationUnit.getSourceName();
    }

    public ImmutableList<AbstractCompilerError> getCauseChain()
    {
        return this.populateCauseChain(Lists.mutable.empty()).toImmutable();
    }

    protected final MutableList<AbstractCompilerError> populateCauseChain(MutableList<AbstractCompilerError> list)
    {
        list.add(this);
        this.macroCause.ifPresent(each -> each.populateCauseChain(list));
        return list;
    }
}
