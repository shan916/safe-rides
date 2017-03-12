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

        function Driver() {
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
        }

        return Driver;

    });
