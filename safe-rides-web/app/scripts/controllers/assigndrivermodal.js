'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AssignDriverModalCtrl
 * @description
 * # AssignDriverModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AssignDriverModalCtrl', function($uibModalInstance, request, drivers) {
        var vm = this;
        vm.request = request;
        vm.drivers = drivers;
        vm.selectedDriver = undefined;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function() {
            $uibModalInstance.close();
        };

    });
