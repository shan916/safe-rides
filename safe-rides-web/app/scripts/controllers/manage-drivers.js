'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManagedriversCtrl
 * @description
 * # ManagedriversCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManagedriversCtrl', function (DriverService, $uibModal, authManager, $state, AuthTokenService, Notification, ENV, $window) {
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
                console.log('Not a coordinator');
            } else {
                getDrivers();
            }
        } else {
            $window.location.href = ENV.casLogin + '?service=' + ENV.casServiceName;
            console.log('Not authenticated');
        }

        function getDrivers() {
            vm.loadingActiveDrivers = true;
            vm.loadingInactiveDrivers = true;

            DriverService.query({active: true}).$promise.then(function (response) {
                vm.loadingActiveDrivers = false;
                vm.activeDrivers = response;
                console.log('got active drivers:', response);
            }, function (error) {
                vm.loadingActiveDrivers = false;
                console.log('error getting active drivers:', error);
            });

            DriverService.query({active: false}).$promise.then(function (response) {
                vm.loadingInactiveDrivers = false;
                vm.inactiveDrivers = response;
                console.log('got inactive drivers:', response);
            }, function (error) {
                vm.loadingInactiveDrivers = false;
                console.log('error getting inactive drivers:', error);
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
                    console.log('updated driver, now refreshing', response);
                    getDrivers();
                }, function (error) {
                    console.log('error updating driver:', error);
                });
            }, function () {
                // cancel clicked
            });
        };

        function deleteDriver(driver) {
            DriverService.remove({
                id: driver.id
            }).$promise.then(function (response) {
                console.log('deleted driver:', response);
                getDrivers();
            }, function (error) {
                console.log('error deleting driver:', error);
            });
        }

    });
