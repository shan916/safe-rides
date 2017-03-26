'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RiderdashboardCtrl
* @description
* # RiderdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RiderdashboardCtrl', function(UserService) {
    var vm = this;
    UserService.get().$promise.then(function(response) {
        console.log(response);
    }, function(error) {
        console.log(error);
    });
    vm.maxRidersCount = [1, 2, 3, 4, 5, 6, 7, 8];
});
