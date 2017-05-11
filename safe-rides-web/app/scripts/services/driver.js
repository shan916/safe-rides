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
            this.oneCardId = undefined;
            this.driverFirstName = undefined;
            this.driverLastName = undefined;
            this.phoneNumber = undefined;
            this.dlState = undefined;
            this.dlChecked = undefined;
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

        // returns the first assigned or inprogress ride
        Driver.prototype.currentRide = function () {
            for (var i = 0; i < this.rides.length; i++) {
                if (this.rides[i].status === 'ASSIGNED' || this.rides[i].status === 'PICKINGUP' || this.rides[i].status === 'DROPPINGOFF' || this.rides[i].status === 'ATPICKUPLOCATION') {
                    return this.rides[i];
                }
            }
        };

        return Driver;

    });
