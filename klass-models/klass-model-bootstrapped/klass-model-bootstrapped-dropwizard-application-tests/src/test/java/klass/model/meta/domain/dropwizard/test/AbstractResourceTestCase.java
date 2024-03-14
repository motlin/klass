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

package klass.model.meta.domain.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.liftwizard.dropwizard.testing.junit.AbstractDropwizardAppTest;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;

public abstract class AbstractResourceTestCase
        extends AbstractDropwizardAppTest
{
    @Nonnull
    @Override
    protected DropwizardAppRule getDropwizardAppRule()
    {
        return new DropwizardAppRule<>(
                KlassBootstrappedMetaModelApplication.class,
                ResourceHelpers.resourceFilePath("config-test.json5"));
    }

    protected void assertUrlReturns(@Nonnull String testName, @Nonnull String url)
    {
        Class<?> klass      = this.getClass();
        String   clientName = klass.getPackage().getName() + '.' + klass.getSimpleName() + '.' + testName;
        Client   client     = this.getClient(clientName);
        Response response = client
                .target("http://localhost:{port}/api/" + url)
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);

        String resourceClassPathLocation = klass.getSimpleName() + '.' + testName + ".json";
        this.jsonMatchRule.assertFileContents(resourceClassPathLocation, jsonResponse);
    }
}
