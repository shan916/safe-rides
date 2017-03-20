'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.driver
 * @description
 * # driver
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Driver', function(Vehicle, DriverRidesService) {

        function Driver(data) {
            this.csusId = undefined;
            this.driverFirstName = undefined;
            this.driverLastName = undefined;
            this.phoneNumber = undefined;
            this.dlState = undefined;
            this.dlNumber = undefined;
            this.gender = undefined;
            this.insuranceChecked = undefined;
            this.insuranceCompany = undefined;
            this.active = true;
            this.vehicle = new Vehicle();
            this.status = undefined;
            this.rides = undefined;

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

        // returns the first assigned or inprogress ride
        Driver.prototype.currentRide = function() {
            for (var i = 0; i < this.rides.length; i++) {
                if (this.rides[i].status === 'ASSIGNED' || this.rides[i].status === 'INPROGRESS') {
                    return this.rides[i];
                }
            }
        };

        return Driver;

    });
