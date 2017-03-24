'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.userService
 * @description
 * # userService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('UserService', function($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'users/me', {});
    });
