## Implementation Plan

### Name

We're going to immediately start creating repositories and modules, so the first step is to agree on a name.

Replace Klass with the agreed name in these docs.

### Private GitHub Repository

This project should be developed in the open from the start. So another early step is to create a private GitHub repository.

```bash
git init
```

Create the scaffolding for a multi-module maven build.

### Meta Model Interfaces

|              |                            |
|--------------|----------------------------|
| Module       | `klass-model-meta-interface` |
| Dependencies | None                       |

This module will contain almost entirely interfaces, including `DomainModel`, `Klass`, `Association`, `Projection`, and `Service`. There will be a few enumerations too, like `PrimitiveType`.

The api will be immutable. It will expose getters, but no setters. For example:

```java
public interface Klass
{
    ImmutableList<DataTypeProperty> getDataTypeProperties();

    ImmutableList<AssociationEnd> getAssociationEnds();

    default ImmutableList<DataTypeProperty> getKeyProperties()
    {
        return this.getDataTypeProperties().select(DataTypeProperty::isKey);
    }

    ImmutableList<ClassModifier> getClassModifiers();

    // ...
}
```

### Mutable Meta Model

|              |                            |
|--------------|----------------------------|
| Module       | `klass-model-meta-mutable`   |
| Dependencies | `klass-model-meta-interface` |

This module will contain mutable meta model pojos like `MutableDomainModel`, `MutableKlass`, `MutableAssociation`, etc. They will implement the corresponding interfaces from `klass-model-meta-interface`. They will also have public mutating methods for setting up the model.

```java
public class MutableKlass implements Klass
{
    private final MutableList<DataTypeProperty> dataTypeProperties = Lists.mutable.empty();

    @Override
    public ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        return this.dataTypeProperties.toImmutable();
    }

    public void addDataTypeProperty(MutableDataTypeProperty mutableDataTypeProperty)
    {
        this.dataTypeProperties.add(mutableDataTypeProperty);
        mutableDataTypeProperty.setOwningKlass(this);
    }

    // ...
}
```

All the public add*() methods will call public setters to set the reverse relationship. For example, `MutableKlass.addDataTypeProperty()` delegates to `MutableDataTypeProperty.setOwningKlass()`.

Eventually, we will create an immutable meta model. However, it is difficult to do so this early in the project. Additionally, the immutable interfaces will give us decent safety for now.

### JSON Meta Model POJOs

|              |                                             |
|--------------|---------------------------------------------|
| Module       | `klass-model-meta-json`                       |
| Dependencies | `klass-model-meta-interface` (for names only) |

This module will contain Jackson-serializable POJOs like `JsonDomainModel`, `JsonClass`, `JsonAssociation`, `JsonProjection`, and `JsonService`. When we use Jackson to serialize a `JsonDomainModel`, we'll get something like this:

```json
{
  "classes": [
    {
      "name": "Question",
      "packageName": "com.stackoverflow",
      "primitiveProperties": [
        {
          "name": "title",
          "primitiveType": "String"
        },
        {
          "name": "body",
          "primitiveType": "String"
        }
      ],
      // ...
    }
  ],
  "associations": [
    // ...
  ],
  // ...
}
```

The main difference from the mutable meta model is that the json meta model has no "reverse relationships". Parent nodes have getters for their children, but not the other way around. For example, `JsonClass.getPrimitiveProperties()` exists, but `JsonPrimitiveProperty.getOwningClass()` does not. If the reverse relationships existed, Jackson would get into an infinite loop during serialization.

Another difference is that the json meta model doesn't use inheritance. `JsonClass` has two separate lists of primitive properties and enumeration properties. `Klass` has a single combined list of the abstract type `DataTypeProperty`.

Where possible, the names in the json meta model should closely follow the manes in the meta model interfaces, without actually implementing the interfaces.

### JSON Compiler

|              |                                                 |
|--------------|-------------------------------------------------|
| Module       | `klass-compiler-json`                             |
| Dependencies | `klass-model-meta-json`, `klass-model-meta-mutable` |

This module will contain the JSON-to-Klass compiler. It's not merely a deserializer, it's actually a compiler. There are String references inside the json that need to be resolved, and referencing something that's undeclared is a compiler error.

```json
{
  "name": "QuestionHasAnswer",
  "packageName": "com.stackoverflow",
  "associationEnds": [
    {
      "name": "question",
      "direction": "source",
      "multiplicity": "1..1",
      "resultType": {
        "name": "Question",
        "packageName": "com.stackoverflow"
      }
    },
    {
      "name": "answers",
      "direction": "target",
      "multiplicity": "0..*",
      "resultType": {
        "name": "Answer",
        "packageName": "com.stackoverflow"
      }
    }
  ]
}
```

