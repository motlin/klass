package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.VersionsContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class VersionReferencePhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    public VersionReferencePhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterVersions(@Nonnull VersionsContext ctx)
    {
        ClassReferenceContext classReferenceContext = ctx.classReference();
        String                className             = classReferenceContext.getText();
        AntlrClass            classState            = this.domainModelState.getClassByName(className);

        if (this.classDeclarationContext != null)
        {
            AntlrClass versionClass = this.domainModelState.getClassByContext(this.classDeclarationContext);
            classState.addVersionClass(versionClass);
            versionClass.setVersionedClass(classState);
        }

        if (this.associationDeclarationContext != null)
        {
            AntlrAssociation versionAssociation = this.domainModelState.getAssociationByContext(this.associationDeclarationContext);
            // TODO: Error check that the association version really versions something
            classState.addVersionAssociation(versionAssociation);
            versionAssociation.setVersionClass(classState);
        }
    }
}
