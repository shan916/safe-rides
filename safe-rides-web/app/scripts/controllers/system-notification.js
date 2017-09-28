'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:SystemNotificationCtrl
 * @description
 * # SystemNotificationCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('SystemNotificationCtrl', function ($rootScope, $log, SettingsService, Settings) {
        var vm = this;
        vm.settings = undefined;
        vm.isLive = undefined;

        $rootScope.$on('$stateChangeSuccess', function () {
            SettingsService.message().then(function (response) {
                console.log(response);
                if (response.status !== 204 && response.data !== undefined && response.data.message !== undefined) {
                    vm.message = response.data.message;
                } else {
                    vm.message = undefined;
                }
            }, function (error) {
                $log.debug('Error getting system notification message:', error);
            });

            SettingsService.isLive().then(function (response) {
                vm.isLive = response.data;

            }, function (error) {
                $log.debug('Error getting system status:', error);
            });
        });
    });
