package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<T extends Type> extends AntlrNamedElement
{
    public static final AntlrProperty<Type> AMBIGUOUS = new AntlrProperty<Type>(
            Element.NO_CONTEXT,
            null,
            "ambiguous property",
            Element.NO_CONTEXT)
    {
        @Override
        public PropertyBuilder<Type, ?> build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented");
        }

        @Override
        protected AntlrClass getOwningClassState()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                    + ".getOwningClassState() not implemented yet");
        }
    };

    protected AntlrProperty(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            String name,
            ParserRuleContext nameContext)
    {
        super(elementContext, compilationUnit, name, nameContext);
    }

    @Override
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public abstract PropertyBuilder<T, ?> build();

    public void reportDuplicateMemberName(CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_MEM: Duplicate member: '%s'.", this.name);

        ParserRuleContext[] parserRuleContexts = this.elementContext instanceof ClassModifierContext
                ? new ParserRuleContext[]{}
                : new ParserRuleContext[]{this.getOwningClassState().getElementContext()};
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.elementContext,
                parserRuleContexts);
    }

    protected abstract AntlrClass getOwningClassState();
}
