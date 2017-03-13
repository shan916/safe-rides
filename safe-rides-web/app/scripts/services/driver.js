'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.driver
 * @description
 * # driver
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Driver', function(Vehicle) {

        function Driver(data) {
            this.csusId = undefined;
            this.driverFirstName = undefined;
            this.driverLastName = undefined;
            this.phoneNumber = undefined;
            this.dlState = undefined;
            this.dlNumber = undefined;
            this.gender = undefined;
            this.insuranceChecked = undefined;
            this.active = true;
            this.vehicle = new Vehicle();
            this.status = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        Driver.prototype.statusOrderValue = function() {
            switch (this.status) {
                case 'AVAILABLE':
                    return 0;
                case 'ASSIGNED':
                    return 1;
                case 'INPROGRESS':
                    return 2;
            }
        };

        return Driver;

    });
