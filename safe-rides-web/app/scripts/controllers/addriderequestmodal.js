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
    vm.riderequest = [
      {csusid: undefined},
      {phone: undefined},
      {fname: undefined},
      {lname: undefined},
      {pickupline1: undefined},
      {pickupline2: undefined},
      {pickupcity: undefined},
      {pickupzip: undefined},
      {dropOffline1: undefined},
      {dropOffline2: undefined},
      {dropOffcity: undefined},
      {dropOffzip: undefined},
      {peopleCount: undefined},
      {msg: undefined}
    ];
    vm.cancel = function(){
        $uibModalInstance.dismiss('cancel');
    };

    vm.saveRideRequest = function(){
      //TODO add the rest of the captures
      vm.riderequest.csusid = element(by.binding('csusId'));
    };

  });
