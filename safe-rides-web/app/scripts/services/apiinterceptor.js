'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function($injector, $window, $cookies) {
        return {
            request: function(req) {
                if ($window.localStorage.token) {
                    req.headers.Authorization = $window.localStorage.token;
                } else {
                    req.headers.Authorization = $cookies.get("Authorization");
                }
                return req;
            },
            responseError: function(rejection, $window) {
                console.log(rejection);
                switch (rejection.status) {
                    case 401:
                    case -1:
                        $injector.get('$state').go('login');
                        break;
                    case 404:
                    default:
                        console.log("Error, that page does not exist");
                }
            }
        };
    });
