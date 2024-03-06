package cool.klass.dropwizard.bundle.h2;

import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.servlet.ServletRegistration.Dynamic;

import com.google.auto.service.AutoService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inspired by {@link io.github.jhipster.config.h2.H2ConfigurationHelper}
 */
@AutoService(Bundle.class)
public class H2Bundle implements Bundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Bundle.class);

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull Environment environment)
    {
        Config  config         = ConfigFactory.load();
        Config  h2BundleConfig = config.getConfig("bundle.h2");
        boolean runEmbedded    = h2BundleConfig.getBoolean("runEmbedded");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = h2BundleConfig.root().render(configRenderOptions);
            LOGGER.debug("H2 Bundle configuration:\n{}", render);
        }

        if (runEmbedded)
        {
            Server tcpServer = this.createTcpServer();
            environment.lifecycle().manage(new TcpServerShutdownHook(tcpServer));

            Dynamic h2ConsoleServlet = environment.servlets().addServlet("H2Console", new WebServlet());
            h2ConsoleServlet.addMapping("/h2-console/*");
            h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
            h2ConsoleServlet.setLoadOnStartup(1);
        }
    }

    @Nonnull
    private Server createTcpServer()
    {
        try
        {
            Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-baseDir", "./target/h2db");
            server.start();
            LOGGER.debug(server.getStatus());
            return server;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
