'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:EditdriverCtrl
 * @description
 * # EditdriverCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('EditdriverCtrl', function ($stateParams, $location, DriverService, Driver) {
        var vm = this;

        vm.driver = new Driver();

        vm.NUM_REGEX = '\\d+';

        vm.yearChoices = [];

        vm.loading = false;

        vm.stateChoices = [
            'CA', 'AL', 'AK', 'AZ', 'AR', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA',
            'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
            'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
            'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
            'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY'
        ];

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
                $location.path('/managedrivers');
            }, function (error) {
                console.log('error updating driver:', error);
            });
        }

        if ($stateParams.driverId) {
            getDriver($stateParams.driverId);
        }

        for (var year = new Date().getFullYear() + 1; year >= 1980; year--) {
            vm.yearChoices.push(year);
        }

        vm.saveDriver = function () {
            if ($stateParams.driverId) {
                updateDriver();
            } else {
                DriverService.save(vm.driver).$promise.then(function (response) {
                    console.log('saved driver:', response);
                    $location.path('/managedrivers');
                }, function (error) {
                    console.log('error saving driver:', error);
                });
            }
        };

    });
