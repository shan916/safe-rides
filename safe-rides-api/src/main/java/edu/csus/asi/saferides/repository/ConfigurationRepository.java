package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Configuration;
import org.springframework.data.repository.CrudRepository;

/**
 * Datastore interface for application configurations. Only stores configuration values that can be set by a privileged user
 */
public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {

    Configuration findOne(int id);
}
