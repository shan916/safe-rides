'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManageCoordinatorsCtrl
 * @description
 * # ManageCoordinatorsCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManageCoordinatorsCtrl', function(CoordinatorService, $uibModal) {
        var vm = this;

        vm.activeCoordinators = [];
        vm.inactiveCoordinators = [];
        vm.searchString = undefined;

        vm.loadingActiveCoordinators = true;
        vm.loadingInactiveCoordinators = true;

        function getCoordinators() {
            vm.loadingActiveCoordinators = true;
            vm.loadingInactiveCoordinators = true;

            CoordinatorService.query().$promise.then(function(response) {
                vm.loadingActiveCoordinators = false;
                vm.activeCoordinators = response;
                console.log('got active Coordinators:', response);
            }, function(error) {
                vm.loadingActiveCoordinators = false;
                console.log('error getting active coordinators:', error);
            });

            CoordinatorService.query().$promise.then(function(response) {
                vm.loadingInactiveCoordinators = false;
                vm.inactiveCoordinators = response;
                console.log('got inactive coordinators:', response);
            }, function(error) {
                vm.loadingInactiveCoordinators = false;
                console.log('error getting inactive coordinators:', error);
            });
        }

        getCoordinators();

        vm.openConfirmDeleteModal = function(coordinator) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmdeletecoordinatormodal.html',
                controller: 'ConfirmDeleteCoordinatorModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function() {
                        return coordinator;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function(coordinator) {
                deleteCoordinator(coordinator);
            }, function() {
                // cancel clicked
            });
        };

        vm.openConfirmChangeCoordinatorActiveModal = function(coordinator) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmchangecoordinatoractivemodal.html',
                controller: 'ConfirmChangeCoordinatorActiveModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function() {
                        return coordinator;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function() {
                coordinator.active = !coordinator.active;
                CoordinatorService.update({id: coordinator.id}, coordinator).$promise.then(function(response) {
                    console.log('updated coordinator, now refreshing', response);
                    getCoordinators();
                }, function(error) {
                    console.log('error updating coordinator:', error);
                });
            }, function() {
                // cancel clicked
            });
        };

        function deleteCoordinator(coordinator) {
            console.log(coordinator);
            CoordinatorService.remove({
                id: coordinator.id
            }).$promise.then(function(response) {
                console.log('deleted coordinator:', response);
                getCoordinators();
            }, function(error) {
                console.log('error deleting Coordinator:', error);
            });
        }

    });
