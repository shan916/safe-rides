'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RiderdashboardCtrl
* @description
* # RiderdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RiderdashboardCtrl', function(UserService, $http, ENV, $window, $cookies, RideRequestService, RideRequest) {
    var vm = this;
    vm.maxRidersCount = [1, 2, 3];
    vm.loading = true;
    vm.loggedIn = false;
    vm.oneCardId = undefined;
    vm.rideRequest = new RideRequest();
    vm.existingRide = undefined;

    var callback = function() {
      vm.login();
    }

    function checkLogin() {
        UserService.get().$promise.then(function(response) {
            console.log(response.data);
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
            console.log(response.data);
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
            console.log('ride saved:', response.data);
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
                vm.existingRide = new RideRequest(response.data);
            }

            console.log(response.data);
            vm.loading = false;
        }, function (error) {
            console.log(error);
            vm.loading = false;
        });
    }

    checkLogin();

});
