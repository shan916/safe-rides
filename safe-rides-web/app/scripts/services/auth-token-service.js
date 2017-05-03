'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.AuthTokenService
 * @description
 * # AuthTokenService
 * Service in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .service('AuthTokenService', function ($window, $cookies, jwtHelper) {
        this.getToken = function () {
            if ($window.localStorage.getItem('safeRidesToken')) {
                return $window.localStorage.getItem('safeRidesToken');
            } else if ($cookies.get('safeRidesToken')) {
                return $cookies.get('safeRidesToken');
            } else {
                return null;
            }
        };

        this.setToken = function (token) {
            $window.localStorage.setItem('safeRidesToken', token);

            var expirationDate = new Date();
            expirationDate.setTime(expirationDate.getTime() + 6 * 60 * 60 * 1000);
            $cookies.put('safeRidesToken', token, {
                expires: expirationDate
            });
        };

        this.removeToken = function () {
            if ($window.localStorage.getItem('safeRidesToken')) {
                $window.localStorage.removeItem('safeRidesToken');
            }

            if ($cookies.get('safeRidesToken')) {
                return $cookies.remove('safeRidesToken');
            }
        };

        /**
         * roleName options: ['ROLE_ADMIN','ROLE_COORDINATOR','ROLE_DRIVER','ROLE_RIDER']
         * returns true or false
         */
        this.isInRole = function (roleName) {
            var tokenPayload = jwtHelper.decodeToken(this.getToken());
            var isInRole = false;

            if (tokenPayload.authorities === undefined) {
                return false;
            }

            tokenPayload.authorities.forEach(function (element) {
                if (element.authority === roleName) {
                    isInRole = true;
                    return;
                }
            });
            return isInRole;
        };

        /**
         * returns username of logged in user
         */
        this.getUsername = function () {
            return jwtHelper.decodeToken(this.getToken()).sub;
        };

    });
