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

public abstract class ClassifierDTO
        extends PackageableElementDTO
{
    private final ImmutableList<SuperInterfaceMappingDTO> superInterfaces;
    private final ImmutableList<ModifierDTO>              classifierModifiers;
    private final ImmutableList<PrimitivePropertyDTO>     primitiveProperties;
    private final ImmutableList<EnumerationPropertyDTO>   enumerationProperties;

    protected ClassifierDTO(
            String name,
            String packageName,
            ImmutableList<SuperInterfaceMappingDTO> superInterfaces,
            ImmutableList<ModifierDTO> classifierModifiers,
            ImmutableList<PrimitivePropertyDTO> primitiveProperties,
            ImmutableList<EnumerationPropertyDTO> enumerationProperties)
    {
        super(name, packageName);
        this.superInterfaces       = superInterfaces;
        this.classifierModifiers   = classifierModifiers;
        this.primitiveProperties   = primitiveProperties;
        this.enumerationProperties = enumerationProperties;
    }

    public ImmutableList<SuperInterfaceMappingDTO> getSuperInterfaces()
    {
        return this.superInterfaces;
    }

    public ImmutableList<ModifierDTO> getClassifierModifiers()
    {
        return this.classifierModifiers;
    }

    public ImmutableList<PrimitivePropertyDTO> getPrimitiveProperties()
    {
        return this.primitiveProperties;
    }

    public ImmutableList<EnumerationPropertyDTO> getEnumerationProperties()
    {
        return this.enumerationProperties;
    }
}
