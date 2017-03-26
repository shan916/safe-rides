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
            this.pickupZip = undefined;
            this.dropoffLine1 = undefined;
            this.dropoffLine2 = undefined;
            this.dropoffCity = undefined;
            this.dropoffZip = undefined;
            this.driver = undefined;
            this.status = undefined;
            this.cancelMessage = undefined;
            this.messageToDriver = undefined;
            this.estimatedTime = undefined;

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
                case 'INPROGRESS':
                    return 2;
                case 'COMPLETE':
                    return 3;
                case 'CANCELEDBYCOORDINATOR':
                    return 4;
                case 'CANCELEDBYREQUESTOR':
                    return 5;
                case 'CANCELEDBYDRIVER':
                    return 6;
                    case 'CANCELEDBYCOORDINATOR':
                        return 4;
            }
        };

        return RideRequest;
    });
