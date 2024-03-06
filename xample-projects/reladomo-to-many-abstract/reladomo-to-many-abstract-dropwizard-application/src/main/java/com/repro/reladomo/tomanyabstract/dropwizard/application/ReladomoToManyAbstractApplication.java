package com.repro.reladomo.tomanyabstract.dropwizard.application;

import javax.annotation.Nonnull;

import com.repro.reladomo.tomanyabstract.ChapterWithQuote;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ReladomoToManyAbstractApplication extends AbstractReladomoToManyAbstractApplication
{
    public static void main(String[] args) throws Exception
    {
        new ReladomoToManyAbstractApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<ReladomoToManyAbstractConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
    }

    @Override
    public void run(
            ReladomoToManyAbstractConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);

        ChapterWithQuote chapterWithQuote = new ChapterWithQuote();
        chapterWithQuote.setText("chapter text");
    }
}
