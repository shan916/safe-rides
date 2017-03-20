'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ConfirmChangeDriverActiveModalCtrl
 * @description
 * # ConfirmChangeDriverActiveModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
  .controller('ConfirmChangeDriverActiveModalCtrl', function (driver, $uibModalInstance) {
      var vm = this;
      vm.driver = driver;

      vm.cancel = function() {
          $uibModalInstance.dismiss('cancel');
      };

      vm.ok = function() {
          $uibModalInstance.close();
      };
  });
