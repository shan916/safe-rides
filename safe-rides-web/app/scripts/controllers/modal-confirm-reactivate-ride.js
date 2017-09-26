'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ModalConfirmReactivateRideCtrl
 * @description
 * # ModalConfirmReactivateRideCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ModalConfirmReactivateRideCtrl', function ($uibModalInstance, ride, RideRequestService) {
        var vm = this;
        vm.ride = ride;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            $uibModalInstance.close(ride);
        };
    });
