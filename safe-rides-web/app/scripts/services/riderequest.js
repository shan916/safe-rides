'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.riderequest
 * @description
 * # riderequest
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('RideRequest', function() {
        function RideRequest(data) {
            this.id = undefined;
            this.oneCardId = undefined;
            this.requestDate = undefined;
            this.requestorFirstName = undefined;
            this.requestorLastName = undefined;
            this.requestorPhoneNumber = undefined;
            this.numPassengers = undefined;
            this.startOdometer = undefined;
            this.endOdometer = undefined;
            this.pickupLine1 = undefined;
            this.pickupLine2 = undefined;
            this.pickupCity = undefined;
            this.dropoffLine1 = undefined;
            this.dropoffLine2 = undefined;
            this.dropoffCity = undefined;
            this.driver = undefined;
            this.status = undefined;
            this.cancelMessage = undefined;
            this.messageToDriver = undefined;
            this.estimatedTime = undefined;
            this.pickupLatitude = undefined;
            this.pickupLongitude = undefined;
            this.dropoffLatitude = undefined;
            this.dropoffLongitude = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        RideRequest.prototype.statusOrderValue = function() {
            switch (this.status) {
                case 'UNASSIGNED':
                    return 0;
                case 'ASSIGNED':
                    return 1;
                case 'PICKINGUP':
                    return 2;
                case 'DROPPINGOFF':
                    return 3;
                case 'COMPLETE':
                    return 4;
                case 'CANCELEDBYCOORDINATOR':
                    return 5;
                case 'CANCELEDBYRIDER':
                    return 6;
                case 'CANCELEDOTHER':
                    return 7;
            }
        };

        RideRequest.prototype.getVehicleDescription = function() {
            return this.driver.vehicle.color + ' ' + this.driver.vehicle.year + ' ' + this.driver.vehicle.make + ' ' + this.driver.vehicle.model;
        };

        return RideRequest;
    });
