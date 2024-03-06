package cool.klass.model.converter.compiler.state.value;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;

public class AntlrThisMemberValue extends AntlrMemberExpressionValue
{
    public AntlrThisMemberValue(
            ThisMemberReferenceContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrClass classState,
            AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred, classState, dataTypePropertyState);
    }
}
