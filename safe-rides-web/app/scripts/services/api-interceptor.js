'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function ($injector, AuthTokenService, $window, $cookies, $q, ENV, $log) {
        var redirect = '/';

        return {
            request: function (req) {
                req.headers.Authorization = AuthTokenService.getToken();
                return req;
            },
            responseError: function (rejection) {
                var state = $injector.get('$state');

                if (redirect !== state.current.name && state.current.name !== 'login') {
                    redirect = state.current.name;
                }

                $log.debug(rejection);
                switch (rejection.status) {
                    case 401:
                        state.go('/');
                        break;
                    default:
                        return $q.reject(rejection);
                }
            }
        };
    });
