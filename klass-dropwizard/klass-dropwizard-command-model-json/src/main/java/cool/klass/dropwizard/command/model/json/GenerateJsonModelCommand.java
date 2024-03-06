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

package cool.klass.dropwizard.command.model.json;

import java.nio.file.Paths;
import java.time.Clock;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactoryProvider;
import cool.klass.generator.meta.json.JsonMetaModelGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.configuration.clock.ClockFactoryProvider;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateJsonModelCommand<T extends Configuration>
        extends EnvironmentCommand<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateJsonModelCommand.class);

    private final Class<T> configurationClass;

    public GenerateJsonModelCommand(Application<T> application)
    {
        this(application, "generate-json-model", "Generate JSON Domain Model");
    }

    protected GenerateJsonModelCommand(Application<T> application, String name, String description)
    {
        super(application, name, description);
        this.configurationClass = application.getConfigurationClass();
    }

    @Override
    protected Class<T> getConfigurationClass()
    {
        return this.configurationClass;
    }

    @Override
    public void configure(Subparser subparser)
    {
        super.configure(subparser);

        subparser.addArgument("-n", "--applicationName")
                .dest("applicationName")
                .type(String.class)
                .required(true)
                .help("The application name");

        subparser.addArgument("-r", "--rootPackageName")
                .dest("rootPackageName")
                .type(String.class)
                .required(true)
                .help("The root package name");

        subparser.addArgument("-o", "--outputDirectory")
                .dest("outputDirectory")
                .type(String.class)
                .required(true)
                .help("The output directory");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception
    {
        DomainModelFactoryProvider domainModelFactoryProvider = safeCastConfiguration(
                DomainModelFactoryProvider.class,
                configuration);
        DataStoreFactoryProvider dataStoreFactoryProvider = safeCastConfiguration(
                DataStoreFactoryProvider.class,
                configuration);
        ClockFactoryProvider clockFactoryProvider = safeCastConfiguration(
                ClockFactoryProvider.class,
                configuration);

        LOGGER.info("Running {}.", this.getClass().getSimpleName());

        ObjectMapper objectMapper = environment.getObjectMapper();
        DomainModel  domainModel   = domainModelFactoryProvider.getDomainModelFactory().createDomainModel(objectMapper);
        DataStore    dataStore     = dataStoreFactoryProvider.getDataStoreFactory().createDataStore();
        Clock        clock         = clockFactoryProvider.getClockFactory().createClock();

        String applicationName = namespace.getString("applicationName");
        String rootPackageName = namespace.getString("rootPackageName");
        String outputDirectory = namespace.getString("outputDirectory");

        JsonMetaModelGenerator jsonMetaModelGenerator = new JsonMetaModelGenerator(
                domainModel,
                dataStore,
                objectMapper,
                clock,
                applicationName,
                rootPackageName,
                Paths.get(outputDirectory));

        jsonMetaModelGenerator.writeJsonMetaModelFiles();
    }

    // TODO: Extract this into common utility shared with PrioritizedBundle
    public static <C> C safeCastConfiguration(Class<C> aClass, Object configuration)
    {
        if (aClass.isInstance(configuration))
        {
            return aClass.cast(configuration);
        }

        String message = String.format(
                "Expected configuration to implement %s but found %s",
                aClass.getCanonicalName(),
                configuration.getClass().getCanonicalName());
        throw new IllegalStateException(message);
    }
}
