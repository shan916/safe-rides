'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ConfirmCancelRequestModalCtrl
 * @description
 * # ConfirmCancelRequestModalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ConfirmCancelRequestModalCtrl', function($uibModalInstance, request, drivers, $sanitize, RideRequestService) {
        var vm = this;
        vm.drivers = drivers;
        vm.request = request;
        vm.reasonForCancellation = undefined;
        vm.cancelReasons = ['Rider', 'Driver', 'Coordinator', 'Other'];
        vm.body = 'You selected to cancel <strong>' + $sanitize(request.requestorFirstName) + '\'s</strong> ride request. The request will be remove from the queue and <strong>' + $sanitize(request.requestorFirstName) + '</strong> will be notified that their request is canceled.';
        vm.reasonRequest = 'Reason for <strong>' + $sanitize(request.requestorFirstName) + '\'s</strong> ride cancellation?';
        vm.danger = true;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };


        vm.ok = function() {
          //RideRequestService.save({id: request.requestorId}, request.reasonForCancellation).$promise.then(function(response){
            vm.request.status = getStatusFromReason();
            request.reasonForCancellation = vm.reasonForCancellation;
            request.deleted = true;
            console.log('Request cancelled by: '+vm.reasonForCancellation+'  status: '+vm.request.status);
            RideRequestService.update({id: vm.request.requestorId}, vm.request).$promise.then(function(response){
              console.log('updated reasonForCancellation AND status: ', response);
            }, function(error){
              console.log('error setting reasonForCancellation AND status: ', error);
            });
            $uibModalInstance.close();
        };

        function getStatusFromReason(){
          switch (vm.reasonForCancellation) {
            case 'Rider':
              return 'CANCELEDBYREQUESTOR';
            case 'Coordinator':
              return 'CANCELEDBYCOORDINATOR';
            case 'Driver':
              return 'CANCELEDBYDRIVER';
            case 'Other':
              return 'CANCELEDOTHER';
          }
        }

    });
