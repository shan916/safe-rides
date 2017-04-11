'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RiderdashboardCtrl
* @description
* # RiderdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RiderdashboardCtrl', function(UserService, $http, ENV, $window, $cookies, RideRequestService, RideRequest, authManager, AuthTokenService, $state, $interval, $scope, Notification) {
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
     * Kick user out if authenticated and higher than rider (driver, coordinator, admin,...)
     * */
    if (authManager.isAuthenticated()) {
        // TODO: also check if role is coordinator. Should display a notification asking user to be logged out?
        if (AuthTokenService.isInRole('ROLE_DRIVER')) {
            Notification.error({
                message: 'You must be a ride requestor to request a ride.',
                positionX: 'center',
                delay: 10000
            });
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

            if (error.status === 429) {
                Notification.error({message: 'Too many requests, please try again after 1 minute.', positionX: 'center', delay: 60000, closeOnClick: false, replaceMessage: true});
            }
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
