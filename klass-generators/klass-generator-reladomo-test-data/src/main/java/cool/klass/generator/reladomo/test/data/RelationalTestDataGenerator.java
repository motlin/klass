package cool.klass.generator.reladomo.test.data;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.util.dbextractor.MithraTestDataRowFormatter;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;

public class RelationalTestDataGenerator
{
    private static final Function<Object, String> FORMATTER = new MithraTestDataRowFormatter()::valueOf;
    private final        DomainModel              domainModel;

    public RelationalTestDataGenerator(DomainModel domainModel)
    {
        this.domainModel = domainModel;
    }

    public String generate()
    {
        return this.domainModel
                .getKlasses()
                .asLazy()
                .collect(this::convertClass)
                .makeString("");
    }

    @Nonnull
    private String convertClass(Klass klass)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".convertClass() not implemented yet");
    }
}
