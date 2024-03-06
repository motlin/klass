/*
 * Copyright 2020 Craig Motlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassReference;
import cool.klass.model.converter.compiler.state.AntlrClassReferenceOwner;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;

public class ReferencePropertyPhase
        extends AbstractCompilerPhase
{
    @Nullable
    protected AntlrClassReferenceOwner classReferenceOwnerState;
    @Nullable
    protected AntlrMultiplicityOwner   multiplicityOwnerState;

    public ReferencePropertyPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    public void handleClassReference(@Nonnull ClassReferenceContext ctx)
    {
        if (this.classReferenceOwnerState == null)
        {
            return;
        }

        String           className        = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClass       classState       = domainModelState.getClassByName(className);
        AntlrClassReference classReferenceState = new AntlrClassReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classReferenceOwnerState,
                classState);

        this.classReferenceOwnerState.enterClassReference(classReferenceState);
    }

    public void handleMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        if (this.multiplicityOwnerState == null)
        {
            return;
        }

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.multiplicityOwnerState);

        this.multiplicityOwnerState.enterMultiplicity(multiplicityState);
    }
}
