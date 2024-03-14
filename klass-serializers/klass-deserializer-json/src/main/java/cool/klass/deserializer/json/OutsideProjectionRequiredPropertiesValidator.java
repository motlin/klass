/*
 * Copyright 2024 Craig Motlin
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

package cool.klass.deserializer.json;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class OutsideProjectionRequiredPropertiesValidator
        extends RequiredPropertiesValidator
{
    public OutsideProjectionRequiredPropertiesValidator(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        super(klass, objectNode, operationMode, errors, warnings, contextStack, pathHere, isRoot, false);
    }

    @Override
    protected void handleAssociationEndOutsideProjection(AssociationEnd associationEnd)
    {
        if (!associationEnd.isRequired())
        {
            return;
        }

        this.handleAnnotationIfPresent(associationEnd, "outside projection");
    }

    @Override
    protected void handlePlainAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode nextMode)
    {
        RequiredPropertiesValidator validator = new OutsideProjectionRequiredPropertiesValidator(
                associationEnd.getType(),
                objectNode,
                nextMode,
                this.errors,
                this.warnings,
                this.contextStack,
                Optional.of(associationEnd),
                false);
        validator.validate();
    }
}
