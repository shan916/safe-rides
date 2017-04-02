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
        vm.textareaMinLength = 5;
        vm.reasonForCancellation = undefined;
        vm.cancelMessage = undefined;
        vm.cancelReasons = ['Rider', 'Coordinator', 'Other'];
        vm.body = 'You selected to cancel <strong>' + $sanitize(request.requestorFirstName) + '\'s</strong> ride request. The request will be removed from the queue and <strong>' + $sanitize(request.requestorFirstName) + '</strong> will be notified that their request is canceled.';
        vm.reasonRequest = '<strong>Reason for ' + $sanitize(request.requestorFirstName) + '\'s ride cancellation?</strong>';
        vm.danger = true;

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };


        vm.ok = function() {
          //RideRequestService.save({id: request.requestorId}, request.reasonForCancellation).$promise.then(function(response){
            vm.request.status = getStatusFromReason();
            //if other set the cancelMessage to what was entered
            if(vm.cancelMessage != undefined){
              vm.request.cancelMessage = vm.cancelMessage;
            }

            request.deleted = true;
            console.log('Request cancelled by: '+vm.reasonForCancellation+'  status: '+vm.request.status);
            RideRequestService.update({id: vm.request.requestorId}, vm.request).$promise.then(function(response){
              console.log('updated reasonForCancellation AND status: ', response);
              $uibModalInstance.close();
            }, function(error){
              console.log('error setting reasonForCancellation AND status: ', error);
            });
        };

        function getStatusFromReason(){
          switch (vm.reasonForCancellation) {
            case 'Rider':
              return 'CANCELEDBYRIDER';
            case 'Coordinator':
              return 'CANCELEDBYCOORDINATOR';
            case 'Other':
              return 'CANCELEDOTHER';
          }
        }

    });
