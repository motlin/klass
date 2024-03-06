/*
 * Copyright 2023 Craig Motlin
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

package io.liftwizard.reladomo.test.resource.writer.tests;

import java.sql.Connection;
import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import io.liftwizard.reladomo.connectionmanager.h2.memory.H2InMemoryConnectionManager;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class LiquibaseTestRule
        implements TestRule
{
    @Nonnull
    private final Supplier<? extends Connection> connectionSupplier = () -> H2InMemoryConnectionManager
            .getInstance()
            .getConnection();

    private       final String                         migrationsFile;

    public LiquibaseTestRule(String migrationsFile)
    {
        this.migrationsFile = Objects.requireNonNull(migrationsFile);
    }

    @Override
    public Statement apply(Statement base, Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate()
                    throws Throwable
            {
                try (Connection connection = LiquibaseTestRule.this.connectionSupplier.get())
                {
                    try (
                            CloseableLiquibase liquibase = LiquibaseTestRule.this.openLiquibase(
                                    connection,
                                    null,

                                    null,
                                    LiquibaseTestRule.this.migrationsFile))
                    {
                        if (false)
                        {
                            liquibase.dropAll();
                        }
                        String context = "";
                        liquibase.update(context);
                    }
                    catch (LiquibaseException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                base.evaluate();
            }
        };
    }

    private CloseableLiquibase openLiquibase(
            Connection connection,
            String catalogName,
            String schemaName,
            String migrationsFile)
            throws LiquibaseException
    {
        Database         database         = this.createDatabase(connection, catalogName, schemaName);
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
        return new CloseableLiquibase(migrationsFile, resourceAccessor, database);
    }

    private Database createDatabase(Connection connection, String catalogName, String schemaName)
            throws LiquibaseException
    {
        DatabaseConnection conn     = new JdbcConnection(connection);
        Database           database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);

        if (database.supportsCatalogs() && catalogName != null)
        {
            database.setDefaultCatalogName(catalogName);
            database.setOutputDefaultCatalog(true);
        }
        if (database.supportsSchemas() && schemaName != null)
        {
            database.setDefaultSchemaName(schemaName);
            database.setOutputDefaultSchema(true);
        }

        return database;
    }
}
