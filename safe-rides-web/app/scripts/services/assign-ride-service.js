'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.assignRideService
 * @description
 * # assignRideService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('AssignRideService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'drivers/:id/rides', {
            id: '@id'
        });

    });
