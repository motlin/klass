## Summary Implementation Plan

### Milestone 1: Preparation
* Agree on a **name**. Replace Klass with that name.
* Create a **GitHub private repository**. Prepare for open-source from the start.
* Create the infrastructure for a **multi-module build** using maven or gradle.

### Milestone 2: Model and Schema
* **Meta model interfaces** in Java. Interfaces like `DomainModel`, `Klass` and `Association`.
* **Meta model implementation**. Mutable classes like `MutableDomainModel`, `MutableKlass` and `MutableAssociation` implementing the interfaces.
* **Meta model Jackson POJOs** like `JsonDomainModel`, `JsonClass`, and `JsonAssociation`.
* **JSON model compiler**. Input `JsonDomainModel`, resolve all String references, output `MutableDomainModel`.
* **Model to ORM definitions**. Input `DomainModel`, output Hibernate and/or Reladomo definitions.
* **ORM definitions to schema**. Input ORM definitions, output sql ddls.

**Demo**: Generate the data layer. Generate a Dropwizard application and services separately. Copy in the data layer code. Write glue code manually, to query the database and build service responses.

### Milestone 3: Services

* **Model to Jersey services**. Input `DomainModel`, output Jersey annotated Java resource classes.
* **Model to Jackson DTOs**. Input `DomainModel`, output Java POJOs with Jackson annotations.

**Demo**: Similar to previous milestone, but now we don't have to write our services with a separate technology. We still have to write code to glue together data and services.

### Milestone 3.5: OpenAPI Services

If we haven't already scaled up to 2 developers, now would be a good point.

* **Model to OpenAPI converter**. Input `DomainModel`, output openapi.yml. Best-effort conversion.
* **JSON Model to OpenAPI converter**. Input `JsonDomainModel`, output openapi.yml. Best-effort conversion.

This is an alternative approach to generating services. Instead of directly generating service code, we'd generate an OpenAPI definition and delegate to OpenAPI's code generation.

Pros:
* Some chance of faster time to market.
* Likely faster implementation for multiple server-side languages (python services).
* Part of the integration story with OpenAPI. Code generation of clients.

Cons:
* Team experienced bugs and incompatibilities in swagger-codegen, at least in the SmartBear fork.
* A later milestone will require writing our own code generator anyway.

**Demo**: Same as milestone 3.

### Milestone 4: Fully generated read services

* **Data Store API**. An interface called `DataStore` with methods like `runInTransaction()` and `getDataTypeProperty()`.
    * It may be too expensive to support more than one ORM, or non-relational stores.
    * We should create the abstraction anyway to leave open the possibility and encourage loose coupling.
* **Data to json serializer**. Takes as inputs a `DataStore`, an instance to serialize, and a `Projection`. Serializes it to an output stream.
    * Does not depend on a particular ORM. The ORM-specific stuff is hidden behind `DataStore`.
* **Model to Jersey services enhancements**. Enhance the code generator to glue together the new parts.
    * Code generate ORM queries.
    * Perform validations and throw errors.
    * Pass the fetched data and relevant projection to the json serializer.

### Milestone 5: Fully generated write services

Writes are more complex than reads. The implementation touches same modules as the previous milestone, but deserves its own milestone.

* **Data Store API enhancements**. Add methods to instantiate, update properties, and terminate.
* **Json to data deserializer**. Takes as input a `Klass` and the incoming json body. Validates the json and deserializes it to primitives (maps, lists, Strings, and language primitives). Throws helpful error messages.
    * Validate expected fields. (No such property `Question.description`. Expected `title` or `body`.)
    * Validate types. (`Question.title` is a `String` but got `Number`.)
    * Validate required properties. (`Question.title` is required but value was missing/`null`.)
    * Additional validations like minimum length, valid enumeration choice, immutable properties, etc.
* **Primitive data to ORM synchronizer**.
    * Implement Create and Update writers, for types with a synthetic key. Create should return the auto-generated integer.
    * Implement Upsert writer, for types with a natural key. Figure out if the incoming key already exists and create or update as appropriate.
    * Implement synchronization of primitive data to ORM data, calling the relevant setters.
    * Recurse on owned association ends.
    * For embedded arrays, recursively create, update, delete, or ignore as appropriate. Say we're updating a Book with an embedded set of owned Chapters. Each chapter could match what's in the database (ignore), be completely new (insert), or have some edits (update). Chapters that are in the database but absent from the incoming set should be terminated.
    * Translate embedded keys to foreign keys. Foreign keys like `Answer.questionId` are private to the data layer and never appear in serialized data. Say we have a service to create new Answers. When receiving external data, the service has to translate `Answer.question.id` into a write to `Answer.questionId`. Depending on the style of service, the id may be in different locations (in a url or inside a Protobuf message).

### More

* **OpenAPI to model converter**. Input openapi.yml, output `DomainModel`. Best-effort conversion.
* **gRPC support**. Similar to existing modules but using Protobuf and gRPC rather than Jackson and Jersey.
    * **Model to Protobuf definitions**.
    * **Data to Protobuf serializer**.
    * **Protobuf to data deserializer**.
    * **Model to gRPC services**.
* **Application scaffolding**. Wire up dropwizard application and configuration and equivalent for gRPC.
* **Dynamic user interface**. A simple form that allows editing a single instance of a class at a time.
