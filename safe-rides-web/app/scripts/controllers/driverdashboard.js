'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:DriverdashboardCtrl
 * @description
 * # DriverdashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('DriverdashboardCtrl', function($scope, DriverRidesService, RideRequest, CurrentDriverRidesService, Driver) {
        var vm = this;
        vm.loadingRideRequest = true;
        vm.ride = undefined;
        vm.rideRequests = [];
        vm.startOdo = undefined;
        vm.assignedRide = undefined;
        vm.isStartOdoEntered = false;
        vm.getPickupDirections = undefined;
        vm.dropoffAddress = undefined;
        //vm.driver = driver;

        // START EXAMPLE - GET LIST OF (ASSIGNED) RIDES

        vm.assignedRideRequests = undefined;

        function getCurrentRideRequests() {
            CurrentDriverRidesService.get({status: 'ASSIGNED'}).$promise.then(function(response) {
                vm.assignedRideRequests = response;

                vm.assignedRideRequests.forEach(function(element, index, assignedRideRequests) {
                    var rideRequest = new RideRequest(element);
                    assignedRideRequests[index] = rideRequest;
                });

                console.log('got currently assigned ride requests:', response);
            }, function(error) {
                console.log('error getting currently assigned ride requests:', error);
            });
        }

        vm.assignedRideRequests = getCurrentRideRequests();

        // END EXAMPLE - GET LIST OF (ASSIGNED) RIDES

        function getRideRequests() {
        vm.loadingRideRequests = true;
        DriverRidesService.get({
            id: '1'
        }).$promise.then(function(response) {
            vm.rideRequests = response;

            vm.rideRequests.forEach(function(element, index, rideRequests) {
                var rideRequest = new RideRequest(element);
                rideRequests[index] = rideRequest;
                if(rideRequests[index].status === 'ASSIGNED'){
                  vm.assignedRide = rideRequests[index];
                  //todo generate addresses
                  if(vm.assignedRide.dropoffLine2 !== undefined){
                    vm.dropoffAddress ="https://www.google.com/maps/place/"+ vm.assignedRide.dropoffLine1
                    +", "+vm.assignedRide.dropoffCity+ ", CA, "
                    +vm.assignedRide.dropoffZip;
                  }else if(vm.assignedRide.dropoffLine1 !== undefined){
                    vm.dropoffAddress = "https://www.google.com/maps/place/"+vm.assignedRide.dropoffLine1
                    +", "+vm.assignedRide.dropoffCity+ ", CA, "
                    +vm.assignedRide.dropoffZip+", "+vm.assignedRide.dropoffLine2;
                  }
                  console.log("for each: added: "+rideRequests[index].status);
                }else{
                  console.log("for each: added NON-ASSIGNED: "+rideRequests[index].status);
                }
            });


            vm.loadingRideRequests = false;

            console.log('got driver-ride request:', response);

        }, function(error) {
            vm.loadingRideRequests = false;
            console.log('error getting-ride request:', error);
        });
      }

      /*function getAssignedRequests(){
        console.log("Getting assinged requests");
        for(var i=0;i<vm.rideRequests.length;i++){
          console.log("in for");
          if(vm.rideRequests[i].status === "ASSIGNED"){
            console.log("ASSIGNED ride Request in vm.assignedRide");
            vm.assignedRide = vm.rideRequests[i];
            //console.log("assignedRide startOdo: "+vm.rideRequests[1].startOdo);
          }
        }
      }*/
      getRideRequests();
      vm.viewRide = function() {
          if(vm.startOdo !== undefined){
                vm.assignedRide.startOdometer = vm.startOdo;
                vm.isStartOdoEntered = true;
                //todo Save the Ride Request -> Accepted by driver
                console.log("got assignedRide");
          }
      };


        // TODO: Move this to an environment file
        //$scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE";





    /*    if(vm.assignedRide === undefined){
          vm.dropoffAddress = vm.assignedRide.dropoffLine1
          +", "+vm.assignedRide.dropoffCity+ ", CA, "
          +vm.assignedRide.dropoffZip;
        }else if(vm.assignedRide.dropoffLine2 !== undefined){
          //add line 2
          vm.dropOffAddress = vm.assignedRide.pickupLine1
          +", "+vm.pickupLine2+", "+vm.assignedRide.pickupCity+ ", CA, "
          +vm.assignedRide.pickupZip;
        }
        */

    });
