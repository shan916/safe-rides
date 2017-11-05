'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:RiderdashboardCtrl
 * @description
 * # RiderdashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('RiderdashboardCtrl', function (AuthService, ENV, $cookies, RideRequestService, RideRequest, authManager, AuthTokenService, $state, $interval, $scope, Notification, MyRideService, SettingsService, $log) {
        var vm = this;
        vm.maxRidersCount = [1, 2, 3];
        vm.loading = true;
        vm.loggedIn = false;
        vm.rideRequest = new RideRequest();
        vm.existingRide = undefined;
        vm.cancelPressed = undefined;
        vm.AcceptingNewRides = undefined;

        var REFRESH_INTERVAL = 30000;
        var rideRefresher;

        /*
         * Kick user out if authenticated and higher than rider (driver, coordinator, admin,...)
         * */
        if (authManager.isAuthenticated()) {
            vm.loading = false;
            vm.loggedIn = true;
            getRide();
        } else {
            vm.loading = false;
            vm.loggedIn = false;
        }

        // check if new rides are being accepted
        SettingsService.isLive().then(function (response) {
                if (response.data !== undefined && response.data.message !== undefined) {
                    vm.AcceptingNewRides = response.data.message === 'true';
                } else {
                    vm.AcceptingNewRides = undefined;
                }
            },
            function () {
                Notification.error({
                    message: 'Failed to retrieve Safe Rides\' operation hours.',
                    positionX: 'center',
                    delay: 10000
                });
            });

        /*
         * Gets the rider's current ride
         * */
        function getRide() {
            vm.loading = true;
            MyRideService.get().$promise.then(function (response) {
                // if a ride exists
                if (response.$status !== 204) {
                    vm.existingRide = new RideRequest(response);

                    // if ride was cancelled, allow for requesting a new one
                    if (vm.existingRide.status === 'CANCELEDBYRIDER' || vm.existingRide.status === 'CANCELEDBYCOORDINATOR' || vm.existingRide.status === 'CANCELEDBYOTHER') {
                        vm.existingRide = undefined;
                    } else {
                        // set refresh interval only if ride has been requested
                        if (!rideRefresher) {
                            rideRefresher = $interval(getRide, REFRESH_INTERVAL);
                        }
                    }
                }

                $log.debug('got ride:', response);
                vm.loading = false;
            }, function (error) {
                $log.debug('Failed to get ride:', error);
                vm.loading = false;
            });
        }

        /*
         * Cancel refresh interval on exit
         * */
        $scope.$on('$destroy', function () {
            cancelInterval();
        });

        /**
         * Cancels refresh interval
         */
        function cancelInterval() {
            if (rideRefresher) {
                $interval.cancel(rideRefresher);
            }
        }

        /*
         * Submit the ride request
         * */
        vm.submitRideRequest = function () {
            vm.loading = true;
            RideRequestService.save(vm.rideRequest).$promise.then(function (response) {
                $log.debug('ride saved:', response.data);
                getRide();
            }, function (error) {
                $log.debug('error saving ride:', error);
                vm.loading = false;
            });
        };

        vm.getVehicleDescription = function () {
            if (vm.existingRide) {
                return vm.existingRide.vehicleColor + ' ' + vm.existingRide.vehicleYear + ' ' + vm.existingRide.vehicleMake + ' ' + vm.existingRide.vehicleModel;
            }
        };

        vm.getEstimatedTime = function () {
            if (!vm.existingRide) {
                return;
            }

            if (vm.existingRide.estimatedTime === '1 hour' || vm.existingRide.estimatedTime === '> 1 hour') {
                return vm.existingRide.estimatedTime;
            } else {
                return vm.existingRide.estimatedTime + ' minutes';
            }
        };

        /**
         * Cancels the current ride if it is still UNASSIGNED
         */
        vm.cancelRide = function () {
            // first time pressing cancel
            if (!vm.cancelPressed) {
                vm.cancelPressed = true;
                return;
            }

            // cancel confirmed
            if (vm.existingRide.status === 'UNASSIGNED') {
                vm.existingRide.status = 'CANCELEDBYRIDER';
                RideRequestService.update(vm.existingRide).$promise.then(function (response) {
                    cancelInterval();
                    getRide();
                    $log.debug('Ride cancelled:', response);
                    Notification.info({
                        message: 'Your ride has been cancelled',
                        positionX: 'center',
                        delay: 6000
                    });
                }, function (error) {
                    Notification.error({
                        message: 'Your ride could not be cancelled. Please contact the Safe Rides office.',
                        positionX: 'center',
                        delay: 60000
                    });
                    $log.debug('Ride cancellation failed:', error);
                });
            }
        };

    });
