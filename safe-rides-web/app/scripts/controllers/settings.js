'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:SettingsCtrl
 * @description Settings view of the the applications
 * # SettingsCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('SettingsCtrl', function(SettingsService, Settings, authManager, AuthTokenService, $state, Notification) {
        var vm = this;
        vm.settingsLoading = true;
        vm.settings = undefined;
        vm.startTime = undefined;
        vm.endTime = undefined;
        vm.isMeridian = true;
        vm.hours = undefined;
        vm.minutes = undefined;

        /*
         * Kick user out if not authenticated or if not a coordinator or admin
         * */
        if (authManager.isAuthenticated()) {
            if (!AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                Notification.error({
                    message: 'You must be logged in as a coordinator or admin to view the settings page.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                $state.go('/');
                console.log('Not a coordinator or admin');
            } else {
                loadData();
            }
        } else {
            $state.go('login');
            console.log('Not authenticated');
        }

        vm.changedTime = function() {
            vm.settings.startTime.hour = moment(vm.startTime).hours();
            vm.settings.startTime.minute = moment(vm.startTime).minutes();
            vm.settings.endTime.hour = moment(vm.endTime).minutes();
            vm.settings.endTime.minute = moment(vm.endTime).minutes();

            if (moment(vm.endTime).diff(moment(vm.startTime)) < 0) {
                moment(vm.endTime).add(1, 'days');
            }

            vm.hours = Math.abs(moment(vm.startTime).diff(vm.endTime, 'hours') % 60);
            vm.minutes = Math.abs(moment(vm.startTime).diff(vm.endTime, 'minutes') % 60);
        }

        vm.toggleMode = function() {
            vm.isMeridian = !vm.isMeridian;
        }

        function loadData() {
            // get current settings
            SettingsService.current().then(function(response) {
                vm.settings = new Settings(response.data);

                var now = moment();
                var now2 = now.clone();
                vm.startTime = now.hour(vm.settings.startTime.hour);
                vm.startTime = vm.startTime.minute(vm.settings.startTime.minute);
                vm.endTime = now2.hour(vm.settings.endTime.hour);
                vm.endTime = vm.endTime.minute(vm.settings.endTime.minute);

                if (vm.endTime.diff(vm.startTime) < 0) {
                    vm.endTime.add(1, 'days');
                }

                vm.hours = Math.abs(moment(vm.startTime).diff(vm.endTime, 'hours') % 60);
                vm.minutes = Math.abs(moment(vm.startTime).diff(vm.endTime, 'minutes') % 60);

                vm.settingsLoading = false;

            }, function(error) {
                Notification.error({
                    message: 'An error occured with retreiving the latest application settings. Please try again at a later time.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                vm.settingsLoading = false;
            });
        }
    });
