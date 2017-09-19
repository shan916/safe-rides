'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.driver
 * @description
 * # driver
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Driver', function (Vehicle) {

        function Driver(data) {
            this.username = undefined;
            this.driverFirstName = undefined;
            this.driverLastName = undefined;
            this.phoneNumber = undefined;
            this.dlChecked = undefined;
            this.insuranceChecked = undefined;
            this.insuranceCompany = undefined;
            this.active = true;
            this.vehicle = new Vehicle();
            this.status = undefined;
            this.location = undefined;
            this.currentRideRequest = undefined;

            if (data) {
                if (data.currentRideRequest === undefined || data.currentRideRequest === null || data.currentRideRequest.status === 'COMPLETE') {
                    data.status = 'AVAILABLE';
                } else {
                    data.status = data.currentRideRequest.status;
                }
                angular.extend(this, data);

            }
        }

        Driver.prototype.statusOrderValue = function () {
            switch (this.status) {
                case 'AVAILABLE':
                    return 0;
                case 'ASSIGNED':
                    return 1;
                case 'PICKINGUP':
                    return 2;
                case 'ATPICKUPLOCATION':
                    return 3;
                case 'DROPPINGOFF':
                    return 4;
            }
        };

        return Driver;

    });
