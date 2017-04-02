'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.riderequest
 * @description
 * # riderequest
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('RideRequest', function(Driver) {
        function RideRequest(data) {
            this.requestorId = undefined;
            this.date = undefined;
            this.requestorContactNumber = undefined;
            this.requestorFirstName = undefined;
            this.requestorLastName = undefined;
            this.pickupLine1 = undefined;
            this.pickupLine2 = undefined;
            this.pickupCity = undefined;
            this.pickupZip = undefined;
            this.dropoffLine1 = undefined;
            this.dropoffLine2 = undefined;
            this.dropoffCity = undefined;
            this.dropoffZip = undefined;
            this.numPassengers = undefined;
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

        return RideRequest;
    });
