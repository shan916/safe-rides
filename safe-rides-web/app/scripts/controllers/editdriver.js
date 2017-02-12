'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:EditdriverCtrl
* @description
* # EditdriverCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('EditdriverCtrl', function () {
    var vm = this;

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
        firstName: undefined,
        lastName: undefined,
        dlNumber: undefined,
        state: undefined,
        sex: undefined
    };

    vm.vehicle = {
        make: undefined,
        model: undefined,
        year: undefined,
        licensePlate: undefined,
        bodyType: undefined,
        color: undefined,
        seats: undefined
    };

    for (var year = new Date().getFullYear() + 1; year >= 1980; year--) {
        vm.yearChoices.push(year);
    }

});
