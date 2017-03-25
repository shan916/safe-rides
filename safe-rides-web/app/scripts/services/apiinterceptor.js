'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('APIInterceptor', function($injector, $window, $cookies, $q) {
        var redirect = '/';

        return {
            request: function(req) {
                if ($window.localStorage.token) {
                    req.headers.Authorization = $window.localStorage.safeRidesToken;
                } else {
                    req.headers.Authorization = $cookies.get('safeRidesToken');
                }
                return req;
            },
            responseError: function(rejection, $window) {
                var state = $injector.get('$state');

                if (redirect !== state.current.name && state.current.name !== 'login') {
                    redirect = state.current.name;
                }

                console.log(rejection);
                switch (rejection.status) {
                    case 401:
                        state.go('login', {
                            redirect: redirect
                        });
                        break;
                    case -1:
                        state.go('login', {
                            redirect: redirect
                        });
                        break;
                    default:
                        return $q.reject(rejection);
                }
            }
        };
    });
