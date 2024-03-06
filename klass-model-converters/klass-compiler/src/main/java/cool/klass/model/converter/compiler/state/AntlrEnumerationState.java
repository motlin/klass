package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.phase.BuildAntlrStatePhase;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationState
{
    private final EnumerationDeclarationContext                    ctx;
    private final MutableList<AntlrEnumerationLiteralState>        enumerationLiteralStates = Lists.mutable.empty();
    private       MutableMap<String, AntlrEnumerationLiteralState> enumerationLiteralsByName;

    public AntlrEnumerationState(EnumerationDeclarationContext ctx)
    {
        this.ctx = ctx;
    }

    public void addLiteral(
            EnumerationLiteralContext ctx,
            EnumerationPrettyNameContext prettyNameContext,
            String literalName,
            String prettyName)
    {
        AntlrEnumerationLiteralState enumerationLiteralState =
                new AntlrEnumerationLiteralState(ctx, prettyNameContext, literalName, prettyName);
        this.enumerationLiteralStates.add(enumerationLiteralState);
    }

    public void postProcess(BuildAntlrStatePhase buildAntlrStatePhase)
    {
        MutableBag<String> duplicateNames = this.getDuplicateLiteralNames();
        this.checkDuplicateLiteralNames(buildAntlrStatePhase, duplicateNames);
        this.checkDuplicatePrettyNames(buildAntlrStatePhase);

        this.enumerationLiteralsByName = this.enumerationLiteralStates.toMap(
                AntlrEnumerationLiteralState::getLiteralName,
                each ->
                {
                    if (duplicateNames.contains(each.getLiteralName()))
                    {
                        return AntlrEnumerationLiteralState.AMBIGUOUS;
                    }
                    return each;
                });
    }

    protected MutableBag<String> getDuplicateLiteralNames()
    {
        return this.enumerationLiteralStates
                .collect(AntlrEnumerationLiteralState::getLiteralName)
                .toBag()
                .selectByOccurrences(i -> i > 1);
    }

    protected void checkDuplicateLiteralNames(
            BuildAntlrStatePhase buildAntlrStatePhase,
            MutableBag<String> duplicateNames)
    {
        this.enumerationLiteralStates
                .asLazy()
                .select(each -> duplicateNames.contains(each.getLiteralName()))
                .each(each ->
                {
                    String message = String.format("Duplicate enumeration literal: '%s'.", each.getLiteralName());
                    buildAntlrStatePhase.error(message, each.getCtx(), this.ctx);
                });
    }

    protected void checkDuplicatePrettyNames(BuildAntlrStatePhase buildAntlrStatePhase)
    {
        MutableBag<String> duplicatePrettyNames = this.enumerationLiteralStates
                .collect(AntlrEnumerationLiteralState::getPrettyName)
                .reject(Objects::isNull)
                .toBag()
                .selectByOccurrences(i -> i > 1);
        this.enumerationLiteralStates
                .asLazy()
                .select(each -> duplicatePrettyNames.contains(each.getPrettyName()))
                .each(each ->
                {
                    String message = String.format("Duplicate enumeration pretty name: '%s'.", each.getPrettyName());
                    buildAntlrStatePhase.error(message, each.getPrettyNameContext(), this.ctx);
                });
    }
}
