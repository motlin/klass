package cool.klass.dropwizard.bundle.h2;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletRegistration.Dynamic;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.h2.H2Factory;
import cool.klass.dropwizard.configuration.h2.H2FactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inspired by {@link io.github.jhipster.config.h2.H2ConfigurationHelper}
 */
@AutoService(PrioritizedBundle.class)
public class H2Bundle implements PrioritizedBundle<H2FactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Bundle.class);

    @Override
    public int getPriority()
    {
        return -4;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(H2FactoryProvider configuration, Environment environment)
    {
        H2Factory h2Factory = configuration.getH2Factory();
        if (!h2Factory.isEnabled())
        {
            LOGGER.info("{} disabled.", H2Bundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", H2Bundle.class.getSimpleName());

        Server tcpServer = this.createTcpServer(h2Factory.getTcpServerArgs());
        environment.lifecycle().manage(new TcpServerShutdownHook(tcpServer));

        Dynamic h2ConsoleServlet = environment.servlets().addServlet(h2Factory.getServletName(), new WebServlet());
        h2ConsoleServlet.addMapping(h2Factory.getServletUrlMapping());
        h2ConsoleServlet.setInitParameter("-properties", h2Factory.getPropertiesLocation());
        h2ConsoleServlet.setLoadOnStartup(1);

        LOGGER.info("Completing {}.", H2Bundle.class.getSimpleName());
    }

    @Nonnull
    private Server createTcpServer(List<String> tcpServerArgs)
    {
        try
        {
            Server server = Server.createTcpServer(tcpServerArgs.toArray(new String[]{}));
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
