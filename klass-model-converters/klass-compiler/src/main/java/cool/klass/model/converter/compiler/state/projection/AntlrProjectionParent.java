package cool.klass.model.converter.compiler.state.projection;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrProjectionParent extends AntlrNamedElement
{
    @Nonnull
    protected final AntlrClass klass;

    protected final MutableList<AntlrProjectionChild> children = Lists.mutable.empty();

    protected final MutableOrderedMap<String, AntlrProjectionChild> childrenByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    protected AntlrProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.klass = Objects.requireNonNull(klass);
    }

    @Override
    @Nonnull
    public abstract AbstractProjectionParentBuilder<? extends AbstractProjectionParent> getElementBuilder();

    public MutableList<AntlrProjectionChild> getChildren()
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

    public void enterAntlrProjectionMember(@Nonnull AntlrProjectionChild child)
    {
        this.children.add(child);
        this.childrenByName.compute(
                child.getName(),
                (name, builder) -> builder == null
                        ? child
                        : AntlrProjectionDataTypeProperty.AMBIGUOUS);
    }
}
