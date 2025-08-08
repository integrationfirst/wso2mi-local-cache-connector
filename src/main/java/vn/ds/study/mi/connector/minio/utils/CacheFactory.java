package vn.ds.study.mi.connector.minio.utils;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public final class CacheFactory {

    static EmbeddedCacheManager cacheManager = new DefaultCacheManager(new GlobalConfigurationBuilder()
                                                                               .nonClusteredDefault() // Disables clustering
                                                                               .build());

    public static Cache getCache(String name) {

        Cache<String, String> cache = cacheManager.getCache(name);
        if (cache == null) {
            cacheManager.defineConfiguration(name, new ConfigurationBuilder()
                    .persistence()
                    .passivation(false)
                    .addStore(org.infinispan.persistence.rocksdb.configuration.RocksDBStoreConfigurationBuilder.class)
                    .location("rocksdb-data/" + name)
                    .expiredLocation("rocksdb-expired/" + name)
                    .build());
            cache = cacheManager.getCache(name);
        }

        return cache;
    }
}
