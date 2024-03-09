package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.meta.domain.AbstractPackageableElement.PackageableElementBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrPackageableElement
        extends AntlrIdentifierElement
{
    @Nonnull
    protected final AntlrCompilationUnit compilationUnitState;

    protected AntlrPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull AntlrCompilationUnit compilationUnitState,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, compilationUnitState.getCompilationUnit(), ordinal, nameContext);
        this.compilationUnitState = Objects.requireNonNull(compilationUnitState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.compilationUnitState);
    }

    @Nonnull
    public String getPackageName()
    {
        return this.compilationUnitState.getPackage().getName();
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        return TYPE_NAME_PATTERN;
    }

    @Nonnull
    @Override
    public PackageableElementBuilder<?> getElementBuilder()
    {
        return (PackageableElementBuilder<?>) super.getElementBuilder();
    }

    public abstract void reportErrors(CompilerAnnotationHolder compilerAnnotationHolder);
}
