package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.phase.BuildAntlrStatePhase;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrClass
{
    private final ClassDeclarationContext ctx;

    private final MutableList<AntlrProperty<?>>       antlrProperties          = Lists.mutable.empty();
    private final MutableList<AntlrPrimitiveProperty> antlrPrimitiveProperties = Lists.mutable.empty();

    public AntlrClass(ClassDeclarationContext ctx)
    {
        this.ctx = ctx;
    }

    public void add(AntlrPrimitiveProperty antlrPrimitiveProperty)
    {
        this.antlrPrimitiveProperties.add(antlrPrimitiveProperty);
        this.antlrProperties.add(antlrPrimitiveProperty);
    }

    public void postProcess(BuildAntlrStatePhase buildAntlrStatePhase)
    {
        MutableBag<String> duplicateNames = this.getDuplicateMemberNames();
        this.checkDuplicateMemberNames(buildAntlrStatePhase, duplicateNames);
    }

    private MutableBag<String> getDuplicateMemberNames()
    {
        return this.antlrProperties
                .asLazy()
                .collect(AntlrProperty::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);
    }

    private void checkDuplicateMemberNames(
            BuildAntlrStatePhase buildAntlrStatePhase,
            MutableBag<String> duplicateNames)
    {
        this.antlrProperties
                .asLazy()
                .select(each -> duplicateNames.contains(each.getName()))
                .each(each ->
                {
                    String message = String.format("Duplicate member: '%s'.", each.getName());
                    buildAntlrStatePhase.error(message, each.getCtx(), this.ctx);
                });
    }
}
