package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

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

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.empty();
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        PackageDeclarationContext context = this.getElementContext().packageDeclaration();
        return Tuples.pair(context.getStart(), context.getStop());
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

    @Override
    public String toString()
    {
        return this.packageState.toString();
    }
}
