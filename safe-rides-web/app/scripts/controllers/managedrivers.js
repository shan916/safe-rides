'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManagedriversCtrl
 * @description
 * # ManagedriversCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManagedriversCtrl', function(DriverService, $uibModal) {
        var vm = this;

        vm.drivers = [];

        vm.searchString = undefined;

        function getDrivers() {
            DriverService.query().$promise.then(function(response) {
                vm.drivers = response;
                console.log('got drivers:', response);
            }, function(error) {
                console.log('error getting drivers:', error);
            });
        }

        getDrivers();

        vm.openConfirmDeleteModal = function(driver) {
            var modalInstance = $uibModal.open({
                template: '<div class="modal-header">' +
                    '<h3 class="modal-title" id="modal-title">Confirm Delete</h3>' +
                    '</div>' +
                    '<div class="modal-body" id="modal-body">' +
                    '<p>Are you sure you want to delete the following driver?</p>' +
                    '<strong>{{ctrl.driver.driverFirstName}}&nbsp;{{ctrl.driver.driverLastName}}</strong>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                    '<button type="button" class="btn btn-danger" ng-click="ctrl.ok()">OK</button>' +
                    '<button type="button" class="btn btn-default" ng-click="ctrl.cancel()">Cancel</button>' +
                    '</div>',
                controller: ['driver', '$uibModalInstance', function(driver, $uibModalInstance) {
                    var vm = this;
                    vm.driver = driver;

                    vm.cancel = function() {
                        $uibModalInstance.dismiss('cancel');
                    };

                    vm.ok = function() {
                        $uibModalInstance.close(driver);
                    };
                }],
                controllerAs: 'ctrl',
                resolve: {
                    driver: function() {
                        return driver;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function(driver) {
                deleteDriver(driver);
            }, function() {
                // cancel clicked
            });
        };

        vm.openConfirmChangeActiveModal = function(driver) {
            var modalInstance = $uibModal.open({
                template: '<div class="modal-header">' +
                    '<h3 class="modal-title" id="modal-title">Confirm <span ng-show="!ctrl.driver.active">Activate</span><span ng-show="ctrl.driver.active">Deactivate</span></h3>' +
                    '</div>' +
                    '<div class="modal-body" id="modal-body">' +
                    '<p>Are you sure you want to <span ng-show="!ctrl.driver.active">activate</span><span ng-show="ctrl.driver.active">deactivate</span> the following driver?</p>' +
                    '<strong>{{ctrl.driver.driverFirstName}}&nbsp;{{ctrl.driver.driverLastName}}</strong>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                    '<button type="button" class="btn btn-danger" ng-click="ctrl.ok()">OK</button>' +
                    '<button type="button" class="btn btn-default" ng-click="ctrl.cancel()">Cancel</button>' +
                    '</div>',
                controller: ['driver', '$uibModalInstance', function(driver, $uibModalInstance) {
                    var vm = this;
                    vm.driver = driver;

                    vm.cancel = function() {
                        $uibModalInstance.dismiss('cancel');
                    };

                    vm.ok = function() {
                        vm.driver.active = !vm.driver.active;
                        DriverService.update({id: driver.id}, driver).$promise.then(function(response) {
                            console.log('updated driver:', response);
                            $uibModalInstance.close();
                        }, function(error) {
                            console.log('error updating driver:', error);
                        });
                    };
                }],
                controllerAs: 'ctrl',
                resolve: {
                    driver: function() {
                        return driver;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function(driver) {
                console.log('ok clicked, refreshing drivers');
                getDrivers();
            }, function() {
                // cancel clicked
            });
        };

        function deleteDriver(driver) {
            console.log(driver);
            DriverService.remove({
                id: driver.id
            }).$promise.then(function(response) {
                console.log('deleted driver:', response);
                getDrivers();
            }, function(error) {
                console.log('error deleting driver:', error);
            });
        }

    });
