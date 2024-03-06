package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public class RelationshipInferencePhase
        extends AbstractCompilerPhase
{
    private static final Converter<String, String> LOWER_CAMEL_TO_UPPER_CAMEL =
            CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    private static final Converter<String, String> UPPER_TO_LOWER_CAMEL =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    public RelationshipInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Association relationship";
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);

        RelationshipContext relationship = ctx.associationBodyDeclaration().associationBody().relationship();
        if (relationship == null)
        {
            AntlrAssociation association = this.compilerState.getCompilerWalk().getAssociation();
            if (association.isManyToMany())
            {
                return;
            }

            AntlrAssociationEnd sourceEnd = association.getSourceEnd();
            AntlrAssociationEnd targetEnd = association.getTargetEnd();

            if (targetEnd.isToOne() && sourceEnd.isToMany() || sourceEnd.isToOne() && sourceEnd.isOwned())
            {
                this.handleSourceAssociationEnd(sourceEnd);
            }
            else if (sourceEnd.isToOne() && targetEnd.isToMany() || targetEnd.isToOne() && targetEnd.isOwned())
            {
                this.handleTargetAssociationEnd(targetEnd);
            }
            else if (sourceEnd.isToOne() && targetEnd.isToOneRequired())
            {
                this.handleSourceAssociationEnd(sourceEnd);
            }
            else if (targetEnd.isToOne() && sourceEnd.isToOneRequired())
            {
                this.handleTargetAssociationEnd(targetEnd);
            }
            else
            {
                throw new IllegalStateException("Unhandled association end combination: " + association);
            }
        }
    }

    private void handleSourceAssociationEnd(AntlrAssociationEnd associationEnd)
    {
        AntlrClass oppositeType = associationEnd.getOpposite().getType();

        String sourceCodeText = oppositeType
                .getAllKeyProperties()
                .collect(each -> "this.%s%s == %s.%s".formatted(
                        UPPER_TO_LOWER_CAMEL.convert(oppositeType.getName()),
                        LOWER_CAMEL_TO_UPPER_CAMEL.convert(each.getName()),
                        oppositeType.getName(),
                        each.getName()))
                .makeString("relationship ", " && ", "");

        this.runCompilerMacro(sourceCodeText);
    }

    private void handleTargetAssociationEnd(AntlrAssociationEnd associationEnd)
    {
        AntlrClass oppositeType = associationEnd.getOpposite().getType();

        String sourceCodeText = oppositeType
                .getAllKeyProperties()
                .collect(each -> "this.%s == %s.%s%s".formatted(
                        each.getName(),
                        associationEnd.getType().getName(),
                        UPPER_TO_LOWER_CAMEL.convert(oppositeType.getName()),
                        LOWER_CAMEL_TO_UPPER_CAMEL.convert(each.getName())))
                .makeString("relationship ", " && ", "");

        this.runCompilerMacro(sourceCodeText);
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        AntlrAssociation association = this.compilerState.getCompilerWalk().getAssociation();

        ParseTreeListener compilerPhase = new RelationshipPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                association,
                this,
                sourceCodeText,
                KlassParser::relationship,
                compilerPhase);
    }
}
