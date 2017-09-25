'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function ($injector, AuthTokenService, $window, $cookies, $q, ENV, $log, $rootScope) {
        return {
            request: function (req) {
                return req;
            },
            responseError: function (rejection) {
                var state = $injector.get('$state');

                $log.debug(rejection);
                switch (rejection.status) {
                    case 401:
                    case 403:
                        AuthTokenService.removeToken();
                        $rootScope.isAuthenticated = false;
                        state.go('/');
                        break;
                    default:
                        return $q.reject(rejection);
                }
            }
        };
    });
