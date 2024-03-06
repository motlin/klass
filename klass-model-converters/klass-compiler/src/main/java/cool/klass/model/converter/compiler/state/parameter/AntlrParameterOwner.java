package cool.klass.model.converter.compiler.state.parameter;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface AntlrParameterOwner
{
    // TODO: ❗ Move this to AntlrElement
    default ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> result = Lists.mutable.empty();
        this.getParserRuleContexts(result);
        return result.toImmutable();
    }

    void getParserRuleContexts(MutableList<ParserRuleContext> parserRuleContexts);

    int getNumParameters();

    void enterPrimitiveParameterDeclaration(AntlrPrimitiveParameter primitiveParameterState);

    void enterEnumerationParameterDeclaration(AntlrEnumerationParameter enumerationParameterState);
}
