'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.AuthTokenService
 * @description
 * # AuthTokenService
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .service('AuthTokenService', function($window, $cookies, jwtHelper) {
        this.getToken = function() {
            if ($window.localStorage.safeRidesToken) {
                return $window.localStorage.safeRidesToken;
            } else if ($cookies.get('safeRidesToken')) {
                return $cookies.get('safeRidesToken');
            } else {
                return null;
            }
        };

        this.setToken = function(token) {
            $window.localStorage.safeRidesToken = token;

            var expirationDate = new Date();
            expirationDate.setTime(expirationDate.getTime() + 6 * 60 * 60 * 1000);
            $cookies.put('safeRidesToken', token, {
                expires: expirationDate
            });
        };

        /**
         * roleName options: ['ROLE_ADMIN','ROLE_COORDINATOR','ROLE_DRIVER','ROLE_RIDER']
         * returns true or false
         */
        this.isInRole = function(roleName) {
            var tokenPayload = jwtHelper.decodeToken(this.getToken());
            var isInRole = false;
            tokenPayload.authorities.forEach(function(element) {
                if (element.authority === roleName) {
                    isInRole = true;
                    return;
                }
            });
            return isInRole;
        };
    });
