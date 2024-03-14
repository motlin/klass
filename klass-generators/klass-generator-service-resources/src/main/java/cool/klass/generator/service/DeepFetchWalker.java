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

package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public class DeepFetchWalker
{
    private final MutableStack<AssociationEnd> associationEndStack = Stacks.mutable.empty();

    private final MutableStack<String> stringStack = Stacks.mutable.empty();
    private final MutableList<String>  result      = Lists.mutable.empty();

    private final Klass klass;

    public DeepFetchWalker(Klass klass)
    {
        this.klass = Objects.requireNonNull(klass);
    }

    public ImmutableList<String> getResult()
    {
        return this.result.toImmutable();
    }

    // TODO: Figure out how to deep fetch polymorphic projection properties

    public void walk()
    {
        this.klass.getAssociationEnds()
                .select(AssociationEnd::isOwned)
                .each(this::handleAssociationEnd);
    }

    public static ImmutableList<String> walk(Klass klass)
    {
        DeepFetchWalker deepFetchWalker = new DeepFetchWalker(klass);
        deepFetchWalker.walk();
        return deepFetchWalker.getResult();
    }

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        this.associationEndStack.push(associationEnd);
        this.stringStack.push(associationEnd.getName());

        // TODO: Figure out how to deep fetch polymorphic projection properties
        if (this.isLeaf(associationEnd))
        {
            String string = this.stringStack
                    .toList()
                    .asReversed()
                    .collect(each -> each + "()")
                    .makeString(".");
            String navigation = String.format("%sFinder.%s", this.klass.getName(), string);
            this.result.add(navigation);
        }
        this.associationEndStack.pop();
        this.stringStack.pop();
    }

    private boolean isLeaf(@Nonnull AssociationEnd associationEnd)
    {
        return associationEnd.getType()
                .getAssociationEnds()
                .noneSatisfy(AssociationEnd::isOwned);
    }
}
