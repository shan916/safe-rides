'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.driverNight
 * @description
 * # driverNight
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
  .factory('DriverNight', function () {

      function DriverNight(data) {
          this.distanceDrivenOdometer = undefined;
          this.distanceDrivenSystem = undefined;
          this.rides = undefined;

          if (data) {
              angular.extend(this, data);
          }
      }

      return DriverNight;
  });
