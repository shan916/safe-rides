'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:DriverdashboardCtrl
 * @description
 * # DriverdashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('DriverdashboardCtrl', function($scope, RideRequestService, DriverRidesService, RideRequest, CurrentDriverRidesService, Driver) {
        var vm = this;
        vm.loadingRideRequest = true;
        vm.ride = undefined;
        vm.rideRequests = [];
        vm.startOdo = undefined;
        vm.assignedRide = undefined;
        vm.isStartOdoEntered = false;
        vm.getPickupDirections = undefined;
        vm.dropoffAddress = undefined;
        vm.assignedRideRequest = undefined;
        vm.isRideAssigned = false;
        vm.showNoRequestView = false;

        function getCurrentRideRequest() {
            CurrentDriverRidesService.get({status: 'ASSIGNED'}).$promise.then(function(response) {
                vm.assignedRideRequest = response;
                vm.isRideAssigned = false;

                vm.assignedRideRequest.forEach(function(element, index, assignedRideRequest) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequest[index] = rideRequest;
                    if(rideRequest.status === 'ASSIGNED'){
                      //return rideRequest;
                      vm.isRideAssigned=true;
                      vm.isStartOdoEntered = false;
                      vm.assignedRide = rideRequest;
                      console.log("got Assigned Ride");
                      }
                });


                console.log('getCurrentRideRequest() called API success:', response);
            }, function(error) {
                console.log('error in getCurrentRideRequest():', error);
            });
        };

        getCurrentRideRequest();

      function buildDirectionButtons(){
        if(vm.assignedRide.pickupLine1 !== undefined){
            vm.pickupAddress = "https://www.google.com/maps/place/"+vm.assignedRide.pickupLine1
            +", "+vm.assignedRide.pickupCity+ ", CA, "
            +vm.assignedRide.pickupZip;
            if(vm.assignedRide.pickupLine2 !== undefined){
              vm.pickupAddress += ", "+vm.assignedRide.pickupLine2
            }
        }
        if(vm.assignedRide.dropoffLine1 !== undefined){
            vm.dropoffAddress = "https://www.google.com/maps/place/"+vm.assignedRide.dropoffLine1
            +", "+vm.assignedRide.dropoffCity+ ", CA, "
            +vm.assignedRide.dropoffZip;
            console.log("buttons built");
            if(vm.assignedRide.dropoffLine2 !== undefined){
              vm.dropoffAddress += ", "+vm.assignedRide.dropoffLine2
            }
        }
      };
      //getRideRequests();

      vm.viewRide = function() {
          if(vm.startOdo !== undefined){
            buildDirectionButtons();
            vm.assignedRide.startOdometer = vm.startOdo;
            vm.assignedRide.status = 'INPROGRESS';
            vm.isRideAssigned = true;
            vm.isStartOdoEntered = true;

            //TODO notify rider, Ride on the way

            RideRequestService.update(vm.assignedRide).$promise.then(function(response) {
                console.log('Driver, saved riderequest:', response);
            }, function(error) {
                console.log('Driver, error saving riderequest:', error);
            });


            //Ride Request -> Accepted by driver

            }
      };

      vm.endRide = function(){
        //TODO save current rideRequest COMPLETE
        vm.assignedRide.status = 'COMPLETE';
        vm.isStartOdoEntered = false;
        vm.isRideAssigned = false;
        //get new ride request
        //TODO uncomment below when able to save ride request
        //getCurrentRideRequest();
      };

      vm.refresh = function(){
        getCurrentRideRequest();
      }






    });//end Controller
