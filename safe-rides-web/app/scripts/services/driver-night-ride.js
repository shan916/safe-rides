'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.driverNightRide
 * @description
 * # driverNightRide
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('DriverNightRide', function () {
        function DriverNightRide(data) {
            this.rideAssigned = undefined;
            this.rideCompleted = undefined;
            this.startOdometer = undefined;
            this.endOdometer = undefined;
            this.odometerDistance = undefined;
            this.recordedDistance = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        return DriverNightRide;
    });
