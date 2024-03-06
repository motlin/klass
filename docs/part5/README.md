# Minimal Viable Product

The MVP can leave out of scope many of the features in the long-term vision. It should support the creation of a relational schema and basic crud services, but not much more.

### JSON instead of DSL

Instead of a DSL with a compiler, the initial model can be hand-written in json. Ideally, the json should be a strict subset of the planned bootstrapped meta-model, for an easy transition.

### Keys

The MVP should include either auto-incrementing ids as keys, or regular natural keys, but not both. If the MVP implements natural keys, it probably does not need to implement composite keys.

### Operations

The MVP could start with read, followed by replace, upsert, and create. The operations delete and patch (update) could be postponed indefinitely.

### Audit Data

The MVP could start without any audit features (`systemTemporal`, `versioned`, `audited`). It could start without other modifiers, like `final` and `owned`. It wouldn't need any macros or inference at first.

### Bootstrapped meta-model

The meta-model needs to be robust even in the MVP, but it need not be queryable over services in the MVP. Similarly, the dynamic UIs built on the bootstrapped meta-model could come later.
