package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrOrderBy extends AntlrElement
{
    @Nonnull
    private final AntlrClass thisContext;

    @Nonnull
    private final MutableList<AntlrOrderByProperty> orderByPropertyStates = Lists.mutable.empty();

    public AntlrOrderBy(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass thisContext)
    {
        super(elementContext, compilationUnit, inferred);
        this.thisContext = Objects.requireNonNull(thisContext);
    }

    public int getNumProperties()
    {
        return this.orderByPropertyStates.size();
    }

    public void enterOrderByProperty(AntlrOrderByProperty orderByPropertyState)
    {
        this.orderByPropertyStates.add(orderByPropertyState);
    }
}
