## Bootstrapped Meta-Model Data

Since the meta-model is part of the model, the bootstrap process populates meta-model types too. This means that we can GET `/api/meta/class/Class` for the Class class in any application.

```json
{
  "name": "Class",
  "inferred": false,
  "packageName": "klass.model.meta.domain",
  "ordinal": 1,
  "sourceCode": "class Class\n    transient\n{\n    name                          : String key;\n    inferred                      : Boolean;\n    packageName                   : String;\n    ordinal                       : Integer;\n    sourceCode                    : String;\n    primitiveProperties: PrimitiveProperty[0..*] owned\n        orderBy: this.ordinal;\n    enumerationProperties: EnumerationProperty[0..*]\n        orderBy: this.ordinal;\n    classModifiers: ClassModifier[0..*]\n        orderBy: this.ordinal;\n    associationEnds: AssociationEnd[0..*];\n    associationEndsResultTypeOf: AssociationEnd[0..*];\n    serviceGroup: ServiceGroup[0..1];\n}\n",
  "classModifiers": [
    {
      "name": "transient",
      "inferred": false,
      "ordinal": 1
    }
  ],
  "primitiveProperties": [
    {
      "name": "name",
      "inferred": false,
      "primitiveType": "String",
      "optional": false,
      "key": true,
      "id": false,
      "ordinal": 1,
      "primitivePropertyModifiers": [
        {
          "name": "key",
          "inferred": false,
          "ordinal": 1
        }
      ]
    },
    {
      "name": "inferred",
      "inferred": false,
      "primitiveType": "Boolean",
      "optional": false,
      "key": false,
      "id": false,
      "ordinal": 2,
      "primitivePropertyModifiers": []
    },
    {
      "name": "packageName",
      "inferred": false,
      "primitiveType": "String",
      "optional": false,
      "key": false,
      "id": false,
      "ordinal": 3,
      "primitivePropertyModifiers": []
    },
    {
      "name": "ordinal",
      "inferred": false,
      "primitiveType": "Integer",
      "optional": false,
      "key": false,
      "id": false,
      "ordinal": 4,
      "primitivePropertyModifiers": []
    },
    {
      "name": "sourceCode",
      "inferred": false,
      "primitiveType": "String",
      "optional": false,
      "key": false,
      "id": false,
      "ordinal": 5,
      "primitivePropertyModifiers": []
    }
  ],
  "enumerationProperties": [],
  "associationEnds": [
    {
      "name": "enumerationProperties",
      "inferred": false,
      "direction": "target",
      "multiplicity": "0..*",
      "resultType": {
        "name": "EnumerationProperty"
      },
      "owningAssociation": {
        "name": "ClassHasEnumerationProperties"
      },
      "associationEndModifiers": []
    },
    {
      "name": "classModifiers",
      "inferred": false,
      "direction": "target",
      "multiplicity": "0..*",
      "resultType": {
        "name": "ClassModifier"
      },
      "owningAssociation": {
        "name": "ClassHasModifiers"
      },
      "associationEndModifiers": []
    },
    {
      "name": "primitiveProperties",
      "inferred": false,
      "direction": "target",
      "multiplicity": "0..*",
      "resultType": {
        "name": "PrimitiveProperty"
      },
      "owningAssociation": {
        "name": "ClassHasPrimitiveTypeProperties"
      },
      "associationEndModifiers": [
        {
          "name": "owned",
          "inferred": false,
          "ordinal": 1
        }
      ]
    },
    {
      "name": "associationEnds",
      "inferred": false,
      "direction": "target",
      "multiplicity": "0..*",
      "resultType": {
        "name": "AssociationEnd"
      },
      "owningAssociation": {
        "name": "ClassHasAssociationEnds"
      },
      "associationEndModifiers": []
    },
    {
      "name": "associationEndsResultTypeOf",
      "inferred": false,
      "direction": "source",
      "multiplicity": "0..*",
      "resultType": {
        "name": "AssociationEnd"
      },
      "owningAssociation": {
        "name": "AssociationEndHasResultType"
      },
      "associationEndModifiers": []
    },
    {
      "name": "serviceGroup",
      "inferred": false,
      "direction": "source",
      "multiplicity": "0..1",
      "resultType": {
        "name": "ServiceGroup"
      },
      "owningAssociation": {
        "name": "ServiceGroupHasClass"
      },
      "associationEndModifiers": []
    }
  ]
}
```
