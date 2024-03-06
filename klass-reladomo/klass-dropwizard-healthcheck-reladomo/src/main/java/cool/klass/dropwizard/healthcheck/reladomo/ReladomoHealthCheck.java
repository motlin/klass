package cool.klass.dropwizard.healthcheck.reladomo;

import com.codahale.metrics.health.HealthCheck;
import cool.klass.reladomo.simseq.ObjectSequenceFinder;
import cool.klass.reladomo.simseq.ObjectSequenceList;

public class ReladomoHealthCheck extends HealthCheck
{
    @Override
    protected Result check()
    {
        ObjectSequenceList objectSequences = ObjectSequenceFinder.findMany(ObjectSequenceFinder.all());
        int                size            = objectSequences.size();
        return Result.healthy("Found " + size + " rows.");
    }
}
