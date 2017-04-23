'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:DriverdashboardCtrl
* @description
* # DriverdashboardCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('DriverdashboardCtrl', function($scope, DriverService, RideRequestService, DriverRidesService, RideRequest, CurrentDriverRidesService, Driver, authManager, AuthTokenService,
                                            $state, DriverSaveService, $interval, GeolocationService, CurrentDriverLocationService, GetDriverCurrentRideService, UserService, Notification) {
    var vm = this;
    vm.loadingRideRequest = true;
    vm.ride = undefined;
    vm.rideRequests = [];
    vm.startOdo = undefined;
    vm.endOdo = undefined;
    vm.endNightOdo = undefined;
    vm.assignedRide = undefined;
    vm.getPickupDirections = undefined;
    vm.dropoffAddress = undefined;
    vm.assignedRideRequest = undefined;
    vm.isRideAssigned = false;
    vm.pickedUpButtonPressed = false;
    vm.lastCoords = undefined;
    vm.driver = undefined;
    var REFRESH_INTERVAL = 30000;
    var rideRefresher;
    vm.endNightPressed = false;
    vm.currentRideRequest = undefined;

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
            console.log('Not a driver');
        } else {
            UserService.getAuthUserInfo().then(function(response){
                vm.driver = response.data;
            }, function(error){
                console.log('error getting the driver name', error);
            });

            getCurrentRideRequest();
        }
    } else {
        $state.go('login');
        console.log('Not authenticated');
    }

    function getCurrentRideRequest() {
        vm.isRideAssigned = false;
        GetDriverCurrentRideService.get().$promise.then(function(response) {
            if(response === undefined){
                console.log('response is undefined');
                return;
            } else {
                vm.assignedRide = new RideRequest(response);
                if(vm.assignedRide.status === 'ASSIGNED'){
                    vm.isRideAssigned=true;
                    vm.pickedUpButtonPressed = false;
                    console.log('got Assigned Ride, ASSIGNED');
                } else if (vm.assignedRide.status === 'PICKINGUP') {
                    vm.isRideAssigned = true;
                    vm.pickedUpButtonPressed = false;
                    console.log('got Assigned Ride, PICKINGUP');
                } else if (vm.assignedRide.status === 'ATPICKUPLOCATION'){
                    vm.isRideAssigned = true;
                    vm.pickedUpButtonPressed = false;//different from PICKINGUP
                    console.log('got Assigned Ride, ATPICKUPLOCATION');
                } else if (vm.assignedRide.status === 'DROPPINGOFF'){
                    vm.isRideAssigned = true;
                    vm.pickedUpButtonPressed = true;//different from PICKINGUP
                    console.log('got Assigned Ride, DROPPINGOFF');
                }
                buildDirectionButtons();

                if (!rideRefresher) {
                    rideRefresher = $interval(getCurrentRideRequest, REFRESH_INTERVAL);
                }else {
                    console.log('REFRESH_INTERVAL already ran');
                }
            }

        }, function(error){
            console.log('No Assigned Rides');
            vm.isRideAssigned = false;
        });
    }//end newgetCurrentRideRequest

    function buildDirectionButtons(){
        if(vm.assignedRide.pickupLine1 !== undefined){
            vm.pickupAddress = 'https://www.google.com/maps/place/' + vm.assignedRide.pickupLine1 +
            ', ' + vm.assignedRide.pickupCity + ', CA';
        }
        if(vm.assignedRide.dropoffLine1 !== undefined){
            vm.dropoffAddress = 'https://www.google.com/maps/place/' + vm.assignedRide.dropoffLine1 +
            ', ' + vm.assignedRide.dropoffCity + ', CA';
            console.log('buttons built');
        }
    }

    function updateDriver() {
        DriverSaveService.update(vm.driver).$promise.then(function(response) {
            console.log('saved driver endNightOdo:', response);
        }, function(error) {
            console.log('error saving driver endNightOdo:', error);
        });
    };

    function updateRideRequest(){
        RideRequestService.update({id: vm.assignedRide.id}, vm.assignedRide).$promise.then(function(response) {
            console.log('Driver, saved riderequest:', response);
        }, function(error) {
            console.log('Driver, error saving riderequest:', error);
        });
    }

    //enter odometer and view newly assigned ride
    vm.viewRide = function() {
        //Stop refresh when a new ride is assigned.
        //No new data
        if (rideRefresher) {
            $interval.cancel(rideRefresher);
        }
        if(vm.startOdo !== undefined){
            buildDirectionButtons();
            vm.assignedRide.startOdometer = vm.startOdo;
            vm.assignedRide.status = 'PICKINGUP';
            vm.isRideAssigned = true;
            vm.pickedUpButtonPressed = false;

            updateRideRequest();
        }
    };

    vm.pickedUp = function(){
        vm.assignedRide.status = 'DROPPINGOFF';
        vm.isRideAssigned = true;
        vm.pickedUpButtonPressed = true;
        updateRideRequest();
    };

    vm.endRide = function() {
        vm.assignedRide.status = 'COMPLETE';
        //if(vm.assignedRide.startOdo > vm.endOdo){}
            vm.assignedRide.endOdometer = vm.endOdo;
            vm.isRideAssigned = false;
            vm.pickedUpButtonPressed = false;
            updateRideRequest();
            $interval(getCurrentRideRequest, REFRESH_INTERVAL);
    };

    vm.notifyRider = function() {
        if(vm.assignedRide.status !== 'ATPICKUPLOCATION'){
            vm.assignedRide.status = 'ATPICKUPLOCATION';
            updateRideRequest();
        }else{
            console.log("Already notified rider");
        }
            vm.isRideAssigned = true;
            vm.pickedUpButtonPressed = false;
            Notification.success({
                message: vm.assignedRide.requestorFirstName+'&nbsp;'+vm.assignedRide.requestorLastName+' has been notified.',
                positionX: 'center',
                delay: 4000,
                replaceMessage: true
            });
    };

    vm.endNight = function() {
        vm.endNightPressed = true;
    }
    vm.submitEndNightOdo = function(){
        vm.driver.endNightOdo = vm.endNightOdo;
        updateDriver();
    }
    vm.cancelEndNight = function(){
        vm.endNightPressed = false;
    }

    /**
    *   Driver can manually refresh to see if they
    *   have a ride assigned to them
    */
    vm.refresh = function(){
        getCurrentRideRequest();
    };


    var updateLocation = function() {
        GeolocationService.getCurrentPosition().then(function(location) {
            var coords = {
                'latitude': location.coords.latitude,
                'longitude': location.coords.longitude
            };

            if (!vm.lastCoords || calcCrow(vm.lastCoords.latitude, vm.lastCoords.longitude, coords.latitude, coords.longitude) > 75) {
                CurrentDriverLocationService.save(coords).$promise.then(function(response) {
                    console.log('saved driver\'s location:', response);
                }, function(error) {
                    console.log('error saving driver\'s location:', error);
                });
            } else {
                console.log('Distance is not long enough to update the api.');
//                getCurrentRideRequest();
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
    $scope.$on('$destroy', function() {
        if (rideRefresher) {
            $interval.cancel(rideRefresher);
        }
    });

    // destroy interval on exit
    $scope.$on('$destroy', function() {
        if (locationUpdater) {
            $interval.cancel(locationUpdater);
        }
    });


});//end Controller
