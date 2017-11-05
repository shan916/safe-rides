package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
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
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank">Query Methods</a>
 */
public interface RideRequestRepository extends CrudRepository<RideRequest, Long> {

    // Returns a list of RideRequests with the specified status

    /**
     * Finds all ride requests with the specified status
     *
     * @param rideRequestStatus the status to filter by
     * @return list of ride requests filtered by the specified status
     */
	@Cacheable("requests")
    List<RideRequest> findByStatus(RideRequestStatus rideRequestStatus);


    /**
     * Finds the latest ride request by the one card id
     *
     * @param oneCardId the one card id of the ride request
     * @return the ride request
     */
	@Cacheable("requests")
    RideRequest findTop1ByOneCardIdOrderByRequestDateDesc(String oneCardId);

    /**
     * Finds the latest ride requested that is associated with the specified user
     * Returns null if no ride is associated with the specified user
     *
     * @param user the user to search by
     * @return latest ride requested that is associated with the specified user
     */
	@Cacheable("requests")
    RideRequest findTop1ByUserOrderByRequestDateDesc(User user);

    /**
     * Deletes all ride requests with the request date that is before the latestDate
     *
     * @param latestDate the date which prior ride request records should be deleted (exclusive)
     */
    void deleteByRequestDateBefore(LocalDateTime latestDate);
}