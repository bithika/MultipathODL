package org.opendaylight.controller.config.yang.config.distributed_datastore_provider;

import org.opendaylight.controller.cluster.datastore.DistributedDataStoreFactory;
import org.opendaylight.controller.md.sal.dom.store.impl.InMemoryDOMDataStoreConfigProperties;

public class DistributedConfigDataStoreProviderModule extends
    org.opendaylight.controller.config.yang.config.distributed_datastore_provider.AbstractDistributedConfigDataStoreProviderModule {
    public DistributedConfigDataStoreProviderModule(
        org.opendaylight.controller.config.api.ModuleIdentifier identifier,
        org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public DistributedConfigDataStoreProviderModule(
        org.opendaylight.controller.config.api.ModuleIdentifier identifier,
        org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
        org.opendaylight.controller.config.yang.config.distributed_datastore_provider.DistributedConfigDataStoreProviderModule oldModule,
        java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        return DistributedDataStoreFactory.createInstance("config", getConfigSchemaServiceDependency(),
                InMemoryDOMDataStoreConfigProperties.create(getConfigMaxShardDataChangeExecutorPoolSize(),
                        getConfigMaxShardDataChangeExecutorQueueSize(),
                        getConfigMaxShardDataChangeListenerQueueSize()));
    }

}
