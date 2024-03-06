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

package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;

public class AntlrClassReference
        extends AntlrElement
{
    @Nonnull
    private final AntlrClassReferenceOwner classReferenceOwnerState;
    @Nonnull
    private final AntlrClass               classState;

    public AntlrClassReference(
            @Nonnull ClassReferenceContext classReferenceContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassReferenceOwner classReferenceOwnerState,
            @Nonnull AntlrClass classState)
    {
        super(classReferenceContext, compilationUnit);
        this.classReferenceOwnerState = Objects.requireNonNull(classReferenceOwnerState);
        this.classState               = Objects.requireNonNull(classState);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.classReferenceOwnerState);
    }

    @Nonnull
    public AntlrClass getClassState()
    {
        return this.classState;
    }
}
