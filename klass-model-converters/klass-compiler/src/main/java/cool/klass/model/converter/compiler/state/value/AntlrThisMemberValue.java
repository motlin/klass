package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.meta.domain.value.ThisMemberExpressionValue.ThisMemberExpressionValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrThisMemberValue extends AntlrMemberExpressionValue
{
    public AntlrThisMemberValue(
            @Nonnull ThisMemberReferenceContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred, classState, dataTypePropertyState);
    }

    @Nonnull
    @Override
    public ThisMemberReferenceContext getElementContext()
    {
        return (ThisMemberReferenceContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public ThisMemberExpressionValueBuilder build()
    {
        return new ThisMemberExpressionValueBuilder(
                this.elementContext,
                this.classState.getKlassBuilder(),
                this.dataTypePropertyState.getPropertyBuilder());
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.dataTypePropertyState == AntlrEnumerationProperty.NOT_FOUND)
        {
            IdentifierContext identifier = this.getElementContext().memberReference().identifier();
            String message = String.format(
                    "Cannot find member '%s.%s'.",
                    this.classState.getName(),
                    identifier.getText());
            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    identifier,
                    parserRuleContexts.toArray(new ParserRuleContext[]{}));
        }
    }

    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        AntlrType type = this.dataTypePropertyState.getType();
        if (type == AntlrEnumeration.NOT_FOUND)
        {
            return Lists.immutable.empty();
        }
        return Lists.immutable.with(type);
    }
}
