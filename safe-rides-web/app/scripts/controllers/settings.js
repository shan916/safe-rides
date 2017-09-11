'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:SettingsCtrl
 * @description Settings view of the the applications
 * # SettingsCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('SettingsCtrl', function (SettingsService, Settings, authManager, AuthTokenService, $state, Notification, ENV, $window) {
        var vm = this;
        vm.settingsLoading = true;
        vm.settings = undefined;
        vm.startTime = undefined;
        vm.endTime = undefined;
        vm.isMeridian = true;
        vm.hours = undefined;
        vm.daysOfWeek = [{
            name: 'Monday',
            selected: false
        },
            {
                name: 'Tuesday',
                selected: false
            },
            {
                name: 'Wednesday',
                selected: false
            },
            {
                name: 'Thursday',
                selected: false
            },
            {
                name: 'Friday',
                selected: false
            },
            {
                name: 'Saturday',
                selected: false
            },
            {
                name: 'Sunday',
                selected: false
            }
        ];

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
            $window.location.href = ENV.casLogin + "?service=" + ENV.casServiceName;
            console.log('Not authenticated');
        }

        vm.changedTime = function () {
            updateTimes();
        };

        vm.changedDay = function () {
            updateDays();
        };

        vm.toggleMode = function () {
            vm.isMeridian = !vm.isMeridian;
        };

        /**
         * Persist data to server
         */
        vm.saveSettings = function () {
            SettingsService.update(vm.settings).then(function () {
                Notification.success({
                    message: 'Application settings updated.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                loadData();
            }, function () {
                Notification.error({
                    message: 'An error occured with updating the application settings. Please try again at a later time.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                loadData();
            });
        };

        /*
         * Update vm.settings.startTime and endTime with UI settings
         */
        function updateTimes() {
            if (moment(vm.endTime).diff(moment(vm.startTime), 'hours') > 23) {
                vm.startTime = moment(vm.startTime).add(1, 'days');
            }

            if (moment(vm.endTime).diff(moment(vm.startTime), 'hours') < 0) {
                vm.endTime = moment(vm.endTime).add(1, 'days');
            }

            vm.settings.startTime[0] = moment(vm.startTime).hours();
            vm.settings.startTime[1] = moment(vm.startTime).minutes();
            vm.settings.endTime[0] = moment(vm.endTime).hours();
            vm.settings.endTime[1] = moment(vm.endTime).minutes();

            vm.hours = Math.abs(moment(vm.startTime).diff(vm.endTime, 'hours') % 60);
            vm.minutes = Math.abs(moment(vm.startTime).diff(vm.endTime, 'minutes') % 60);
        }

        /*
         * Update vm.settings.daysOfWeek with UI settings
         */
        function updateDays() {
            // clear old var
            vm.settings.daysOfWeek = [];
            // re-set values
            vm.daysOfWeek.filter(function (d) {
                return d.selected === true;
            }).forEach(function (day) {
                switch (day.name) {
                    case 'Monday':
                        vm.settings.daysOfWeek.push('MONDAY');
                        break;
                    case 'Tuesday':
                        vm.settings.daysOfWeek.push('TUESDAY');
                        break;
                    case 'Wednesday':
                        vm.settings.daysOfWeek.push('WEDNESDAY');
                        break;
                    case 'Thursday':
                        vm.settings.daysOfWeek.push('THURSDAY');
                        break;
                    case 'Friday':
                        vm.settings.daysOfWeek.push('FRIDAY');
                        break;
                    case 'Saturday':
                        vm.settings.daysOfWeek.push('SATURDAY');
                        break;
                    case 'Sunday':
                        vm.settings.daysOfWeek.push('SUNDAY');
                }
            });
        }

        /**
         * Get data from the server
         */
        function loadData() {
            vm.settingsLoading = true;
            // get current settings
            SettingsService.current().then(function (response) {
                    vm.settings = new Settings(response.data);

                    // set times
                    var now = moment();
                    var now2 = now.clone();
                    vm.startTime = now.hour(vm.settings.startTime[0]);
                    vm.startTime = now.minute(vm.settings.startTime[1]);
                    vm.endTime = now2.hour(vm.settings.endTime[0]);
                    vm.endTime = vm.endTime.minute(vm.settings.endTime[1]);

                    updateTimes();

                    // set dates
                    // not checking array bounds as there SHOULD be a result no matter what
                    // the days are defined at the top of the controller.
                    vm.settings.daysOfWeek.forEach(function (day) {
                        switch (day) {
                            case 'MONDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Monday';
                                })[0].selected = true;
                                break;
                            case 'TUESDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Tuesday';
                                })[0].selected = true;
                                break;
                            case 'WEDNESDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Wednesday';
                                })[0].selected = true;
                                break;
                            case 'THURSDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Thursday';
                                })[0].selected = true;
                                break;
                            case 'FRIDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Friday';
                                })[0].selected = true;
                                break;
                            case 'SATURDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Saturday';
                                })[0].selected = true;
                                break;
                            case 'SUNDAY':
                                vm.daysOfWeek.filter(function (d) {
                                    return d.name === 'Sunday';
                                })[0].selected = true;
                        }
                    });

                    vm.settingsLoading = false;

                },
                function () {
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
