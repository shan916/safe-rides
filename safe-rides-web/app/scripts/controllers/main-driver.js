'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:DriverdashboardCtrl
 * @description
 * # DriverdashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('DriverdashboardCtrl', function ($scope, GetDriverMe, DriverService, RideRequestService, DriverRidesService, RideRequest, CurrentDriverRidesService, Driver, authManager, AuthTokenService,
                                                 $state, DriverSaveService, $interval, GeolocationService, CurrentDriverLocationService, GetDriverCurrentRideService, AuthService, Notification, ENV, $window, $log) {
        var vm = this;
        vm.ride = undefined;
        vm.rideRequests = [];
        vm.startOdo = undefined; //used in form to capture startOdo
        vm.endOdo = undefined; //ending odometer reading for ride request
        vm.assignedRide = undefined; //holds current ride assinged to Driver
        vm.dropoffAddress = undefined;  //used to create address
        vm.pickupAddress = undefined;
        vm.isRideAssigned = false;  //is there currently a ride for this Driver
        vm.pickedUpButtonPressed = false; //Driver must press "picked up" button to reveal the dropoff address
        vm.lastCoords = undefined;  //last known coordinates for this driver
        vm.driver = undefined;  //used to authenticate this driver is a valid user
        var REFRESH_INTERVAL = 30000;   //Refresh time in ms for refreshing getCurrentRideRequest
        var rideRefresher; //used to signify if the refresh interval has been created yet
        vm.localDriver = undefined;  //this currently signed in Driver

        /*
         * Kick user out if not authenticated or higher than driver (coordinator, admin,...) or not a driver
         * */
        if (authManager.isAuthenticated()) {
            if (AuthTokenService.isInRole('ROLE_COORDINATOR') ||                                            // Coordinator and up
                (AuthTokenService.isInRole('ROLE_RIDER') && !AuthTokenService.isInRole('ROLE_DRIVER'))) {   // Just a rider
                Notification.error({
                    message: 'You must be logged in as a driver to view the driver dashboard.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                $state.go('/');
                $log.debug('Not a driver');
            } else {
                AuthService.getAuthUserInfo().then(function (response) {
                    vm.driver = response.data;
                }, function (error) {
                    $log.debug('error getting the driver name', error);
                });
                $log.debug('about to call getCurrentRideRequest');
                getCurrentRideRequest();
            }
        } else {
            $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
            $log.debug('Not authenticated');
        }

        /*
         *   gets the currently assigned ride request
         *   and assigns state of driver with booleans
         *   isRideAssigned, pickedUpButtonPressed
         */
        function getCurrentRideRequest() {
            vm.isRideAssigned = false;
            //get the current ride request to driver
            GetDriverCurrentRideService.get().$promise.then(function (response) {
                if (response !== undefined) {
                    vm.assignedRide = new RideRequest(response);
                    if (vm.assignedRide.status !== undefined) {
                        vm.isRideAssigned = true;
                        vm.pickedUpButtonPressed = false;
                        if (vm.assignedRide.status === 'DROPPINGOFF') {
                            vm.pickedUpButtonPressed = true;//different from PICKINGUP
                        }
                        //creat address links
                        buildDirectionButtons();
                    } else {
                        vm.isRideAssigned = false;
                    }
                    //create refresh interval
                    if (!rideRefresher) {
                        rideRefresher = $interval(getCurrentRideRequest, REFRESH_INTERVAL);
                    } else {
                        $log.debug('REFRESH_INTERVAL already ran');
                    }
                } //end if
            }, function (error) {
                $log.debug('No Assigned Rides: ', error);
                vm.isRideAssigned = false;
            });
        }//end newgetCurrentRideRequest

        /*
         *   Creates links for directions to pickup and dropoff addresses on google maps
         */
        function buildDirectionButtons() {
            if (vm.assignedRide.pickupLine1 !== undefined) {
                vm.pickupAddress = 'https://www.google.com/maps/place/' + vm.assignedRide.pickupLine1 +
                    ', ' + vm.assignedRide.pickupCity + ', CA';
            }
            if (vm.assignedRide.dropoffLine1 !== undefined) {
                vm.dropoffAddress = 'https://www.google.com/maps/place/' + vm.assignedRide.dropoffLine1 +
                    ', ' + vm.assignedRide.dropoffCity + ', CA';
                $log.debug('buttons built');
            }
        }

        /*
         *   updates currently assigned ride request
         *   by passing the ride request id
         */
        function updateRideRequest() {
            RideRequestService.update({id: vm.assignedRide.id}, vm.assignedRide).$promise.then(function (response) {
                $log.debug('Driver, saved riderequest:', response);
            }, function (error) {
                $log.debug('Driver, error saving riderequest:', error);
            });
        }


        /*  enter odometer and view newly assigned ride
         *   New Ride Assigned panel
         *   When driver is assigned a ride this screen asks for the
         *   Drivers odometer reading in order to reveal the pickup address
         *   and current ride panel
         */
        vm.viewRide = function () {
            //Stop refresh when a new ride is assigned.
            //No new data
            if (rideRefresher) {
                $interval.cancel(rideRefresher);
            }
            if (vm.startOdo !== undefined) {
                buildDirectionButtons();
                vm.assignedRide.status = 'PICKINGUP';
                vm.isRideAssigned = true;
                vm.pickedUpButtonPressed = false;
                updateRideRequest();
            }
        };

        /*
         *   When "pickedUp" button is pressed the Driver has signified
         *   they have picked up the rider/passengers and is now heading
         *   towards the dropoff location
         */
        vm.pickedUp = function () {
            vm.assignedRide.status = 'DROPPINGOFF';
            vm.isRideAssigned = true;
            vm.pickedUpButtonPressed = true;
            updateRideRequest();
        };

        /*
         *   Ending the ride request requires the Driver enters the
         *   ending odometer reading
         */
        vm.endRide = function () {
            vm.assignedRide.status = 'COMPLETE';
            //if(vm.assignedRide.startOdo > vm.endOdo){}
            vm.assignedRide.endOdometer = vm.endOdo;
            updateRideRequest();
            vm.isRideAssigned = false;
            vm.pickedUpButtonPressed = false;
            $interval(getCurrentRideRequest, REFRESH_INTERVAL);
        };

        /*
         *   changes the ride request status to notify the rider
         *   the driver has arrived at their pickup location
         */
        vm.notifyRider = function () {
            if (vm.assignedRide.status !== 'ATPICKUPLOCATION') {
                vm.assignedRide.status = 'ATPICKUPLOCATION';
                updateRideRequest();
            } else {
                $log.debug('Already notified rider');
            }
            vm.isRideAssigned = true;
            vm.pickedUpButtonPressed = false;
            Notification.success({
                message: vm.assignedRide.requestorFirstName + '&nbsp;' + vm.assignedRide.requestorLastName + ' has been notified.',
                positionX: 'center',
                delay: 4000,
                replaceMessage: true
            });
        };

        /**
         *   Driver can manually refresh to see if they
         *   have a ride assigned to them
         */
        vm.refresh = function () {
            getCurrentRideRequest();
        };

        /*
         * updates this drivers current location if allowed in browser
         */
        var updateLocation = function () {
            GeolocationService.getCurrentPosition().then(function (location) {
                var coords = {
                    'latitude': location.coords.latitude,
                    'longitude': location.coords.longitude
                };

                if (!vm.lastCoords || calcCrow(vm.lastCoords.latitude, vm.lastCoords.longitude, coords.latitude, coords.longitude) > 75) {
                    CurrentDriverLocationService.save(coords).$promise.then(function (response) {
                        $log.debug('saved driver\'s location:', response);
                    }, function (error) {
                        $log.debug('error saving driver\'s location:', error);
                    });
                } else {
                    $log.debug('Distance is not long enough to update the api.');
                }

                vm.lastCoords = coords;
            });
        };

        var locationUpdater = $interval(updateLocation, 15000);

        // helpers for calculating coord distance
        // http://stackoverflow.com/a/18883819
        function calcCrow(_lat1, _lon1, _lat2, _lon2) {
            var R = 6371e3; // meters
            var dLat = toRad(_lat2 - _lat1);
            var dLon = toRad(_lon2 - _lon1);
            var lat1 = toRad(_lat1);
            var lat2 = toRad(_lat2);

            var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            var d = R * c;
            return d;
        }

        // Converts numeric degrees to radians
        function toRad(Value) {
            return Value * Math.PI / 180;
        }

        /*
         * Destroy refresh interval on exit
         * */
        $scope.$on('$destroy', function () {
            if (rideRefresher) {
                $interval.cancel(rideRefresher);
            }
        });

        // destroy interval on exit
        $scope.$on('$destroy', function () {
            if (locationUpdater) {
                $interval.cancel(locationUpdater);
            }
        });


    });//end Controller
