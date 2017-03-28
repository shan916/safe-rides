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

        function getCurrentRideRequest() {
            CurrentDriverRidesService.get({status: 'ASSIGNED'}).$promise.then(function(response) {
                vm.assignedRideRequest = response;

                vm.assignedRideRequest.forEach(function(element, index, assignedRideRequest) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequest[index] = rideRequest;
                    if(rideRequest.status === 'ASSIGNED'){
                      //return rideRequest;
                      vm.assignedRide = rideRequest;
                      console.log("got Assigned Ride");
                      }
                });


                console.log('got currently assigned ride requests:', response);
            }, function(error) {
                console.log('error getting currently assigned ride requests:', error);
            });
        };

        getCurrentRideRequest();

      function buildDirectionButtons(){
        if(vm.assignedRide.pickupLine1 !== undefined){
            vm.pickupAddress = "https://www.google.com/maps/place/"+vm.assignedRide.pickupLine1
            +", "+vm.assignedRide.pickupCity+ ", CA, "
            +vm.assignedRide.pickupZip;
            console.log("got buttons built");
            if(vm.assignedRide.pickupLine2 !== undefined){
              vm.pickupAddress += ", "+vm.assignedRide.pickupLine2
            }
        }
        if(vm.assignedRide.dropoffLine1 !== undefined){
            vm.dropoffAddress = "https://www.google.com/maps/place/"+vm.assignedRide.dropoffLine1
            +", "+vm.assignedRide.dropoffCity+ ", CA, "
            +vm.assignedRide.dropoffZip;
            console.log("got buttons built");
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
            //TODO notify rider, Ride on the way

            /**********************************
            ************ NOT Working **********/
            /*RideRequestService.save(vm.assignedRide).$promise.then(function(response) {
                console.log('Driver, saved riderequest:', response);
                $uibModalInstance.dismiss();
            }, function(error) {
                console.log('Driver, error saving riderequest:', error);
            }); */

                vm.isStartOdoEntered = true;
                //Ride Request -> Accepted by driver

            }

            vm.endRide = function(){
              //TODO save current rideRequest COMPLETE
              vm.isStartOdoEntered = false;
              //get new ride request
              getCurrentRideRequest();
            }

      };




    });//end Controller
