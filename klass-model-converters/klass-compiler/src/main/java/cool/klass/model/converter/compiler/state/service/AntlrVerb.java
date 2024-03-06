package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.api.service.Verb;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrVerb extends AntlrElement
{
    @Nonnull
    public static final AntlrVerb AMBIGUOUS = new AntlrVerb(new ParserRuleContext(), Optional.empty(), Verb.GET);

    @Nonnull
    private final Verb verb;

    public AntlrVerb(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull Verb verb)
    {
        super(elementContext, compilationUnit);
        this.verb = Objects.requireNonNull(verb);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Nonnull
    public Verb getVerb()
    {
        return this.verb;
    }
}
