package cool.klass.dropwizard.configuration.domain.model.loader.constant;

import java.io.File;
import java.net.URL;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import io.dropwizard.configuration.JsonConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.serialization.jackson.config.ObjectMapperConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class DomainModelConstantFactoryTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    private final ObjectMapper objectMapper = getObjectMapper();
    private final Validator    validator    = Validators.newValidator();

    private final JsonConfigurationFactory<DomainModelFactory> factory =
            new JsonConfigurationFactory<>(DomainModelFactory.class, this.validator, this.objectMapper, "dw");

    @Test
    public void isDiscoverable()
    {
        // Make sure the types we specified in META-INF gets picked up
        DiscoverableSubtypeResolver discoverableSubtypeResolver = new DiscoverableSubtypeResolver();
        ImmutableList<Class<?>>     discoveredSubtypes          = discoverableSubtypeResolver.getDiscoveredSubtypes();
        assertThat(discoveredSubtypes, hasItem(DomainModelConstantFactory.class));
    }

    @Test
    public void domainModelConstant() throws Exception
    {
        URL                resource           = Resources.getResource("config-test.json5");
        File               json               = new File(resource.toURI());
        DomainModelFactory domainModelFactory = this.factory.build(json);
        assertThat(domainModelFactory, instanceOf(DomainModelConstantFactory.class));
    }

    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        ObjectMapperConfig.configure(objectMapper);
        return objectMapper;
    }
}
