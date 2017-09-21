'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManagedriversCtrl
 * @description
 * # ManagedriversCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManagedriversCtrl', function (DriverService, Driver, $uibModal, authManager, $state, AuthTokenService, Notification, ENV, $log) {
        var vm = this;

        vm.activeDrivers = [];
        vm.inactiveDrivers = [];
        vm.searchString = undefined;

        vm.loadingActiveDrivers = true;
        vm.loadingInactiveDrivers = true;

        /*
         * Kick user out if not authenticated or if not a coordinator
         * */
        if (authManager.isAuthenticated()) {
            if (!AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                Notification.error({
                    message: 'You must be logged in as a coordinator to manage drivers.',
                    positionX: 'center',
                    delay: 10000
                });
                $state.go('/');
                $log.debug('Not a coordinator');
            } else {
                getDrivers();
            }
        } else {
            $state.go('/');
            $log.debug('Not authenticated');
        }

        function getDrivers() {
            vm.loadingActiveDrivers = true;
            vm.loadingInactiveDrivers = true;

            DriverService.query({active: true}).$promise.then(function (response) {
                vm.loadingActiveDrivers = false;
                vm.activeDrivers = response;
                vm.activeDrivers.forEach(function (element, index, activeDrivers) {
                    activeDrivers[index] = new Driver(element);
                });
                $log.debug('got active drivers:', vm.activeDrivers);
            }, function (error) {
                vm.loadingActiveDrivers = false;
                $log.debug('error getting active drivers:', error);
            });

            DriverService.query({active: false}).$promise.then(function (response) {
                vm.loadingInactiveDrivers = false;
                vm.inactiveDrivers = response;
                vm.inactiveDrivers.forEach(function (element, index, inactiveDrivers) {
                    inactiveDrivers[index] = new Driver(element);
                });
                $log.debug('got inactive drivers:', vm.inactiveDrivers);
            }, function (error) {
                vm.loadingInactiveDrivers = false;
                $log.debug('error getting inactive drivers:', error);
            });
        }

        vm.openConfirmDeleteModal = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-delete-driver.html',
                controller: 'ConfirmDeleteDriverModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    driver: function () {
                        return driver;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function (driver) {
                deleteDriver(driver);
            }, function () {
                // cancel clicked
            });
        };

        vm.openConfirmChangeDriverActiveModal = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-change-driver-active.html',
                controller: 'ConfirmChangeDriverActiveModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    driver: function () {
                        return driver;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function () {
                driver.active = !driver.active;
                DriverService.update({id: driver.id}, driver).$promise.then(function (response) {
                    $log.debug('updated driver, now refreshing', response);
                    getDrivers();
                }, function (error) {
                    $log.debug('error updating driver:', error);
                });
            }, function () {
                // cancel clicked
            });
        };

        function deleteDriver(driver) {
            DriverService.remove({
                id: driver.id
            }).$promise.then(function (response) {
                $log.debug('deleted driver:', response);
                getDrivers();
            }, function (error) {
                $log.debug('error deleting driver:', error);
            });
        }

    });
