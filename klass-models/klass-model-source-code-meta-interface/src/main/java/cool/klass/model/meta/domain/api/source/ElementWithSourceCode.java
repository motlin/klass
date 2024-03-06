package cool.klass.model.meta.domain.api.source;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public interface ElementWithSourceCode
        extends Element
{
    @Nonnull
    SourceCode getSourceCodeObject();

    ParserRuleContext getElementContext();
}
