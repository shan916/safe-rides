'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:EditcoordinatorCtrl
 * @description
 * # EditcoordinatorCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('EditcoordinatorCtrl', function($stateParams, $location, user-service, Coordinator) {
        var vm = this;

        vm.coordinator = new Coordinator();

        vm.NUM_REGEX = '\\d+';

        vm.yearChoices = [];

        vm.loading = false;


        function getCoordinator(coordinatorId) {
            vm.loading = true;
            user-service.get({
                id: coordinatorId
            }).$promise.then(function(response) {
                vm.loading = false;
                vm.coordinator = response;
                console.log('got coordinator:', response);
            }, function(error) {
                vm.loading = false;
                console.log('error getting coordinator:', error);
            });
        }

        function updateCoordinator() {
            user-service.update({
                id: vm.coordinator.id
            }, vm.coordinator).$promise.then(function(response) {
                console.log('updated coordinator:', response);
                $location.path('/managecoordinators');
            }, function(error) {
                console.log('error updating coordinator:', error);
            });
        }

        if ($stateParams.coordinatorId) {
            getCoordinator($stateParams.coordinatorId);
        }

        for (var year = new Date().getFullYear() + 1; year >= 1980; year--) {
            vm.yearChoices.push(year);
        }

        vm.saveCoordinator = function() {
            if ($stateParams.coordinatorId) {
                updateCoordinator();
            } else {
                user-service.save(vm.coordinator).$promise.then(function(response) {
                    console.log('saved coordinator:', response);
                    $location.path('/managecoordinators');
                }, function(error) {
                    console.log('error saving coordinator:', error);
                });
            }
        };

    });
