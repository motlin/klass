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

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;

public class AssociationEndErrorContext implements ErrorContext
{
    private final AssociationEnd        associationEnd;
    private final ImmutableList<Object> keys;

    public AssociationEndErrorContext(AssociationEnd associationEnd, ImmutableList<Object> keys)
    {
        this.associationEnd = Objects.requireNonNull(associationEnd);
        this.keys = Objects.requireNonNull(keys);
    }

    @Override
    public String toString()
    {
        ImmutableList<DataTypeProperty> keyProperties = this.associationEnd.getType().getKeyProperties();
        if (keyProperties.size() != this.keys.size())
        {
            throw new AssertionError();
        }

        String keysContext = keyProperties
                .asLazy()
                .zip(this.keys)
                .collect(AssociationEndErrorContext::toString)
                .makeString();

        String format = this.associationEnd.getMultiplicity().isToMany() ? "%s[%s]" : "%s{%s}";

        return String.format(
                format,
                this.associationEnd.getName(),
                keysContext);
    }

    private static String toString(@Nonnull Pair<DataTypeProperty, Object> pair)
    {
        DataTypeProperty dataTypeProperty = pair.getOne();
        Object           key              = pair.getTwo();
        return String.format("%s=%s", dataTypeProperty.getName(), key);
    }
}
