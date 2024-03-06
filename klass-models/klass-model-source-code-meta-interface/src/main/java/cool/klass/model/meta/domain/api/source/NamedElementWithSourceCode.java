package cool.klass.model.meta.domain.api.source;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.NamedElement;
import org.antlr.v4.runtime.Token;

public interface NamedElementWithSourceCode
        extends NamedElement, ElementWithSourceCode
{
    @Nonnull
    Token getNameToken();
}
