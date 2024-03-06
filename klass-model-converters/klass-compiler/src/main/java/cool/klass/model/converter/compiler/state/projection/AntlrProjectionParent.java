package cool.klass.model.converter.compiler.state.projection;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrProjectionParent extends AntlrNamedElement
{
    @Nonnull
    protected final AntlrClass klass;

    protected final MutableList<AntlrProjectionElement> children = Lists.mutable.empty();

    protected final MutableOrderedMap<String, AntlrProjectionElement> childrenByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    protected AntlrProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.klass = Objects.requireNonNull(klass);
    }

    public MutableList<AntlrProjectionElement> getChildren()
    {
        return this.children.asUnmodifiable();
    }

    @Nonnull
    public AntlrClass getKlass()
    {
        return this.klass;
    }

    public int getNumChildren()
    {
        return this.children.size();
    }

    public void enterAntlrProjectionMember(@Nonnull AntlrProjectionElement child)
    {
        this.children.add(child);
        this.childrenByName.compute(
                child.getName(),
                (name, builder) -> builder == null
                        ? child
                        : AntlrProjectionDataTypeProperty.AMBIGUOUS);
    }
}
