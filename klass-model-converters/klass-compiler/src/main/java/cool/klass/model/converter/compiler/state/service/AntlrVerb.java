package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.api.service.Verb;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrVerb extends AntlrElement
{
    @Nonnull
    public static final AntlrVerb AMBIGUOUS = new AntlrVerb(new ParserRuleContext(), null, true, Verb.GET);

    @Nonnull
    private final Verb verb;

    public AntlrVerb(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull Verb verb)
    {
        super(elementContext, compilationUnit, inferred);
        this.verb = Objects.requireNonNull(verb);
    }

    @Nonnull
    public Verb getVerb()
    {
        return this.verb;
    }
}
