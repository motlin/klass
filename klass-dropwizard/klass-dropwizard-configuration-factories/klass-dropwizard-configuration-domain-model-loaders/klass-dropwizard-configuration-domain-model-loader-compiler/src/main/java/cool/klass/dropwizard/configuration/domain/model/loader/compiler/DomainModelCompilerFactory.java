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

package cool.klass.dropwizard.configuration.domain.model.loader.compiler;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.hibernate.validator.constraints.NotEmpty;

@JsonTypeName("compiler")
@AutoService(DomainModelFactory.class)
public class DomainModelCompilerFactory
        implements DomainModelFactory
{
    @NotEmpty
    private @Valid @NotNull List<String> sourcePackages = Arrays.asList("klass.model.meta.domain");

    private DomainModelWithSourceCode domainModel;

    @Nonnull
    @Override
    public DomainModelWithSourceCode createDomainModel(ObjectMapper objectMapper)
    {
        if (this.domainModel != null)
        {
            return this.domainModel;
        }
        ImmutableList<String> klassSourcePackagesImmutable = Lists.immutable.withAll(this.sourcePackages);
        DomainModelCompilerLoader domainModelLoader = new DomainModelCompilerLoader(
                klassSourcePackagesImmutable,
                Thread.currentThread().getContextClassLoader());
        this.domainModel = domainModelLoader.load();
        return this.domainModel;
    }

    @JsonProperty
    public List<String> getSourcePackages()
    {
        return Lists.mutable.withAll(this.sourcePackages);
    }

    @JsonProperty
    public void setSourcePackages(List<String> sourcePackages)
    {
        this.sourcePackages = sourcePackages;
    }
}
