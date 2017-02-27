'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.riderequest
 * @description
 * # riderequest
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
  .factory('RideRequest', function (Rider) {
    function RideRequest() {
      this.rider = new Rider();
      this.pickupLine1 = undefined;
      this.pickupLine2 = undefined;
      this.pickupCity = undefined;
      this.pickupZip = undefined;
      this.dropOffLine1 = undefined;
      this.dropOffLine2 = undefined;
      this.dropOffCity = undefined;
      this.dropOffZip = undefined;
      this.peopleCount = undefined;
      this.msg = undefined;
    }

    return RideRequest;
  });
