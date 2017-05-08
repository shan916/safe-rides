'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:EditCoordinatorCtrl
 * @description
 * # EditCoordinatorCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('EditCoordinatorCtrl', function($stateParams, $state, UserService, User) {
        var vm = this;

        vm.coordinator = new User();
        vm.loading = false;

        if ($stateParams.username) {
            getCoordinator($stateParams.username);
        }

        function getCoordinator(username) {
            vm.loading = true;
            UserService.get({
                username: username
            }).$promise.then(function (response) {
                vm.loading = false;
                vm.coordinator = new User(response);
                console.log('got coordinator:', response);
            }, function (error) {
                vm.loading = false;
                console.log('error getting coordinator:', error);
            });
        }

        function updateCoordinator() {
            UserService.update({
                username: vm.coordinator.username
            }, vm.coordinator).$promise.then(function (response) {
                console.log('updated coordinator:', response);
                $state.go('manageCoordinators');
            }, function (error) {
                console.log('error updating coordinator:', error);
            });
        }

        vm.saveCoordinator = function () {
            if ($stateParams.username) {
                updateCoordinator();
            } else {
                vm.coordinator.active = true;
                vm.coordinator.authorities = ['ROLE_COORDINATOR', 'ROLE_DRIVER', 'ROLE_RIDER'];

                UserService.save(vm.coordinator).$promise.then(function (response) {
                    console.log('saved coordinator:', response);
                    $state.go('manageCoordinators');
                }, function (error) {
                    console.log('error saving coordinator:', error);
                });
            }
        };

    });
