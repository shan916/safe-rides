'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:ManagedriversCtrl
* @description
* # ManagedriversCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('ManagedriversCtrl', function (DriverService, $uibModal) {
    var vm = this;

    vm.drivers = DriverService.getDrivers();

    vm.openConfirmModal = function(driver) {
        var modalInstance = $uibModal.open({
            template: '<div class="modal-header">' +
            '<h3 class="modal-title" id="modal-title">Confirm Action</h3>' +
            '</div>' +
            '<div class="modal-body" id="modal-body">' +
            '<p>Are you sure you want to delete the following driver?</p>' +
            '<strong>{{ctrl.driver.name}}</strong>' +
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

    function deleteDriver(driver) {
        DriverService.deleteDriver(driver);
    }
});
