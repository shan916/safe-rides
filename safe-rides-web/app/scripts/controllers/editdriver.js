'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:EditdriverCtrl
* @description
* # EditdriverCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('EditdriverCtrl', function ($routeParams, DriverService, $location) {
    var vm = this;

    vm.NUM_REGEX = '\\d+';

    vm.yearChoices = [];

    vm.stateChoices = [
        'CA', 'AL', 'AK', 'AZ', 'AR', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA',
        'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
        'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
        'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
        'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY'
    ];

    vm.driver = {
        csusId: undefined,
        name: undefined,
        dlNumber: undefined,
        dlState: undefined,
        sex: undefined,
        active: true,
        vehicle: {
            make: undefined,
            model: undefined,
            year: undefined,
            licensePlate: undefined,
            bodyType: undefined,
            color: undefined,
            seats: undefined
        }
    };

    if ($routeParams.driverId) {
        vm.driver = DriverService.getDriver($routeParams.driverId);
    }

    for (var year = new Date().getFullYear() + 1; year >= 1980; year--) {
        vm.yearChoices.push(year);
    }

    vm.saveDriver = function() {
        if (!$routeParams.driverId) {
            DriverService.saveDriver(vm.driver);
        }
        $location.path('/managedrivers');
    };

});
