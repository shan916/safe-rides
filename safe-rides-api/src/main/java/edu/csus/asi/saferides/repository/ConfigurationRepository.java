package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Configuration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * Datastore interface for application configurations. Only stores configuration values that can be set by a privileged user
 */
public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {

    @Cacheable(value = "Configuration", key = "#p0")
    Configuration findOne(int id);

    @Override
    @CacheEvict(value = "Configuration", allEntries = true)
    <S extends Configuration> S save(S entity);
}
