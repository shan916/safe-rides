'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ModalDriverNightSummaryCtrl
 * @description
 * # ModalDriverNightSummaryCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ModalDriverNightSummaryCtrl', function ($uibModalInstance, summary, driver) {
        var vm = this;
        vm.driver = driver;
        vm.summary = summary;

        vm.close = function () {
            $uibModalInstance.dismiss('close');
        };
    });