Let's walk through the compilation of this example; the association `QuestionHasAnswer`.

* Compile classes before compiling associations.
* Create a `MutableAssociation` to represent the `JsonAssociation`.
* Create the two `MutableAssociationEnds` and call `MutableAssociation.addAssociationEnd()` on each.
* Look up the two result types by name and fail with a helpful error message if either cannot be found.
* Call `MutableClass.addAssociationEnd()` on each result type, passing in the opposite association end.
* Call `MutableAssociationEnd.setResultType()` on each association end.

### Meta-model to OpenAPI converter

|              |                                   |
|--------------|-----------------------------------|
| Module       | `klass-generator-openapi`           |
| Dependencies | `klass-model-meta-mutable`, OpenAPI |

`klass-generator-openapi` will take as input a `DomainModel` and make a best effort conversion into an in-memory OpenAPI model. Services should convert fairly directly. Projections should convert into definitions. Classes and associations wouldn't convert.

### JSON to OpenAPI Converter

|              |                                                         |
|--------------|---------------------------------------------------------|
| Module       | `klass-json-to-openapi-cli`, `klass-json-to-openapi-plugin` |
| Dependencies | `klass-compiler-json`, `klass-generator-openapi`            |

`klass-json-to-openapi-cli` will string together the previous two converters.

`JsonDomainModel` -> `DomainModel` -> OpenAPI model

The CLI will perform file IO, starting and ending with files on disk.

`klass-json-to-openapi-plugin` will be essentially the same thing, packaged as a build plugin.

### OpenAPI to Meta-model Converter

|              |                                     |
|--------------|-------------------------------------|
| Module       | `klass-openapi-to-model`              |
| Dependencies | `klass-model-meta-interface`, OpenAPI |

`klass-openapi-to-model` goes in the opposite direction. It takes as input an in-memory OpenAPI model and makes a best effort conversion into a `DomainModel`. It first runs OpenAPI's own validator, so that it doesn't have to bother checking for so many compiler errors. Still, this module is arguably a compiler.

Models created by this converter will need a lot of hand editing afterwards. The usual work will be to add keys and foreign keys, find pairs of parameterized properties and replace them with associations, and fixing relationship criteria.

### Model to JSON Serializer

|                   |                                                   |
|-------------------|---------------------------------------------------|
| Module            | `klass-generator-json`                              |
| Dependencies      | `klass-model-meta-json`, `klass-model-meta-interface` |
| Test Dependencies | `klass-compiler-json` (We'd test the round-tripping of json -> meta model -> json) |

This module will take as input a `DomainModel` and convert it to `JsonDomainModel` for json serialization. At this point the meta model isn't bootstrapped, but we still want to be able to write it back out as json.

### OpenAPI to JSON Converter

|                   |                                                         |
|-------------------|---------------------------------------------------------|
| Module            | `klass-openapi-to-json-cli`, `klass-openapi-to-json-plugin` |
| Dependencies      | `klass-openapi-to-model`, `klass-generator-json`            |

`klass-openapi-to-json-cli` will string together the previous two converters.

OpenAPI model -> `DomainModel` -> `JsonDomainModel`

The CLI will perform file IO, starting and ending with files on disk.

`klass-openapi-to-json-plugin` will be essentially the same thing, packaged as a build plugin.

### Model to ORM definitions

|                   |                                                         |
|-------------------|---------------------------------------------------------|
| Module            | `klass-generator-hibernate`, `klass-generator-reladomo`     |
| Dependencies      | `klass-model-meta-interface`, ORM                         |

Decide whether to experiment with Hibernate or Reladomo first. Eventually we should prove out both.

These modules will input a `DomainModel` and output an ORM-specific definition.

For Hibernate, the definition can be either JPA-annotated Java or a Hibernate mapping xml.

For Reladomo, the output would be a Reladomo metamodel, or those objects serialized as xml files.

### ORM definitions to DDLs

|                   |                                                               |
|-------------------|---------------------------------------------------------------|
| Module            | `klass-generator-hibernate-ddls`, `klass-generator-reladomo-ddls` |
| Dependencies      | `klass-generator-hibernate`, `klass-generator-reladomo`           |

These modules integrate with the ORMs to convert the definitions into DDLs and apply them to a database.

In addition, we'll want functionality to quickly create an empty H2 database, either in memory or in a file, and apply the fresh schema to it.

We'll also want CLIs and/or build plugins.

### Model to Jackson POJOs

|                   |                                                      |
|-------------------|------------------------------------------------------|
| Module            | `klass-generator-jackson-pojos` or `klass-generator-dto` |
| Dependencies      | `klass-model-meta-mutable`                             |

This module reads `DomainModel` and code-generates annotated Java POJOs.

