'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.CoordinatorService
 * @description
 * # CoordinatorService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('CoordinatorService', function($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'coordinators/:id', {
            id: '@id'
        }, {
            update: {
                method: 'PUT'
            }
        });

    });
