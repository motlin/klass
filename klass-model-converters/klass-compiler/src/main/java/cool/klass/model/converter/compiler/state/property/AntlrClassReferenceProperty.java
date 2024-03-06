package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassReference;
import cool.klass.model.converter.compiler.state.AntlrClassReferenceOwner;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrClassReferenceProperty
        extends AntlrReferenceProperty<AntlrClass>
        implements AntlrClassReferenceOwner
{
    protected AntlrClassReference classReferenceState;

    protected AntlrClassReferenceProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
    }

    @Override
    public void enterClassReference(@Nonnull AntlrClassReference classReferenceState)
    {
        if (this.classReferenceState != null)
        {
            throw new AssertionError();
        }

        this.classReferenceState = Objects.requireNonNull(classReferenceState);
    }

    @Nonnull
    @Override
    public AntlrClass getType()
    {
        return this.classReferenceState.getClassState();
    }
}
