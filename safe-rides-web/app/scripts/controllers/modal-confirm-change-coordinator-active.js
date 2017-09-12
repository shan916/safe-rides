'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ConfirmChangeCoordinatorActiveModalCtrl
 * @description
 * # ConfirmChangeCoordinatorActiveModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ConfirmChangeCoordinatorActiveModalCtrl', function (coordinator, $uibModalInstance) {

        var vm = this;
        vm.coordinator = coordinator;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            $uibModalInstance.close();
        };

    });
