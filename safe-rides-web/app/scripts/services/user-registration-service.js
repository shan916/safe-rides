'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.userRegistrationService
 * @description
 * # userRegistrationService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('UserRegistrationService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'users/:id', {
            id: '@id'
        }, {
            update: {
                method: 'PUT'
            }
        });
    });
