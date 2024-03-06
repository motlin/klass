drop table if exists CLASS_WITH_DERIVED_PROPERTY;

create table CLASS_WITH_DERIVED_PROPERTY
(
    key varchar(255) not null,
    class_owning_class_with_derived_property_key varchar(255) not null
);

