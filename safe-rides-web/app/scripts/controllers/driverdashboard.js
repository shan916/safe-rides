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
        vm.endOdo = undefined;
        vm.assignedRide = undefined;
        vm.isStartOdoEntered = false;
        vm.getPickupDirections = undefined;
        vm.dropoffAddress = undefined;
        vm.assignedRideRequest = undefined;
        vm.isRideAssigned = false;
        vm.showNoRequestView = false;
        vm.pickedUpButtonPressed = false;
        vm.inprogressFlag = false;

        function getCurrentRideRequest() {
            CurrentDriverRidesService.get({status: 'ASSIGNED'}).$promise.then(function(response) {
                vm.assignedRideRequest = response;
                vm.isRideAssigned = false;

                vm.pickedUpButtonPressed = false;
                vm.assignedRideRequest.forEach(function(element, index, assignedRideRequest) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequest[index] = rideRequest;
                    //if not in progress
                    if(!vm.inprogressFlag && rideRequest.status === 'ASSIGNED'){
                      //return rideRequest;
                      vm.assignedRide = rideRequest;
                      vm.isRideAssigned=true;
                      vm.isStartOdoEntered = false;
                      console.log("got Assigned Ride, ASSIGNED");
                      }
                });

                console.log('getCurrentRideRequest() called API success:', response);
            }, function(error) {

                console.log('getCurrentRideRequest() returned no ASSIGNED rides:', error);

            });//end CurrentDriverRidesService.get ASSIGNED

            //if there is no ride assigned check if "picking up or dropping off"
            if(vm.isRideAssigned === false){
              CurrentDriverRidesService.get({status: 'PICKINGUP'}).$promise.then(function(response) {
                vm.assignedRideRequest = response;
                vm.assignedRideRequest.forEach(function(element, index, assignedRideRequest) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequest[index] = rideRequest;
                    if(rideRequest.status === 'PICKINGUP'){
                      vm.assignedRide = rideRequest;
                      vm.isRideAssigned = true;
                      vm.isStartOdoEntered = true;
                      vm.pickedUpButtonPressed = false;
                      vm.inprogressFlag = true;
                      console.log("got Assigned Ride, PICKINGUP");
                    }
                });
                  console.log('getCurrentRideRequest() returned PICKINGUP:', response);
              }, function(error) {
                  console.log('error getCurrentRideRequest() PICKINGUP', error);
              });//end CurrentDriverRidesService.get PICKINGUP

              CurrentDriverRidesService.get({status: 'DROPPINGOFF'}).$promise.then(function(response) {
                vm.assignedRideRequest = response;
                vm.assignedRideRequest.forEach(function(element, index, assignedRideRequest) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequest[index] = rideRequest;
                    if(rideRequest.status === 'DROPPINGOFF'){
                      vm.assignedRide = rideRequest;
                      vm.isRideAssigned = true;
                      vm.isStartOdoEntered = true;
                      vm.pickedUpButtonPressed = true;//different from PICKINGUP
                      vm.inprogressFlag = true;
                      console.log("got Assigned Ride, DROPPINGOFF");
                    }
                });
                  console.log('getCurrentRideRequest() returned DROPPINGOFF:', response);
              }, function(error) {
                  console.log('error getCurrentRideRequest() DROPPINGOFF', error);
              });//end CurrentDriverRidesService.get DROPPINGOFF

            }//if vm.isRideAssigned === false

        }; //end getCurrentRideRequest()

        function buildDirectionButtons(){
          if(vm.assignedRide.pickupLine1 !== undefined){
              vm.pickupAddress = "https://www.google.com/maps/place/"+vm.assignedRide.pickupLine1
              +", "+vm.assignedRide.pickupCity+ ", CA";
              if(vm.assignedRide.pickupLine2 !== undefined){
                vm.pickupAddress += ", "+vm.assignedRide.pickupLine2
              }
          }
          if(vm.assignedRide.dropoffLine1 !== undefined){
              vm.dropoffAddress = "https://www.google.com/maps/place/"+vm.assignedRide.dropoffLine1
              +", "+vm.assignedRide.dropoffCity+ ", CA";
              console.log("buttons built");
              if(vm.assignedRide.dropoffLine2 !== undefined){
                vm.dropoffAddress += ", "+vm.assignedRide.dropoffLine2
              }
          }
        };

        function updateRideRequest(){
          RideRequestService.update(vm.assignedRide).$promise.then(function(response) {
              console.log('Driver, saved riderequest:', response);
          }, function(error) {
              console.log('Driver, error saving riderequest:', error);
          });
        };

        getCurrentRideRequest();



      //enter odometer and view newly assigned ride
      vm.viewRide = function() {
          if(vm.startOdo !== undefined){
            buildDirectionButtons();
            vm.assignedRide.startOdometer = vm.startOdo;
            vm.assignedRide.status = 'PICKINGUP';
//            vm.assignedRide.status = 'INPROGRESS';
            vm.inprogressFlag = true;
            vm.isRideAssigned = true;
            vm.isStartOdoEntered = true;
            vm.pickedUpButtonPressed = false;

            updateRideRequest();

            //TODO notify rider, Ride on the way

            }
      };

      vm.pickedUp = function(){
        vm.assignedRide.status = 'DROPPINGOFF';
//        vm.assignedRide.status = 'INPROGRESS';
        vm.pickedUpButtonPressed = true;
        vm.inprogressFlag = true;
        updateRideRequest();
      };

      vm.endRide = function(){
        vm.assignedRide.status = 'COMPLETE';
        vm.assignedRide.endOdometer = vm.endOdo;
        vm.isStartOdoEntered = false;
        vm.isRideAssigned = false;
        vm.pickedUpButtonPressed = false;
        vm.inprogressFlag = false;
        updateRideRequest();

        //TODO Delay for certain time
        //then refresh getCurrentRideRequest()
        //getCurrentRideRequest();
      };

      vm.refresh = function(){
        getCurrentRideRequest();
      };


    });//end Controller
