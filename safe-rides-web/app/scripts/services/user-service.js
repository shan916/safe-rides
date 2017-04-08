'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.userService
 * @description
 * # userService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .service('UserService', function($http, ENV) {

        this.userAuthentication = function(credentials) {
            return $http.post(ENV.apiEndpoint + 'users/auth', credentials);
        };

        this.getAuthUserInfo = function() {
            return $http.get(ENV.apiEndpoint + 'users/me');
        };

    });
