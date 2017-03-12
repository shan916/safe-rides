'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AssignRequestModalCtrl
 * @description
 * # AssignRequestModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AssignRequestModalCtrl', function(RideRequestService, AssignRideService, $uibModalInstance, driver, requests) {
        var vm = this;
        vm.driver = driver;
        vm.requests = requests;
        vm.selectedRequest = undefined;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function() {
            AssignRideService.save({
                id: vm.driver.id
            }, vm.selectedRequest).$promise.then(function(response) {
                console.log('asssigned request to driver:', response);
            }, function(error) {
                console.log('error assigning request to driver:', error);
            });

            $uibModalInstance.close();
        };

    });
