'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AssignDriverModalCtrl
 * @description
 * # AssignDriverModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AssignDriverModalCtrl', function ($uibModalInstance, RideRequestService, request, drivers, Notification, $log) {
        var vm = this;
        vm.request = request;
        vm.drivers = drivers;
        vm.selectedDriver = undefined;
        vm.messageToDriver = undefined;
        vm.textareaMinLength = 5;
        vm.estimateTimes = ['15', '30', '45', '1 hour', '> 1 hour'];
        vm.estimatedTime = undefined;


        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            vm.request.estimatedTime = vm.estimatedTime;
            vm.request.messageToDriver = vm.messageToDriver;
            vm.request.driverId = vm.selectedDriver.id;
            vm.request.status = 'ASSIGNED';
            RideRequestService.update({
                id: vm.request.id
            }, vm.request).$promise.then(function (response) {
                $log.debug('assigned driver to request:', response);
                $uibModalInstance.close();
            }, function (error) {
                $log.debug('error assigning driver to request:', error);
                Notification.error({
                    message: error.data.message,
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
            });
        };

    });
