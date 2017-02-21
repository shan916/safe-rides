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
      {pickup-line1: undefined},
      {pickup-line2: undefined},
      {pickup-city: undefined},
      {pickup-zip: undefined},
      {dropOff-line1: undefined},
      {dropOff-line2: undefined},
      {dropOff-city: undefined},
      {dropOff-zip: undefined},
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
