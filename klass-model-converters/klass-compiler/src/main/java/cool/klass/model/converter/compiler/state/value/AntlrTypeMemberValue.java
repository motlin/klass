package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.meta.domain.value.TypeMemberExpressionValue.TypeMemberExpressionValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrTypeMemberValue extends AntlrMemberExpressionValue
{
    public AntlrTypeMemberValue(
            @Nonnull TypeMemberReferenceContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred, classState, dataTypePropertyState);
    }

    @Nonnull
    @Override
    public TypeMemberExpressionValueBuilder build()
    {
        return new TypeMemberExpressionValueBuilder(
                this.elementContext,
                this.classState.getKlassBuilder(),
                this.dataTypePropertyState.getPropertyBuilder());
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.classState == AntlrClass.AMBIGUOUS || this.classState == AntlrClass.NOT_FOUND)
        {
            ClassReferenceContext offendingToken = this.getElementContext().classReference();

            String message = String.format(
                    "ERR_MEM_TYP: Cannot find class '%s'.",
                    offendingToken.getText());

            compilerErrorHolder.add(this.compilationUnit, message, offendingToken);
        }
        else if (this.dataTypePropertyState == AntlrEnumerationProperty.NOT_FOUND)
        {
            MemberReferenceContext offendingToken = this.getElementContext().memberReference();

            String message = String.format(
                    "ERR_MEM_MEM: Cannot find member '%s'.",
                    offendingToken.getText());

            compilerErrorHolder.add(this.compilationUnit, message, offendingToken);
        }
    }

    @Nonnull
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

    @Nonnull
    @Override
    public TypeMemberReferenceContext getElementContext()
    {
        return (TypeMemberReferenceContext) super.getElementContext();
    }
}
