'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AssignDriverModalCtrl
 * @description
 * # AssignDriverModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AssignDriverModalCtrl', function($uibModalInstance, request, drivers, AssignRideService) {
        var vm = this;
        vm.request = request;
        vm.drivers = drivers;
        vm.selectedDriver = undefined;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.ok = function() {
            AssignRideService.save({id: vm.selectedDriver.id}, vm.request).$promise.then(function(response) {
                console.log('assigned driver to request:', response);
                $uibModalInstance.close();
            }, function(error) {
                console.log('error assigning driver to request:', error);
            });
        };

    });
