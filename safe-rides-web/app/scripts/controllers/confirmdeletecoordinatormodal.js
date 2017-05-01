'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:ConfirmDeleteDriverModalCtrl
* @description
* # ConfirmDeleteDriverModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('ConfirmDeleteDriverModalCtrl', function (user, $uibModalInstance) {
    var vm = this;
    vm.user = user;

    vm.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
      $uibModalInstance.close(user);
    };
});
