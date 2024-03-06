package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.phase.BuildAntlrStatePhase;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrClass
{
    private final String                  packageName;
    private final ClassDeclarationContext ctx;

    private final MutableList<AntlrProperty<?, ?>> propertyStates = Lists.mutable.empty();

    private KlassBuilder                            klassBuilder;
    private MutableMap<String, AntlrProperty<?, ?>> propertiesByName;

    public AntlrClass(String packageName, ClassDeclarationContext ctx)
    {
        this.packageName = packageName;
        this.ctx = ctx;
    }

    public void add(AntlrPrimitiveProperty antlrPrimitiveProperty)
    {
        this.propertyStates.add(antlrPrimitiveProperty);
    }

    public void postProcess(BuildAntlrStatePhase buildAntlrStatePhase)
    {
        MutableBag<String> duplicateNames = this.getDuplicateMemberNames();
        this.checkDuplicateMemberNames(buildAntlrStatePhase, duplicateNames);

        this.propertiesByName = this.propertyStates.toMap(
                AntlrProperty::getName,
                each ->
                {
                    if (duplicateNames.contains(each.getName()))
                    {
                        return AntlrProperty.AMBIGUOUS;
                    }
                    return each;
                });
    }

    private MutableBag<String> getDuplicateMemberNames()
    {
        return this.propertyStates
                .asLazy()
                .collect(AntlrProperty::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);
    }

    private void checkDuplicateMemberNames(
            BuildAntlrStatePhase buildAntlrStatePhase,
            MutableBag<String> duplicateNames)
    {
        this.propertyStates
                .asLazy()
                .select(each -> duplicateNames.contains(each.getName()))
                .each(each ->
                {
                    String message = String.format("Duplicate member: '%s'.", each.getName());
                    buildAntlrStatePhase.error(message, each.getCtx(), this.ctx);
                });
    }

    public void build(DomainModelBuilder domainModelBuilder)
    {
        IdentifierContext identifier = this.ctx.identifier();
        this.klassBuilder = new KlassBuilder(this.ctx, identifier, identifier.getText(), this.packageName);
        ImmutableList<PropertyBuilder<?>> propertyBuilders = this.propertyStates
                .<KlassBuilder, PropertyBuilder<?>>collectWith(AntlrProperty::build, this.klassBuilder)
                .toImmutable();
        this.klassBuilder.setProperties(propertyBuilders);
        domainModelBuilder.klass(this.klassBuilder);
    }
}
