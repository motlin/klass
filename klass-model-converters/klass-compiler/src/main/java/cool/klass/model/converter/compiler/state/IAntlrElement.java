package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.SourceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface IAntlrElement
{
    @Nonnull
    ParserRuleContext getElementContext();

    @Nonnull
    default SourceContext getSourceContext()
    {
        return new SourceContext(this.getCompilationUnit().get(), this.getElementContext());
    }

    @Nonnull
    Optional<AntlrElement> getMacroElement();

    default IAntlrElement getOuterElement()
    {
        return this.getSurroundingElement()
                .map(IAntlrElement::getOuterElement)
                .orElse(this);
    }

    @Nonnull
    ImmutableList<ParserRuleContext> getParserRuleContexts();

    void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts);

    boolean omitParentFromSurroundingElements();

    @Nonnull
    Optional<IAntlrElement> getSurroundingElement();

    @Nonnull
    default ImmutableList<IAntlrElement> getSurroundingElementsIncludingThis()
    {
        return this.gatherSurroundingElements(Lists.mutable.with(this));
    }

    @Nonnull
    default ImmutableList<IAntlrElement> getSurroundingElements()
    {
        return this.gatherSurroundingElements(Lists.mutable.empty());
    }

    @Nonnull
    default ImmutableList<IAntlrElement> gatherSurroundingElements(@Nonnull MutableList<IAntlrElement> result)
    {
        boolean omitParent = this.omitParentFromSurroundingElements();
        this.getSurroundingElement().ifPresent(element -> element.gatherSurroundingElements(result, omitParent));
        return result.toImmutable();
    }

    default void gatherSurroundingElements(@Nonnull MutableList<IAntlrElement> result, boolean omitThis)
    {
        if (!omitThis)
        {
            result.add(this);
        }
        this.getSurroundingElement().ifPresent(element -> element.gatherSurroundingElements(
                result,
                this.omitParentFromSurroundingElements()));
    }

    @Nonnull
    Optional<CompilationUnit> getCompilationUnit();

    @Nonnull
    String getSourceCode();
}
