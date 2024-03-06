package cool.klass.dropwizard.configuration.auth.filter;

import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.auto.service.AutoService;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = Id.NAME, property = "type")
@AutoService(Discoverable.class)
public interface AuthFilterFactory extends Discoverable
{
    AuthFilter<?, ? extends Principal> createAuthFilter();
}
