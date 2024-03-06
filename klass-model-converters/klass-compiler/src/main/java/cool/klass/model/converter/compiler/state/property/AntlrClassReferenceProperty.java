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
    protected AntlrClassReference classReference;

    protected AntlrClassReferenceProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
    }

    @Override
    public void enterClassReference(@Nonnull AntlrClassReference classReference)
    {
        if (this.classReference != null)
        {
            throw new AssertionError();
        }

        this.classReference = Objects.requireNonNull(classReference);
    }

    @Nonnull
    @Override
    public AntlrClass getType()
    {
        return this.classReference.getKlass();
    }
}
