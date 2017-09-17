'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AssignRequestModalCtrl
 * @description
 * # AssignRequestModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AssignRequestModalCtrl', function (RideRequestService, AssignRideService, $uibModalInstance, driver, requests, $log) {
        var vm = this;
        vm.driver = driver;
        vm.requests = requests;
        vm.selectedRequest = undefined;
        vm.messageToDriver = undefined;
        vm.textareaMinLength = 5;
        vm.estimateTimes = ['15', '30', '45', '1 hour', '> 1 hour'];
        vm.estimatedTime = undefined;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function () {
            vm.selectedRequest.messageToDriver = vm.messageToDriver;
            vm.selectedRequest.estimatedTime = vm.estimatedTime;
            AssignRideService.save({
                id: vm.driver.id
            }, vm.selectedRequest).$promise.then(function (response) {
                $log.debug(vm.driver.driverFirstName + ' was assigned to ' + vm.selectedRequest.requestorFirstName +
                    ' with message ' + vm.selectedRequest.messageToDriver);
                $log.debug('asssigned request to driver:', response);
                $uibModalInstance.close();
            }, function (error) {
                $log.debug('error assigning request to driver:', error);
            });
        };

    });
