'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:RequestDetailModalCtrl
* @description
* # RequestDetailModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('RequestDetailModalCtrl', function($uibModalInstance, request) {
    var vm = this;
    vm.request = request;

    vm.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
        $uibModalInstance.close();
    };
});
