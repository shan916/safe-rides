'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RiderdashboardCtrl
* @description
* # RiderdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RiderdashboardCtrl', function(UserService, $http, ENV, $window, $cookies, RideRequestService, RideRequest, authManager, AuthTokenService, $state, $interval, $scope) {
    var vm = this;
    vm.maxRidersCount = [1, 2, 3];
    vm.loading = true;
    vm.loggedIn = false;
    vm.oneCardId = undefined;
    vm.rideRequest = new RideRequest();
    vm.existingRide = undefined;

    var REFRESH_INTERVAL = 30000;
    var rideRefresher;

    /*
    * Gets the rider's current ride
    * */
    function getRide() {
        vm.loading = true;
        $http.get(ENV.apiEndpoint + 'rides/mine').then(function(response) {
            if (response.data && response.data !== '') {
                vm.existingRide = new RideRequest(response.data);

                // set refresh interval only if ride has been requested
                if (!rideRefresher) {
                    rideRefresher = $interval(getRide, REFRESH_INTERVAL);
                }
            }

            console.log(response.data);
            vm.loading = false;
        }, function (error) {
            console.log(error);
            vm.loading = false;
        });
    }

    /*
    * Destroy refresh interval on exit
    * */
    $scope.$on('$destroy', function() {
        if (rideRefresher) {
            $interval.cancel(rideRefresher);
        }
    });

    /*
    * Kick user out if authenticated and higher than rider (driver, coordinator, admin,...)
    * */
    if (authManager.isAuthenticated()) {
        if (AuthTokenService.isInRole('ROLE_DRIVER')) {
            $state.go('/');
            console.log('Not a requestor');
            return;
        } else {
            vm.loading = false;
            vm.loggedIn = true;
            getRide();
        }
    } else {
        vm.loading = false;
        vm.loggedIn = false;
    }

    /*
    * Login driver after one card id has been entered
    * */
    vm.login = function() {
        vm.loading = true;
        UserService.riderAuthentication(vm.oneCardId).then(function(response) {
            vm.loggedIn = true;
            AuthTokenService.setToken(response.data.token);
            getRide();
        }, function(error) {
            console.log(error);
            vm.loading = false;
        });
    };

    /*
    * Submit the ride request
    * */
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

    vm.getVehicleDescription = function() {
        if (vm.existingRide) {
            return vm.existingRide.vehicleColor + ' ' + vm.existingRide.vehicleYear + ' ' + vm.existingRide.vehicleMake + ' ' + vm.existingRide.vehicleModel;
        }
    };

});
