'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ConfirmDeleteCoordinatorModalCtrl
 * @description
 * # ConfirmDeleteCoordinatorModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ConfirmDeleteCoordinatorModalCtrl', function (coordinator, $uibModalInstance) {
        var vm = this;
        vm.coordinator = coordinator;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            $uibModalInstance.close(coordinator);
        };
    });
