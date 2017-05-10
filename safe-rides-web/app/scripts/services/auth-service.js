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

        this.userAuthentication = function (credentials) {
            return $http.post(ENV.apiEndpoint + 'users/auth', credentials);
        };

        /**
         * Authenticates a rider with One Card ID
         **/
        this.riderAuthentication = function (oneCardId) {
            return $http.post(ENV.apiEndpoint + 'users/authrider', {oneCardId: oneCardId});
        };

        /**
         * Gets info of currently authenticated user
         */
        this.getAuthUserInfo = function () {
            return $http.get(ENV.apiEndpoint + 'users/me');
        };

    });
