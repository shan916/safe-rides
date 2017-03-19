'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.vehicle
 * @description
 * # vehicle
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Vehicle', function() {

        function Vehicle() {
            this.make = undefined;
            this.model = undefined;
            this.year = undefined;
            this.licensePlate = undefined;
            this.color = undefined;
            this.seats = undefined;
        }

        return Vehicle;

    });
