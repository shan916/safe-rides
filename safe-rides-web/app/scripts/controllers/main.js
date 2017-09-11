'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('MainCtrl', function (authManager, AuthTokenService, $state, $window, ENV, Notification) {
        if (authManager.isAuthenticated()) {
            if (AuthTokenService.isInRole('ROLE_ADMIN')) {
                $state.go('manageCoordinators');
            } else if (AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                $state.go('coordinator');
            } else if (AuthTokenService.isInRole('ROLE_DRIVER')) {
                $state.go('driver');
            } else if (AuthTokenService.isInRole('ROLE_RIDER')) {
                $state.go('request');
            } else {
                Notification.error({
                    message: 'An application error occurred with, if this issue persists please call to request a ride.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
            }
        } else {
            $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
        }
    });
