'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:AssignRequestModalCtrl
* @description
* # AssignRequestModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('AssignRequestModalCtrl', function(RideRequestService, $uibModalInstance, driver, requests, $http) {
    var vm = this;
    vm.driver = driver;
    vm.requests = requests;
    vm.selectedRequest = null;
    vm.selectedRequestID = null;

    vm.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
          $uibModalInstance.close();
      };

    vm.changed = function() {
        getRideRequest(vm.selectedRequestID);
    };

    function getRideRequest(requestId) {
        RideRequestService.get({id: requestId}).$promise.then(function(response) {
            vm.selectedRequest = response;
            console.log('got ride request:', response);
        }, function(error) {
            console.log('error getting ride request:', error);
        });
    }
});
