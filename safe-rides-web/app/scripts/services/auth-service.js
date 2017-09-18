'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.AuthService
 * @description
 * # userService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .service('AuthService', function ($http, ENV) {
        /**
         * Gets info of currently authenticated user
         */
        this.getAuthUserInfo = function () {
            return $http.get(ENV.apiEndpoint + 'users/me');
        };
    });
