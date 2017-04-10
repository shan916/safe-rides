package edu.csus.asi.saferides.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.utility.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingService {
    @Value("${api-keys.geocoding}")
    private String geocodingApiKey;

    public void setCoordinates(RideRequest rideRequest){
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
                    // 'handle' exception
                    // over query limit, etc
                    System.out.println(apiException.getMessage());
                }
            } catch (InterruptedException interruptedException) {
                // 'handle' exception
                System.out.println(interruptedException.getMessage());
            }
        } catch (IOException ioException) {
            // 'handle' exception
            System.out.println(ioException.getMessage());
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
                    // 'handle' exception
                    System.out.println(apiException.getMessage());
                }
            } catch (InterruptedException interruptedException) {
                // 'handle' exception
                System.out.println(interruptedException.getMessage());
            }
        } catch (IOException ioException) {
            // 'handle' exception
            System.out.println(ioException.getMessage());
        }
    }

}
