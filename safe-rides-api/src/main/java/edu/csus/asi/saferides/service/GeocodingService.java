package edu.csus.asi.saferides.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.utility.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * Service for implementing Google's Geocoding API
 */
@Service
public class GeocodingService {
    /**
     * Geocoding API key from application.yaml
     */
    @Value("${api-keys.geocoding}")
    private String geocodingApiKey;

    private static final Logger log = LoggerFactory.getLogger(GeocodingService.class);

    /**
     * Set the coordinate fields of the RideRequest object for both the pickup and dropoff location.
     *
     * @param rideRequest to set the coordinate fields in
     */
    public void setCoordinates(RideRequest rideRequest) {
        GeoApiContext ctx = new GeoApiContext();
        ctx.setApiKey(geocodingApiKey);
        try {
            try {
                try {
                    GeocodingResult[] results = GeocodingApi.newRequest(ctx)
                            .address(Util.formatAddress(rideRequest.getPickupLine1(), rideRequest.getPickupLine2(),
                                    rideRequest.getPickupCity())).await();
                    if (results.length > 0) {
                        LatLng coords = results[0].geometry.location;
                        rideRequest.setPickupLatitude(coords.lat);
                        rideRequest.setPickupLongitude(coords.lng);
                    }
                } catch (ApiException apiException) {
                    log.error(apiException.getMessage());
                }
            } catch (InterruptedException interruptedException) {
                log.error(interruptedException.getMessage());
            }
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }

        try {
            try {
                try {
                    GeocodingResult[] results = GeocodingApi.newRequest(ctx)
                            .address(Util.formatAddress(rideRequest.getDropoffLine1(), rideRequest.getDropoffLine2(),
                                    rideRequest.getDropoffCity())).await();
                    if (results.length > 0) {
                        LatLng coords = results[0].geometry.location;
                        rideRequest.setDropoffLatitude(coords.lat);
                        rideRequest.setDropoffLongitude(coords.lng);
                    }
                } catch (ApiException apiException) {
                    log.error(apiException.getMessage());
                }
            } catch (InterruptedException interruptedException) {
                log.error(interruptedException.getMessage());
            }
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
    }

}
