'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.riderequest
 * @description
 * # riderequest
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('RideRequest', function () {
        function RideRequest(data) {
            this.id = undefined;
            this.requestDate = undefined;
            this.lastModified = undefined;
            this.assignedDate = undefined;
            this.oneCardId = undefined;
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
            this.status = undefined;
            this.cancelMessage = undefined;
            this.messageToDriver = undefined;
            this.estimatedTime = undefined;
            this.pickupLatitude = undefined;
            this.pickupLongitude = undefined;
            this.dropoffLatitude = undefined;
            this.dropoffLongitude = undefined;
            this.driverId = undefined;
            this.driverFirstName = undefined;
            this.driverLastName = undefined;
            this.vehicleYear = undefined;
            this.vehicleColor = undefined;
            this.vehicleMake = undefined;
            this.vehicleModel = undefined;
            this.vehicleLicensePlate = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        RideRequest.prototype.statusOrderValue = function () {
            switch (this.status) {
                case 'UNASSIGNED':
                    return 0;
                case 'ASSIGNED':
                    return 1;
                case 'PICKINGUP':
                    return 2;
                case 'ATPICKUPLOCATION':
                    return 3;
                case 'DROPPINGOFF':
                    return 4;
                case 'COMPLETE':
                    return 5;
                case 'CANCELEDBYCOORDINATOR':
                    return 6;
                case 'CANCELEDBYRIDER':
                    return 7;
                case 'CANCELEDOTHER':
                    return 8;
            }
        };

        RideRequest.prototype.getVehicleDescription = function () {
            return this.vehicleColor + ' ' + this.vehicleYear + ' ' + this.vehicleMake + ' ' + this.vehicleModel;
        };

        return RideRequest;
    });
