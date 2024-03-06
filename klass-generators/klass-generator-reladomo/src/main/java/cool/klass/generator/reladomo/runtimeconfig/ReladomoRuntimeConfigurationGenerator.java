package cool.klass.generator.reladomo.runtimeconfig;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.mithraruntime.CacheType;
import com.gs.fw.common.mithra.mithraruntime.ConnectionManagerType;
import com.gs.fw.common.mithra.mithraruntime.MithraObjectConfigurationType;
import com.gs.fw.common.mithra.mithraruntime.MithraPureObjectConfigurationType;
import com.gs.fw.common.mithra.mithraruntime.MithraRuntime;
import com.gs.fw.common.mithra.mithraruntime.MithraRuntimeMarshaller;
import com.gs.fw.common.mithra.mithraruntime.PropertyType;
import com.gs.fw.common.mithra.mithraruntime.PureObjectsType;
import cool.klass.generator.reladomo.AbstractReladomoGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class ReladomoRuntimeConfigurationGenerator extends AbstractReladomoGenerator
{
    // "com.liftwizard.reladomo.connectionmanager.h2.H2ConnectionManager"
    // "com.gs.fw.common.mithra.test.ConnectionManagerForTests"
    @Nonnull
    private final String    connectionManagerFullyQualifiedName;
    private final boolean   isTest;
    @Nonnull
    private final String    rootPackageName;
    @Nonnull
    private final CacheType cacheType;

    public ReladomoRuntimeConfigurationGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String connectionManagerFullyQualifiedName,
            boolean isTest,
            @Nonnull String rootPackageName,
            @Nonnull String cacheType)
    {
        super(domainModel);
        this.connectionManagerFullyQualifiedName = Objects.requireNonNull(connectionManagerFullyQualifiedName);
        this.isTest                              = isTest;
        this.rootPackageName                     = Objects.requireNonNull(rootPackageName);
        this.cacheType                           = ReladomoRuntimeConfigurationGenerator.getCacheType(cacheType);
    }

    private static CacheType getCacheType(@Nonnull String cacheType)
    {
        switch (cacheType)
        {
            case "partial":
                return CacheType.PARTIAL;
            case "full":
                return CacheType.FULL;
            case "none":
                return CacheType.NONE;
            default:
                String message = String.format(
                        "Invalid cacheType. Expected one of [partial, full, none] but got: %s",
                        cacheType);
                throw new RuntimeException(message);
        }
    }

    public void writeRuntimeConfigFile(@Nonnull Path path) throws IOException
    {
        MithraRuntime mithraRuntime = this.getMithraRuntime();

        MithraRuntimeMarshaller mithraRuntimeMarshaller = new MithraRuntimeMarshaller();
        mithraRuntimeMarshaller.setIndent(true);

        StringBuilder stringBuilder = new StringBuilder();
        mithraRuntimeMarshaller.marshall(stringBuilder, mithraRuntime);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        this.printStringToFile(path, xmlString);
    }

    @Nonnull
    private MithraRuntime getMithraRuntime()
    {
        MithraRuntime         mithraRuntime     = new MithraRuntime();
        ConnectionManagerType connectionManager = this.getConnectionManager();
        mithraRuntime.setConnectionManagers(Lists.mutable.with(connectionManager));
        mithraRuntime.setPureObjects(this.getPureObjectsType());
        return mithraRuntime;
    }

    private ImmutableList<PropertyType> getPropertyTypes()
    {
        return this.isTest
                ? Lists.immutable.with(ReladomoRuntimeConfigurationGenerator.createPropertyType(
                "resourceName",
                "testdb"))
                : Lists.immutable.empty();
    }

    @Nonnull
    private PureObjectsType getPureObjectsType()
    {
        PureObjectsType pureObjectsType = new PureObjectsType();
        pureObjectsType.setNotificationIdentifier(this.rootPackageName);
        pureObjectsType.setMithraObjectConfigurations(this.getMithraPureObjectConfigurationTypes().castToList());
        return pureObjectsType;
    }

    @Nonnull
    private static PropertyType createPropertyType(String name, String value)
    {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(name);
        propertyType.setValue(value);
        return propertyType;
    }

    @Nonnull
    private ConnectionManagerType getConnectionManager()
    {
        ImmutableList<PropertyType> propertyTypes = this.getPropertyTypes();
        List<PropertyType>          properties    = propertyTypes.castToList();

        ConnectionManagerType connectionManagerType = new ConnectionManagerType();
        connectionManagerType.setClassName(this.connectionManagerFullyQualifiedName);
        connectionManagerType.setProperties(properties);
        connectionManagerType.setMithraObjectConfigurations(this.getConnectionManagerObjectConfigurationTypes().castToList());
        return connectionManagerType;
    }

    private ImmutableList<MithraPureObjectConfigurationType> getMithraPureObjectConfigurationTypes()
    {
        return this.domainModel
                .getClasses()
                .select(Klass::isTransient)
                .collect(PackageableElement::getFullyQualifiedName)
                .collect(ReladomoRuntimeConfigurationGenerator::createMithraPureObjectConfigurationType);
    }

    private ImmutableList<MithraObjectConfigurationType> getConnectionManagerObjectConfigurationTypes()
    {
        ImmutableList<MithraObjectConfigurationType> objectConfigurationTypes              = this.getObjectConfigurationTypes();
        MithraObjectConfigurationType                objectSequenceObjectConfigurationType = ReladomoRuntimeConfigurationGenerator.createObjectSequenceObjectConfigurationType();

        return Lists.immutable.with(objectSequenceObjectConfigurationType)
                .newWithAll(objectConfigurationTypes);
    }

    private ImmutableList<MithraObjectConfigurationType> getObjectConfigurationTypes()
    {
        return this.domainModel
                .getClasses()
                // TODO: Can a class be transient and abstract? Is that redundant?
                .reject(Klass::isTransient)
                .reject(each -> each.getInheritanceType() == InheritanceType.TABLE_PER_SUBCLASS)
                .collect(PackageableElement::getFullyQualifiedName)
                .collectWith(
                        ReladomoRuntimeConfigurationGenerator::createMithraObjectConfigurationType,
                        this.cacheType);
    }

    @Nonnull
    private static MithraObjectConfigurationType createObjectSequenceObjectConfigurationType()
    {
        return ReladomoRuntimeConfigurationGenerator.createMithraObjectConfigurationType(
                "com.liftwizard.reladomo.simseq.ObjectSequence",
                CacheType.NONE);
    }

    @Nonnull
    private static MithraObjectConfigurationType createMithraObjectConfigurationType(
            String fullyQualifiedClassName,
            CacheType cacheType)
    {
        MithraObjectConfigurationType mithraObjectConfigurationType = new MithraObjectConfigurationType();
        mithraObjectConfigurationType.setCacheType(cacheType);
        mithraObjectConfigurationType.setClassName(fullyQualifiedClassName);
        return mithraObjectConfigurationType;
    }

    @Nonnull
    private static MithraPureObjectConfigurationType createMithraPureObjectConfigurationType(String fullyQualifiedClassName)
    {
        MithraPureObjectConfigurationType mithraPureObjectConfigurationType = new MithraPureObjectConfigurationType();
        mithraPureObjectConfigurationType.setClassName(fullyQualifiedClassName);
        return mithraPureObjectConfigurationType;
    }
}
