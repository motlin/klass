package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.domain.property.Property.PropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<T extends Type> extends AntlrNamedElement
{
    public static final AntlrProperty<Type> AMBIGUOUS = new AntlrProperty<Type>(
            Element.NO_CONTEXT,
            null,
            true,
            "ambiguous property",
            Element.NO_CONTEXT)
    {
        public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
        {
            String message = String.format("ERR_DUP_MEM: Duplicate member: '%s'.", this.name);

            ParserRuleContext[] parserRuleContexts = this.elementContext instanceof ClassModifierContext
                    ? new ParserRuleContext[]{}
                    : new ParserRuleContext[]{this.getOwningClassState().getElementContext()};
            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    this.nameContext,
                    parserRuleContexts);
        }

        @Nonnull
        @Override
        public PropertyBuilder<Type, ?> build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented");
        }

        @Nonnull
        @Override
        protected AntlrClass getOwningClassState()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getOwningClassState() not implemented yet");
        }
    };

    protected AntlrProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String name,
            @Nonnull ParserRuleContext nameContext)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
    }

    @Nonnull
    @Override
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Nonnull
    @Override
    public String getName()
    {
        return this.name;
    }

    public abstract PropertyBuilder<T, ?> build();

    protected abstract AntlrClass getOwningClassState();
}
