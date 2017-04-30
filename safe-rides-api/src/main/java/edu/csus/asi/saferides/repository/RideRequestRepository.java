package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Provides methods for working with the RideRequest persistence
 * <p>
 * By extending CrudRepository, RideRequestRepository inherits several methods for working with RideRequest persistence,
 * including methods for saving, deleting, and finding RideRequest entities
 *
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * <p>
 * Other query methods can be defined by simply declaring the method signature.
 * Spring Data JPA will automatically create an implementation on the fly.
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank>Query Methods</a>
 */
public interface RideRequestRepository extends CrudRepository<RideRequest, Long> {

    // Returns a list of RideRequests with the specified status

    /**
     * Finds all ride requests with the specified status
     *
     * @param rideRequestStatus the status to filter by
     * @return list of ride requests filtered by the specified status
     */
    List<RideRequest> findByStatus(RideRequestStatus rideRequestStatus);

    /**
     * Finds the latest ride requested that is associated with the specified OneCard id
     * Returns null if no ride is associated with the specified OneCard id
     *
     * @param oneCardId the OneCard Id to search by
     * @return latest ride requested that is associated with the specified OneCard id
     */
    RideRequest findTop1ByOneCardIdOrderByRequestDateDesc(String oneCardId);
}