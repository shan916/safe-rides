'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:DriverdashboardCtrl
 * @description
 * # DriverdashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('DriverdashboardCtrl', function($scope, DriverRidesService, RideRequest) {
        var vm = this;
        vm.loadingRideRequest = true;
        vm.ride = undefined;
        vm.rideRequests = [];
        //vm.driver = driver;

        function getRideRequests() {
        vm.loadingRideRequests = true;
        DriverRidesService.get({
            id: '1'
        }).$promise.then(function(response) {
            vm.rideRequests = response;

            vm.rideRequests.forEach(function(element, index, rideRequests) {
                var rideRequest = new RideRequest(element);
                rideRequests[index] = rideRequest;
            });


            vm.loadingRideRequests = false;

            console.log('got driver-ride request:', response);
        }, function(error) {
            vm.loadingRideRequests = false;
            console.log('error getting-ride request:', error);
        });
      }
      getRideRequests();
      

      /*vm.ride = function(){
        for(var i = 0; i<vm.rideRequests.length;i++){
          if(vm.rideRequests[i].driver.csusId === vm.driver.csusId)
            vm.ride = vm.rideRequests[i];
        }
      } */
      //vm.ride = vm.rideRequests[0];


        // TODO: Move this to an environment file
        //$scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE";
    });
