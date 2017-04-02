'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RiderdashboardCtrl
* @description
* # RiderdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RiderdashboardCtrl', function(UserService, $http, ENV, $window, $cookies, RideRequestService) {
    var vm = this;
    vm.maxRidersCount = [1, 2, 3];
    vm.loading = true;
    vm.loggedIn = false;
    vm.oneCardId = undefined;
    vm.rideRequest = undefined;
    vm.existingRide = undefined;

    function checkLogin() {
        UserService.get().$promise.then(function(response) {
            console.log(response);
            vm.loading = false;
            vm.loggedIn = true;
            getRide();
        }, function(error) {
            console.log(error);
            vm.loading = false;
            vm.loggedIn = false;
        });
    }

    vm.login = function() {
        vm.loading = true;
        $http.post(ENV.apiEndpoint + 'users/authByID', {
            oneCardId: vm.oneCardId
        }).then(function(response) {
            console.log(response);
            vm.loggedIn = true;
            saveToken(response.data.token);
            getRide();
        }, function(error) {
            console.log(error);
            vm.loading = false;
        });
    };

    vm.submitRideRequest = function() {
        vm.loading = true;
        RideRequestService.save(vm.rideRequest).$promise.then(function(response) {
            console.log('ride saved:', response);
            getRide();
        }, function(error) {
            console.log('error saving ride:', error);
            vm.loading = false;
        });
    };

    function saveToken(token) {
        var expirationDate = new Date();
        expirationDate.setTime(expirationDate.getTime() + 6 * 60 * 60 * 1000);
        $window.localStorage.safeRidesToken = token;
        $cookies.put('safeRidesToken', token, {
            expires: expirationDate
        });
    }

    function getRide() {
        $http.get(ENV.apiEndpoint + 'rides/mine').then(function(response) {
            if (response.data && response.data !== '') {
                vm.existingRide = response.data;
            }

            console.log(response);
            vm.loading = false;
        }, function (error) {
            console.log(error);
            vm.loading = false;
        });
    }

    checkLogin();

});
