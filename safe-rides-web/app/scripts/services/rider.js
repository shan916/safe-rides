'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.rider
 * @description
 * # rider
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
  .factory('Rider', function () {

    function Rider() {
      this.csusid = undefined;
      this.phone = undefined;
      this.firstname = undefined;
      this.lastname = undefined;
    }

    return Rider;
  });
