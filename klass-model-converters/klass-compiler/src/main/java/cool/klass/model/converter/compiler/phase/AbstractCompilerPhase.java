package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.parser.DelegatingKlassListener;
import cool.klass.model.meta.grammar.KlassListener;

@SuppressWarnings("AbstractClassExtendsConcreteClass")
public abstract class AbstractCompilerPhase
        extends DelegatingKlassListener
{
    @Nonnull
    protected final CompilerState compilerState;

    private final KlassListener delegate;

    protected AbstractCompilerPhase(@Nonnull CompilerState compilerState)
    {
        this.compilerState = Objects.requireNonNull(compilerState);
        this.delegate      = compilerState.asListener();
    }

    @Override
    protected KlassListener getDelegate()
    {
        return this.delegate;
    }

    @Nonnull
    public String getName()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getName() not implemented yet");
    }
}
