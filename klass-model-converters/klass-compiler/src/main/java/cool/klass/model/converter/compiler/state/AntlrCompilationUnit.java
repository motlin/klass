package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;

public class AntlrCompilationUnit
        extends AntlrElement
{
    @Nonnull
    public static final AntlrCompilationUnit AMBIGUOUS = new AntlrCompilationUnit(
            new CompilationUnitContext(null, -1),
            Optional.empty());

    @Nonnull
    public static final AntlrCompilationUnit NOT_FOUND = new AntlrCompilationUnit(
            new CompilationUnitContext(null, -1),
            Optional.empty());

    private AntlrPackage packageState;

    public AntlrCompilationUnit(
            @Nonnull CompilationUnitContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit)
    {
        super(elementContext, compilationUnit);
    }

    @Nonnull
    @Override
    public CompilationUnitContext getElementContext()
    {
        return (CompilationUnitContext) super.getElementContext();
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".omitParentFromSurroundingElements() not implemented yet");
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.empty();
    }

    public void enterPackageDeclaration(AntlrPackage packageState)
    {
        if (this.packageState != null)
        {
            throw new IllegalStateException();
        }
        this.packageState = Objects.requireNonNull(packageState);
    }

    public AntlrPackage getPackage()
    {
        return this.packageState;
    }

    public void reportNameErrors(CompilerErrorState compilerErrorHolder)
    {
        this.packageState.reportNameErrors(compilerErrorHolder);
    }
}
