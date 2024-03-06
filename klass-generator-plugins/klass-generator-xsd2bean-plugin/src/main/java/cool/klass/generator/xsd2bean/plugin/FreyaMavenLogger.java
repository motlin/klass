package cool.klass.generator.xsd2bean.plugin;

import com.gs.fw.common.freyaxml.generator.Logger;
import org.apache.maven.plugin.logging.Log;

public class FreyaMavenLogger implements Logger
{
    private final Log log;

    public FreyaMavenLogger(Log log)
    {
        this.log = log;
    }

    @Override
    public void debug(String content)
    {
        this.log.debug(content);
    }

    @Override
    public void debug(String content, Throwable error)
    {
        this.log.debug(content, error);
    }

    @Override
    public void info(String content)
    {
        this.log.info(content);
    }

    @Override
    public void info(String content, Throwable error)
    {
        this.log.info(content, error);
    }

    @Override
    public void warn(String content)
    {
        this.log.warn(content);
    }

    @Override
    public void warn(String content, Throwable error)
    {
        this.log.warn(content, error);
    }

    @Override
    public void error(String content)
    {
        this.log.error(content);
    }

    @Override
    public void error(String content, Throwable error)
    {
        this.log.error(content, error);
    }
}
