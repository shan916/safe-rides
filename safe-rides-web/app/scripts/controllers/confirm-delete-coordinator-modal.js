'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:ConfirmDeleteCoordinatorModalCtrl
* @description
* # ConfirmDeleteCoordinatorModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('ConfirmDeleteCoordinatorModalCtrl', function (user, $uibModalInstance) {
    var vm = this;
    vm.user = user;

    vm.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
      $uibModalInstance.close(user);
    };
});
