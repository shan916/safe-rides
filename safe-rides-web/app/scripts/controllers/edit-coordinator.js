'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:EditCoordinatorCtrl
 * @description
 * # EditCoordinatorCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('EditCoordinatorCtrl', function ($stateParams, $state, UserService, User, Notification) {
        var vm = this;

        vm.coordinator = new User();
        vm.loading = false;
        vm.existingUser = !!$stateParams.id;

        vm.USERNAME_REGEX = /^[a-z0-9]+$/i;

        if (vm.existingUser) {
            getCoordinator($stateParams.id);
        }

        function getCoordinator(id) {
            vm.loading = true;
            UserService.get({
                id: id
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
                id: vm.coordinator.id
            }, vm.coordinator).$promise.then(function (response) {
                console.log('updated coordinator:', response);
                $state.go('manageCoordinators');
            }, function (error) {
                Notification.error({
                    message: error.data.message,
                    positionX: 'center',
                    delay: 10000
                });
            });
        }

        vm.saveCoordinator = function () {
            if ($stateParams.id) {
                updateCoordinator();
            } else {
                vm.coordinator.active = true;

                UserService.save(vm.coordinator).$promise.then(function (response) {
                    console.log('saved coordinator:', response);
                    $state.go('manageCoordinators');
                }, function (error) {
                    console.log('error saving coordinator:', error);
                });
            }
        };

    });
