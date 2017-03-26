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
    vm.maxRidersCount = [1, 2, 3, 4, 5, 6, 7, 8];
    vm.loading = true;
    vm.loggedIn = false;

    vm.login = function() {
        UserService.get().$promise.then(function(response) {
            vm.loading = false;
            vm.loggedIn = true;
        }, function(error) {
            vm.loading = false;
            vm.loggedIn = false;
        });
    };

    vm.login();

});
