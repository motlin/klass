package cool.klass.dropwizard.configuration.domain.model.loader.compiler;

import java.io.File;
import java.net.URL;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class DomainModelCompilerFactoryTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator    validator    = Validators.newValidator();

    private final YamlConfigurationFactory<DomainModelFactory> factory =
            new YamlConfigurationFactory<>(DomainModelFactory.class, this.validator, this.objectMapper, "dw");

    @Test
    public void isDiscoverable()
    {
        // Make sure the types we specified in META-INF gets picked up
        DiscoverableSubtypeResolver discoverableSubtypeResolver = new DiscoverableSubtypeResolver();
        ImmutableList<Class<?>>     discoveredSubtypes          = discoverableSubtypeResolver.getDiscoveredSubtypes();
        assertThat(discoveredSubtypes, hasItem(DomainModelCompilerFactory.class));
    }

    @Test
    public void domainModelCompiler() throws Exception
    {
        URL                resource           = Resources.getResource("test-config.yml");
        File               yml                = new File(resource.toURI());
        DomainModelFactory domainModelFactory = this.factory.build(yml);
        assertThat(domainModelFactory, instanceOf(DomainModelCompilerFactory.class));
    }
}
