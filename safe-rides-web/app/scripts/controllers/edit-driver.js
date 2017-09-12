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
    .controller('EditdriverCtrl', function ($stateParams, $state, DriverService, Driver) {
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
                console.log('got driver:', response);
            }, function (error) {
                vm.loading = false;
                console.log('error getting driver:', error);
            });
        }

        function updateDriver() {
            DriverService.update({
                id: vm.driver.id
            }, vm.driver).$promise.then(function (response) {
                console.log('updated driver:', response);
                $state.go('managedrivers');
            }, function (error) {
                console.log('error updating driver:', error);
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
                    console.log('saved driver:', response);
                    $state.go('managedrivers');
                }, function (error) {
                    console.log('error saving driver:', error);
                });
            }
        };

    });
