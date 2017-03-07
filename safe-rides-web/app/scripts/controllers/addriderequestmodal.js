'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AddriderequestmodalCtrl
 * @description
 * # AddriderequestmodalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
  .controller('AddriderequestmodalCtrl', function ($uibModalInstance, $routeParams, RideRequest, RideRequestService) {
    var vm = this;
    vm.riderequest = new RideRequest();
    vm.NUM_REGEX = '\\d+';
    vm.PHONE_REGEX = '/^[2-9]\d{2}-\d{3}-\d{4}$/';
    vm.maxPeopleCount = [1, 2, 3, 4, 5, 6, 7, 8];

    vm.cancel = function(){
        $uibModalInstance.dismiss('cancel');
    };


    function updateRideRequest() {
        RideRequestService.update({id: vm.riderequest.id}, vm.riderequest).$promise.then(function(response) {
            console.log('updated riderequest:', response);
            $uibModalInstance.dismiss();
        }, function(error) {
            console.log('error updating riderequest:', error);
        });
    }

    if ($routeParams.requestId) {
        getRideRequest($routeParams.requestId);
    }

    function getRideRequest(requestId) {
        RideRequestService.get({id: requestId}).$promise.then(function(response) {
            vm.riderequest = response;
            console.log('got riderequest:', response);
        }, function(error) {
            console.log('error getting riderequest:', error);
        });
    }

    vm.saveRideRequest = function(){
      //TODO if the ride request exists already?
      //if($routeParams.csusid)
      RideRequestService.save(vm.riderequest).$promise.then(function(response){
        console.log('saved riderequest:', response);
      $uibModalInstance.dismiss();
      },function(error){
        console.log('error saving riderequest:', error);
      });
    };//end vm.saveRideRequest

  });
