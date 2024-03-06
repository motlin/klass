package cool.klass.model.converter.compiler.phase;

import java.util.List;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ExtendsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ImplementsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;

public class InheritancePhase extends AbstractCompilerPhase
{
    public InheritancePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterExtendsDeclaration(@Nonnull ExtendsDeclarationContext ctx)
    {
        super.enterExtendsDeclaration(ctx);

        ClassReferenceContext classReferenceContext = ctx.classReference();
        IdentifierContext     identifier            = classReferenceContext.identifier();
        String                className             = identifier.getText();
        AntlrClass            superClassState       = this.compilerState.getDomainModelState().getClassByName(className);

        AntlrClass classState = this.compilerState.getCompilerWalkState().getClassState();
        classState.enterExtendsDeclaration(superClassState);
    }

    @Override
    public void enterImplementsDeclaration(@Nonnull ImplementsDeclarationContext ctx)
    {
        super.enterImplementsDeclaration(ctx);

        AntlrClassifier classifierState = this.compilerState.getCompilerWalkState().getClassifierState();

        List<InterfaceReferenceContext> interfaceReferenceContexts = ctx.interfaceReference();
        for (InterfaceReferenceContext interfaceReferenceContext : interfaceReferenceContexts)
        {
            IdentifierContext identifier     = interfaceReferenceContext.identifier();
            String            interfaceName  = identifier.getText();
            AntlrInterface    interfaceState = this.compilerState.getDomainModelState().getInterfaceByName(interfaceName);

            classifierState.enterImplementsDeclaration(interfaceState);
        }
    }
}
