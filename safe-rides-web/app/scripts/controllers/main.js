'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('MainCtrl', function (authManager, AuthTokenService, $state, $window, $cookies, ENV, Notification, $log) {
        if (authManager.isAuthenticated()) {
            if (AuthTokenService.isInRole('ROLE_ADMIN')) {
                $log.debug('ADMIN');
                $state.go('coordinator');
            } else if (AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                $log.debug('COORDINATOR');
                $state.go('coordinator');
            } else if (AuthTokenService.isInRole('ROLE_DRIVER')) {
                $log.debug('DRIVER');
                $state.go('driver');
            } else if (AuthTokenService.isInRole('ROLE_RIDER')) {
                $log.debug('RIDER');
                $state.go('request');
            } else {
                $log.debug('ROLE FALLTHROUGH');
                Notification.error({
                    message: 'An application error occurred with logging in, if this issue persists please call to request a ride.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
            }
        } else {
            if (window.location.search.indexOf('ticket=') < 0) {
                $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
            }
        }
    });
