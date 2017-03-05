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
    function RideRequest() {
      this.requestorId = undefined;
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
    }

    return RideRequest;
  });
