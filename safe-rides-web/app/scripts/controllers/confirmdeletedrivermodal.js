'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ConfirmDeleteDriverModalCtrl
 * @description
 * # ConfirmDeleteDriverModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ConfirmDeleteDriverModalCtrl', function (driver, $uibModalInstance) {
        var vm = this;
        vm.driver = driver;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            $uibModalInstance.close(driver);
        };
    });
