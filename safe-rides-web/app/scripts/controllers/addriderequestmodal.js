'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AddriderequestmodalCtrl
 * @description
 * # AddriderequestmodalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
  .controller('AddriderequestmodalCtrl', function ($uibModalInstance) {
    var vm = this;
    vm.riderequest = {
      csusid: undefined,
      phone: undefined,
      firstname: undefined,
      lastname: undefined,
      pickupLine1: undefined,
      pickupLine2: undefined,
      pickupCity: undefined,
      pickupZip: undefined,
      dropOffLine1: undefined,
      dropOffLine2: undefined,
      dropOffCity: undefined,
      dropOffZip: undefined,
      peopleCount: undefined,
      msg: undefined
    };

    vm.cancel = function(){
        $uibModalInstance.dismiss('cancel');
    };

    vm.saveRideRequest = function(){
      //TODO add the rest of the captures
      vm.riderequest.csusid = element(by.binding('csusId'));
    };

  });
