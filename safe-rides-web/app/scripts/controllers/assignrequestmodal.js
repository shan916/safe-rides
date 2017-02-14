'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:AssignRequestModalCtrl
* @description
* # AssignRequestModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('AssignRequestModalCtrl', function($uibModalInstance, driver, requests) {
    var vm = this;
    vm.driver = driver;
    vm.requests = requests;
    vm.selectedRequest = null;
    vm.selectedRequestID = null;
    vm.REQUEST_STATUS = REQUEST_STATUS;

    vm.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
        vm.selectedRequest.status = REQUEST_STATUS.ASSIGNED;
        vm.driver.status = DRIVER_STATUS.DRIVING;
        $uibModalInstance.close();
    };

    vm.changed = function() {
        // assuming id = index :o
        vm.selectedRequest = vm.requests[vm.selectedRequestID];
    };
});
