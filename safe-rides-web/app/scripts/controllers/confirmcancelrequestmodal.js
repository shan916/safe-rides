'use strict';

/**
* @ngdoc function
* @name safeRidesWebApp.controller:ConfirmCancelRequestModalCtrl
* @description
* # ConfirmCancelRequestModalCtrl
* Controller of the safeRidesWebApp
*/
angular.module('safeRidesWebApp')
.controller('ConfirmCancelRequestModalCtrl', function($uibModalInstance, request, $sanitize) {
    var vm = this;
    vm.body = 'You selected to cancel <strong>' + $sanitize(request.requestorFirstName) + '\'s</strong> ride request. Are you sure you want to continue? The request will be remove from the queue and <strong>' + $sanitize(request.requestorFirstName) + '</strong> will be notified that their request is canceled.';
    vm.danger = true;

    vm.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
        request.deleted = true;
        $uibModalInstance.close();
    };
});
