'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManageCoordinatorsCtrl
 * @description
 * # ManageCoordinatorsCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManageCoordinatorsCtrl', function (authManager, $state, UserService, Notification, AuthTokenService, $uibModal, ENV, $window, $log) {
        var vm = this;

        vm.activeCoordinators = [];
        vm.inactiveCoordinators = [];

        vm.loadingActiveCoordinators = true;
        vm.loadingInactiveCoordinators = true;

        /**
         * Kick user out if not authenticated or if not an admin
         **/
        if (authManager.isAuthenticated()) {
            if (!AuthTokenService.isInRole('ROLE_ADMIN')) {
                Notification.error({
                    message: 'You must be logged in as an admin to manage coordinators.',
                    positionX: 'center',
                    delay: 10000
                });
                $state.go('/');
                $log.debug('Not an admin');
            } else {
                getCoordinators();
            }
        } else {
            $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
            $log.debug('Not authenticated');
        }

        function getCoordinators() {
            vm.loadingActiveCoordinators = true;
            vm.loadingInactiveCoordinators = true;

            UserService.query({active: true, role: 'ROLE_COORDINATOR'}).$promise.then(function (response) {
                vm.loadingActiveCoordinators = false;
                vm.activeCoordinators = response;
                $log.debug('got active coordinators:', response);
            }, function (error) {
                vm.loadingActiveCoordinators = false;
                $log.debug('error getting active coordinators:', error);
            });

            UserService.query({active: false, role: 'ROLE_COORDINATOR'}).$promise.then(function (response) {
                vm.loadingInactiveCoordinators = false;
                vm.inactiveCoordinators = response;
                $log.debug('got inactive coordinators:', response);
            }, function (error) {
                vm.loadingInactiveCoordinators = false;
                $log.debug('error getting inactive coordinators:', error);
            });
        }

        vm.openConfirmDeleteModal = function (coordinator) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-delete-coordinator.html',
                controller: 'ConfirmDeleteCoordinatorModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function () {
                        return coordinator;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function (coordinator) {
                deleteCoordinator(coordinator);
            }, function () {
                // cancel clicked
            });
        };

        vm.openConfirmChangeCoordinatorActiveModal = function (coordinator) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-change-coordinator-active.html',
                controller: 'ConfirmChangeCoordinatorActiveModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function () {
                        return coordinator;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function () {
                coordinator.active = !coordinator.active;
                UserService.update({id: coordinator.id}, coordinator).$promise.then(function (response) {
                    $log.debug('updated coordinator, now refreshing', response);
                    getCoordinators();
                }, function (error) {
                    $log.debug('error updating coordinator:', error);
                });
            }, function () {
                // cancel clicked
            });
        };

        function deleteCoordinator(coordinator) {
            UserService.remove({
                id: coordinator.id
            }).$promise.then(function (response) {
                $log.debug('deleted coordinator:', response);
                getCoordinators();
            }, function (error) {
                $log.debug('error deleting coordinator:', error);
            });
        }
    });
