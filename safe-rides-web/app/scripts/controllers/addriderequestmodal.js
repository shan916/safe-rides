'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AddriderequestmodalCtrl
 * @description
 * # AddriderequestmodalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
  .controller('AddriderequestmodalCtrl', function ($uibModalInstance, RideRequest, RideRequestService, Rider) {
    var vm = this;
    vm.riderequest = new RideRequest();
    vm.rider = new Rider();
    vm.NUM_REGEX = '\\d+';
    vm.PHONE_REGEX = '/^\d{0,9}(\.\d{1,9})?$/';
    vm.maxPeopleCount = [1, 2, 3, 4, 5, 6, 7, 8];

    vm.cancel = function(){
        $uibModalInstance.dismiss('cancel');
    };

    vm.saveRideRequest = function(){
      //TODO if the ride request exists already?
      //if($routeParams.csusid)
      RideRequestService.save(vm.rider, vm.riderequest).$promise.then(function(response){

      },function(error){

      });
//      vm.riderequest.csusid = element(by.binding('csusId'));
    };//end vm.saveRideRequest

  });
