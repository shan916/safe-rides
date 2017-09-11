'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.casService
 * @description
 * # casService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
  .factory('casService', function ($http, ENV) {
      this.validate = function (casTuple) {
          return $http.post(ENV.apiEndpoint + 'cas/validate', casTuple);
      };

      return {
          validate: this.validate
      };
  });
