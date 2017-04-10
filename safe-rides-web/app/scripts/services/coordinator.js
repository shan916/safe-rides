'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.coordinator
 * @description
 * # coordinator
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Coordinator', function(Vehicle, CoordinatorRidesService) {

        function Coordinator(data) {
            this.csusId = undefined;
            this.coordinatorFirstName = undefined;
            this.coordinatorLastName = undefined;
            this.phoneNumber = undefined;
            this.active = true;

            if (data) {
                angular.extend(this, data);
            }
        }

        // returns the first assigned or inprogress ride


        return Coordinator;

    });
