'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.UserService
 * @description
 * # userService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('UserService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'users/:id', {
            id: '@id'
        }, {
            update: {
                method: 'PUT'
            }
        });

    });
