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
      this.csusid = undefined;
      this.phone = undefined;
      this.firstname = undefined;
      this.lastname = undefined;
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
      this.driver =undefined;
    }

    return RideRequest;
  });
