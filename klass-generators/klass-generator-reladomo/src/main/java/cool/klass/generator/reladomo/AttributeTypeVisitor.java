package cool.klass.generator.reladomo;

import com.gs.fw.common.mithra.generator.metamodel.AttributeType;
import com.gs.fw.common.mithra.generator.metamodel.PrimaryKeyGeneratorStrategyType;
import com.gs.fw.common.mithra.generator.metamodel.SimulatedSequenceType;
import cool.klass.model.meta.domain.PrimitiveProperty;
import cool.klass.model.meta.domain.PrimitiveTypeVisitor;
import cool.klass.reladomo.simulatedsequence.ObjectSequenceObjectFactory;

public class AttributeTypeVisitor implements PrimitiveTypeVisitor
{
    private final AttributeType     attributeType;
    private final PrimitiveProperty primitiveProperty;

    public AttributeTypeVisitor(AttributeType attributeType, PrimitiveProperty primitiveProperty)
    {
        this.attributeType = attributeType;
        this.primitiveProperty = primitiveProperty;
    }

    @Override
    public void visitID()
    {
        this.attributeType.setJavaType("long");
        // TODO: Infer during compilation that ID properties are key properties, or add an error when they are not.
        PrimaryKeyGeneratorStrategyType primaryKeyGeneratorStrategyType = new PrimaryKeyGeneratorStrategyType();
        primaryKeyGeneratorStrategyType.with("SimulatedSequence", this.attributeType);
        this.attributeType.setPrimaryKeyGeneratorStrategy(primaryKeyGeneratorStrategyType);
        SimulatedSequenceType simulatedSequence = new SimulatedSequenceType();
        simulatedSequence.setSequenceName(this.primitiveProperty.getOwningKlass().getName());
        simulatedSequence.setSequenceObjectFactoryName(
                ObjectSequenceObjectFactory.class.getCanonicalName());
        simulatedSequence.setHasSourceAttribute(false);
        simulatedSequence.setBatchSize(10);
        simulatedSequence.setInitialValue(1);
        simulatedSequence.setIncrementSize(1);
        this.attributeType.setSimulatedSequence(simulatedSequence);
    }

    @Override
    public void visitString()
    {
        this.attributeType.setJavaType("String");
        this.attributeType.setTrim(false);
    }

    @Override
    public void visitInteger()
    {
        this.attributeType.setJavaType("int");
    }

    @Override
    public void visitLong()
    {
        this.attributeType.setJavaType("long");
    }

    @Override
    public void visitDouble()
    {
        this.attributeType.setJavaType("double");
    }

    @Override
    public void visitFloat()
    {
        this.attributeType.setJavaType("float");
    }

    @Override
    public void visitBoolean()
    {
        this.attributeType.setJavaType("boolean");
    }

    @Override
    public void visitInstant()
    {
        this.attributeType.setJavaType("Timestamp");
    }

    @Override
    public void visitLocalDate()
    {
        this.attributeType.setJavaType("Date");
    }
}
