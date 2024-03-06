package cool.klass.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonPrettyPrinter extends DefaultPrettyPrinter
{
    public JsonPrettyPrinter()
    {
        this._arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
    }

    @Override
    public DefaultPrettyPrinter createInstance()
    {
        return this;
    }

    @Override
    public void writeObjectFieldValueSeparator(JsonGenerator jsonGenerator) throws IOException
    {
        jsonGenerator.writeRaw(this._separators.getObjectFieldValueSeparator() + " ");
    }

    @Override
    public void writeStartObject(JsonGenerator jsonGenerator) throws IOException
    {
        super.writeStartObject(jsonGenerator);
    }

    @Override
    public void writeEndObject(JsonGenerator jsonGenerator, int nrOfEntries) throws IOException
    {
        super.writeEndObject(jsonGenerator, nrOfEntries);
        if (this._nesting == 0)
        {
            jsonGenerator.writeRaw(DefaultIndenter.SYS_LF);
        }
    }
}
