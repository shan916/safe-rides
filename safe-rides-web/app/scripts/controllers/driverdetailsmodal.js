'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:DriverDetailsModalCtrl
 * @description
 * # DriverDetailsModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('DriverDetailsModalCtrl', function($uibModalInstance, driver) {
        var vm = this;
        vm.driver = driver;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function() {
            $uibModalInstance.close();
        };
    });
