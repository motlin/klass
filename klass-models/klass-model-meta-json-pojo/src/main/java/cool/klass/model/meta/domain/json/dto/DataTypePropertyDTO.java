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

package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public abstract class DataTypePropertyDTO
        extends NamedElementDTO
{
    private final boolean                    optional;
    private final ImmutableList<ModifierDTO> propertyModifiers;
    private final ValidationDTO              minLengthValidation;
    private final ValidationDTO              maxLengthValidation;

    protected DataTypePropertyDTO(
            String name,
            boolean optional,
            ImmutableList<ModifierDTO> propertyModifiers,
            ValidationDTO minLengthValidation, ValidationDTO maxLengthValidation)
    {
        super(name);
        this.optional            = optional;
        this.propertyModifiers   = propertyModifiers;
        this.minLengthValidation = minLengthValidation;
        this.maxLengthValidation = maxLengthValidation;
    }

    public boolean getOptional()
    {
        return this.optional;
    }

    public ImmutableList<ModifierDTO> getPropertyModifiers()
    {
        return this.propertyModifiers;
    }

    public ValidationDTO getMinLengthValidation()
    {
        return this.minLengthValidation;
    }

    public ValidationDTO getMaxLengthValidation()
    {
        return this.maxLengthValidation;
    }
}
