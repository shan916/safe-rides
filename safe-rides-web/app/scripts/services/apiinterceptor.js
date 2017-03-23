'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function($injector, $window) {
        return {
            request: function(req) {
                if ($window.sessionStorage.token) {
                    req.headers.Authorization = $window.sessionStorage.token;
                } else {
                    req.headers.Authorization = '';
                }
                return req;
            },
            responseError: function(rejection, $window) {
              if(!$window.sessionStorage.token)
                $injector.get('$state').go('login');
            }
        };
    });
