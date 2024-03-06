# Bootstrapped Meta-Model

Klass has a meta-model; a model of models. In fact, it has several representations of the same meta-model.

* The Abstract Syntax Tree used internally by the compiler.
* The in-memory DomainModel object returned upon successful compilation.
* The bootstrapped meta-model.
* etc.

The bootstrapped meta-model is expressed like a regular model. The first few meta-types are defined like this:

```klass
package klass.model.meta.domain

class Class
    transient
{
    name       : String key;
    inferred   : Boolean;
    packageName: String private;
    ordinal    : Integer;
    sourceCode : String;
}

enumeration PrimitiveType
{
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    FLOAT("Float"),
    BOOLEAN("Boolean"),
    STRING("String"),
    INSTANT("Instant"),
    LOCAL_DATE("LocalDate"),
    TEMPORAL_INSTANT("TemporalInstant"),
    TEMPORAL_RANGE("TemporalRange"),
}

enumeration Multiplicity
{
    ZERO_TO_ONE("0..1"),
    ONE_TO_ONE("1..1"),
    ZERO_TO_MANY("0..*"),
    ONE_TO_MANY("1..*"),
}

class PrimitiveProperty
    transient
{
    className                     : String key;
    name                          : String key;
    inferred                      : Boolean;
    primitiveType                 : PrimitiveType;
    optional                      : Boolean;
    key                           : Boolean;
    id                            : Boolean;
    ordinal                       : Integer;
}

association ClassHasPrimitiveTypeProperties
{
    owningClass: Class[1..1];
    primitiveProperties: PrimitiveProperty[0..*] owned

    relationship this.name == PrimitiveProperty.className
}

// ...
```

The full source is too large to include here. It defines enumeration, class, association, projection, and service, plus their parts.

The bootstrapped meta-model allows us to work with metadata like regular data. It defines some projections and services which return model data. For example, here's the service to get a class by its name:

```klass
service ClassResource
{
    getByName(className: String[1..1]): ClassReadProjection[0..1]
    {
        operation: read;
        url      : /api/meta/class/{className: String[1..1]};
        criteria : this.name == className;
        format   : json;
    }
}
```
