'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function ($injector, $window, $cookies, $q, ENV, $log) {
        var redirect = '/';

        return {
            request: function (req) {
                if ($window.localStorage.token) {
                    req.headers.Authorization = $window.localStorage.safeRidesToken;
                } else {
                    req.headers.Authorization = $cookies.get('safeRidesToken');
                }
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
                        $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
                        break;
                    default:
                        return $q.reject(rejection);
                }
            }
        };
    });
