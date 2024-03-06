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
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;

public class AntlrClassifierReference
        extends AntlrElement
{
    @Nonnull
    private final AntlrClassifierReferenceOwner classifierReferenceOwnerState;
    @Nonnull
    private final AntlrClassifier               classifierState;

    public AntlrClassifierReference(
            @Nonnull ClassifierReferenceContext classifierReferenceContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifierReferenceOwner classifierReferenceOwnerState,
            @Nonnull AntlrClassifier classifierState)
    {
        super(classifierReferenceContext, compilationUnit);
        this.classifierReferenceOwnerState = Objects.requireNonNull(classifierReferenceOwnerState);
        this.classifierState               = Objects.requireNonNull(classifierState);
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
        return Optional.of(this.classifierReferenceOwnerState);
    }

    @Nonnull
    public AntlrClassifier getClassifierState()
    {
        return this.classifierState;
    }
}
