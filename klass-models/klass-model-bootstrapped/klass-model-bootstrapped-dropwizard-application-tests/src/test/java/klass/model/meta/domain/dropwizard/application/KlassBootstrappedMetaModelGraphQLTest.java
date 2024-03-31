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

package klass.model.meta.domain.dropwizard.application;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.extension.match.FileSlurper;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class KlassBootstrappedMetaModelGraphQLTest
        extends AbstractKlassBootstrappedMetaModelApplicationTest
{
    @Test
    public void packageableElements()
    {
        this.assertGraphQL("packageableElements");
    }

    @Test
    public void enumerations()
    {
        this.assertGraphQL("enumerations");
    }

    @Test
    public void interfaces()
    {
        this.assertGraphQL("interfaces");
    }

    @Test
    public void classes()
    {
        this.assertGraphQL("classes");
    }

    @Test
    public void associations()
    {
        this.assertGraphQL("associations");
    }

    @Test
    public void projectionElements()
    {
        this.assertGraphQL("projectionElements");
    }

    @Test
    public void namedProjections()
    {
        this.assertGraphQL("namedProjections");
    }

    @Test
    public void expressionValue()
    {
        this.assertGraphQL("expressionValue");
    }

    @Test
    public void criteria()
    {
        this.assertGraphQL("criteria");
    }

    @Test
    public void serviceGroups()
    {
        this.assertGraphQL("serviceGroups");
    }

    private void assertGraphQL(String testName)
    {
        Client client   = this.getClient(testName);

        Class<?> callingClass   = this.getClass();
        String   sourceName     = callingClass.getSimpleName() + "." + testName + ".graphql";
        String   query = FileSlurper.slurp(sourceName, callingClass);

        Response response = client.target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", query)));

        this.assertResponse(testName, Status.OK, response);
    }
}
