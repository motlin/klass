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

package cool.klass.deserializer.json.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;

public class AssociationEndWithIndexErrorContext implements ErrorContext
{
    private final AssociationEnd        associationEnd;
    private final int                   index;
    private final ImmutableList<Object> keys;

    public AssociationEndWithIndexErrorContext(
            AssociationEnd associationEnd,
            int index,
            ImmutableList<Object> keys)
    {
        this.associationEnd = Objects.requireNonNull(associationEnd);
        this.index = index;
        this.keys = keys;
    }

    public AssociationEnd getAssociationEnd()
    {
        return this.associationEnd;
    }

    public int getIndex()
    {
        return this.index;
    }

    public ImmutableList<Object> getKeys()
    {
        return this.keys;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s%s(%d)",
                this.associationEnd.getName(),
                this.keys,
                this.index);
    }
}
