'use strict';

/**
 * @ngdoc overview
 * @name safeRidesWebApp
 * @description
 * # safeRidesWebApp
 *
 * Main module of the application.

 if ('serviceWorker' in navigator) {
   window.addEventListener('load', function() {
     navigator.serviceWorker.register('/sw.js').then(function(registration) {
       // Registration was successful
       console.log('ServiceWorker registration successful with scope: ', registration.scope);
     }).catch(function(err) {
       // registration failed :(
       console.log('ServiceWorker registration failed: ', err);
     });
   });
 }
 */
angular
    .module('safeRidesWebApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ui.router',
        'ngSanitize',
        'ngTouch',
        'config',
        'ngMap',
        'ui.bootstrap',
        'ui.select',
        'ngMessages',
        'ui.mask',
        'angularSpinner',
        'angular-jwt',
        'ui-notification'
    ])

    .run(function ($rootScope, $window, $cookies, $state, authManager, AuthTokenService, casService, ENV, $log) {
        authManager.checkAuthOnRefresh();
        authManager.redirectWhenUnauthenticated();

        $rootScope.$on('$viewContentLoading', function () {
            var ticketParamStart = window.location.search.indexOf('ticket=');
            if (ticketParamStart >= 0) {
                ticketParamStart += 7;
                casService.validate({
                    'service': ENV.casServiceName,
                    'ticket': window.location.search.substring(ticketParamStart)
                }).then(function (response) {
                    AuthTokenService.setToken(response.data.token);
                    window.location.href = window.location.href.split('?')[0];
                });
            }
        });

        $rootScope.$on('tokenHasExpired', function () {
            AuthTokenService.removeToken();
            $log.debug('Session has expired!');
        });

        $rootScope.$on('$stateChangeStart', function (event, toState) {
            $rootScope.redirect = toState.name;
        });

        $rootScope.$on('$stateChangeError', console.log.bind(console));

        $rootScope.$on('$stateChangeSuccess', function () {
            $rootScope.currentState = $state.current.name;
        });

        $rootScope.globalLogout = function () {
            AuthTokenService.removeToken();
            // update the user authentication state right away
            // angular-jwt uses $rootScope.isAuthenticated
            $rootScope.isAuthenticated = false;
            $window.location.href = ENV.logoutRedirect;
        };
    })

    .config(function ($windowProvider, $stateProvider, $urlRouterProvider, $httpProvider, jwtOptionsProvider, $logProvider) {
        $logProvider.debugEnabled(true);

        jwtOptionsProvider.config({
            authPrefix: '',
            whiteListedDomains: ['localhost', 'codeteam6.io'],
            unauthenticatedRedirector: [function () {
                var $window = $windowProvider.$get();
                $window.location.href = 'https://sacauth.csus.edu/csus.cas/login' + '?service=' + 'http://codeteam6.io/';
            }],
            tokenGetter: ['options', 'AuthTokenService', function (options, AuthTokenService) {
                // Skip authentication for any requests ending in .html
                if (options && options.url.substr(options.url.length - 5) === '.html') {
                    return null;
                } else {
                    return AuthTokenService.getToken();
                }
            }]
        });
        $httpProvider.interceptors.push('jwtInterceptor');

        $urlRouterProvider.otherwise('/');

        $stateProvider
            .state('/', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                controllerAs: 'ctrl'
            })
            .state('coordinator', {
                url: '/coordinator',
                templateUrl: 'views/main-coordinator.html',
                controller: 'CoordinatordashboardCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('editdriver', {
                url: '/editdriver/:driverId?',
                templateUrl: 'views/edit-driver.html',
                controller: 'EditdriverCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('driver', {
                url: '/driver',
                templateUrl: 'views/main-driver.html',
                controller: 'DriverdashboardCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('request', {
                url: '/request',
                templateUrl: 'views/main-request.html',
                controller: 'RiderdashboardCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('managedrivers', {
                url: '/managedrivers',
                templateUrl: 'views/manage-drivers.html',
                controller: 'ManagedriversCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('reports', {
                url: '/reports',
                templateUrl: 'views/reports.html',
                controller: 'ReportsdashboardCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('manageCoordinators', {
                url: '/manage-coordinators',
                templateUrl: 'views/manage-coordinators.html',
                controller: 'ManageCoordinatorsCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('editCoordinator', {
                url: '/edit-coordinator/:id?',
                templateUrl: 'views/edit-coordinator.html',
                controller: 'EditCoordinatorCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            })
            .state('settings', {
                url: '/settings',
                templateUrl: 'views/settings.html',
                controller: 'SettingsCtrl',
                controllerAs: 'ctrl',
                data: {
                    requiresLogin: true
                }
            });
    });
