package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.ThisMemberExpressionValue.ThisMemberExpressionValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;

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
    public ThisMemberExpressionValueBuilder build()
    {
        return new ThisMemberExpressionValueBuilder(
                this.elementContext,
                this.classState.getKlassBuilder(),
                this.dataTypePropertyState.getPropertyBuilder());
    }
}
