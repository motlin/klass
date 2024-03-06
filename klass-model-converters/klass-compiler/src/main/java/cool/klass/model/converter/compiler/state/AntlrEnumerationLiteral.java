package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;

public class AntlrEnumerationLiteral
        extends AntlrIdentifierElement
{
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            new EnumerationLiteralContext(AMBIGUOUS_PARENT, -1),
            Optional.empty(),
            -1,
            AMBIGUOUS_IDENTIFIER_CONTEXT,
            Optional.empty(),
            AntlrEnumeration.AMBIGUOUS);

    public static final AntlrEnumerationLiteral NOT_FOUND = new AntlrEnumerationLiteral(
            new EnumerationLiteralContext(NOT_FOUND_PARENT, -1),
            Optional.empty(),
            -1,
            NOT_FOUND_IDENTIFIER_CONTEXT,
            Optional.empty(),
            AntlrEnumeration.NOT_FOUND);

    @Nonnull
    private final Optional<String> prettyName;
    @Nonnull
    private final AntlrEnumeration owningEnumeration;

    private EnumerationLiteralBuilder elementBuilder;

    public AntlrEnumerationLiteral(
            @Nonnull EnumerationLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull Optional<String> prettyName,
            @Nonnull AntlrEnumeration owningEnumeration)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.prettyName        = prettyName;
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
                this.ordinal,
                this.getNameContext(),
                this.prettyName,
                this.owningEnumeration.getElementBuilder());
        return this.elementBuilder;
    }

    public void reportDuplicateName(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format("Duplicate enumeration literal: '%s'.", this.getName());
        compilerAnnotationHolder.add("ERR_DUP_ENM", message, this);
    }

    public void reportDuplicatePrettyName(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format("Duplicate enumeration pretty name: '%s'.", this.prettyName.get());
        compilerAnnotationHolder.add("ERR_DUP_LIT", message, this, this.getElementContext().enumerationPrettyName());
    }

    @Override
    public boolean isContext()
    {
        return true;
    }
}
