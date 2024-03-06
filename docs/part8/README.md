## Converters

The Klass meta-model exists to address gaps in other meta-models. OpenAPI cannot represent a database schema, and a database schema isn't rich enough to define services.

Even though these models are not rich enough to represent an entire application, they are still full of useful information. If you're starting with one of these models, you can use a converter to code generate an Klass model. The resulting model will require manual fixes.

### OpenAPI converter

OpenAPI's service definitions can be converted directly into Klass services.

OpenAPI's definitions correspond with Klass projections, not classes. From the projections, we can infer classes and parameterized properties. OpenAPI cannot express bidirectional relationships, so we cannot infer associations.

After converting an OpenAPI model, you'll need to add keys and foreign keys. You'll want to find pairs of parameterized properties and replace them with associations. For the parameterized properties that you keep, you'll still need to fix their criteria.

### Other converters

Each converter has the same strengths and weaknesses as the underlying meta-model. When you convert a Hibernate model, you'll wind up with a decent model of classes and associations, but no services. The best conversion is from JHipster's JDL, because it supports the largest subset of Klass's features.
