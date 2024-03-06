package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public abstract class AntlrPackageableElement
        extends AntlrNamedElement
{
    @Nonnull
    protected final AntlrCompilationUnit compilationUnitState;

    protected AntlrPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull AntlrCompilationUnit compilationUnitState)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
        this.compilationUnitState = Objects.requireNonNull(compilationUnitState);

        if (this.compilationUnitState.getElementContext() != this.compilationUnitState.getElementContext())
        {
            throw new AssertionError();
        }
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

    @Override
    public final void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        parserRuleContexts.add(this.compilationUnit.get().getParserContext());
    }
}
