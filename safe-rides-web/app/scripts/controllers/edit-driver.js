'use strict';

//noinspection JSAnnotator
/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:EditdriverCtrl
 * @description
 * # EditdriverCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('EditdriverCtrl', function ($stateParams, $state, DriverService, Driver, $log) {
        var vm = this;

        vm.driver = new Driver();

        vm.NUM_REGEX = '\\d+';

        vm.yearChoices = [];

        vm.loading = false;

        vm.existingDriver = !!$stateParams.driverId;

        function getDriver(driverId) {
            vm.loading = true;
            DriverService.get({
                id: driverId
            }).$promise.then(function (response) {
                vm.loading = false;
                vm.driver = response;
                $log.debug('got driver:', response);
            }, function (error) {
                vm.loading = false;
                $log.debug('error getting driver:', error);
            });
        }

        function updateDriver() {
            DriverService.update({
                id: vm.driver.id
            }, vm.driver).$promise.then(function (response) {
                $log.debug('updated driver:', response);
                $state.go('managedrivers');
            }, function (error) {
                $log.debug('error updating driver:', error);
            });
        }

        if (vm.existingDriver) {
            getDriver($stateParams.driverId);
        }

        for (var year = new Date().getFullYear() + 1; year >= 1980; year--) {
            vm.yearChoices.push(year);
        }

        vm.saveDriver = function () {
            if (vm.existingDriver) {
                updateDriver();
            } else {
                DriverService.save(vm.driver).$promise.then(function (response) {
                    $log.debug('saved driver:', response);
                    $state.go('managedrivers');
                }, function (error) {
                    $log.debug('error saving driver:', error);
                });
            }
        };

    });
