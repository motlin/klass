package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;

public class AntlrEnumerationLiteral
        extends AntlrIdentifierElement
{
    @Nonnull
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            Optional.empty(),
            AntlrEnumeration.AMBIGUOUS);
    @Nonnull
    public static final AntlrEnumerationLiteral NOT_FOUND = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            Optional.empty(),
            AntlrEnumeration.NOT_FOUND);

    @Nonnull
    private final Optional<String> prettyName;
    @Nonnull
    private final AntlrEnumeration owningEnumeration;

    private EnumerationLiteralBuilder elementBuilder;

    public AntlrEnumerationLiteral(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IdentifierContext nameContext,
            int ordinal,
            @Nonnull Optional<String> prettyName,
            @Nonnull AntlrEnumeration owningEnumeration)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
        this.prettyName = prettyName;
        this.owningEnumeration = Objects.requireNonNull(owningEnumeration);
    }

    @Nonnull
    @Override
    public EnumerationLiteralContext getElementContext()
    {
        return (EnumerationLiteralContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningEnumeration);
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    @Nonnull
    public Optional<String> getPrettyName()
    {
        return this.prettyName;
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        return CONSTANT_NAME_PATTERN;
    }

    public EnumerationLiteralBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.elementBuilder = new EnumerationLiteralBuilder(
                this.getElementContext(),
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.getNameContext(),
                this.ordinal,
                this.prettyName,
                this.owningEnumeration.getElementBuilder());
        return this.elementBuilder;
    }

    public void reportDuplicateName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate enumeration literal: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_ENM", message, this);
    }

    public void reportDuplicatePrettyName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate enumeration pretty name: '%s'.", this.prettyName.get());
        compilerErrorHolder.add("ERR_DUP_LIT", message, this, this.getElementContext().enumerationPrettyName());
    }

    @Override
    public boolean isContext()
    {
        return true;
    }
}
