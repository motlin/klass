package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.TypeMemberExpressionValue.TypeMemberExpressionValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;

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
}
