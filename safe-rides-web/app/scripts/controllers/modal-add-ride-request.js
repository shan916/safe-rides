'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:AddriderequestmodalCtrl
 * @description
 * # AddriderequestmodalCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('AddriderequestmodalCtrl', function ($uibModalInstance, $stateParams, RideRequest, RideRequestService, $log) {
        var vm = this;
        vm.riderequest = new RideRequest();
        vm.NUM_REGEX = '\\d+';
        vm.PHONE_REGEX = '/^[2-9]\d{2}-\d{3}-\d{4}$/';
        vm.maxPeopleCount = [1, 2, 3];

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        function getRideRequest(requestId) {
            RideRequestService.get({
                id: requestId
            }).$promise.then(function (response) {
                vm.riderequest = response;
                $log.debug('got riderequest:', response);
            }, function (error) {
                $log.debug('error getting riderequest:', error);
            });
        }

        /*
         function updateRideRequest() {
         RideRequestService.update({
         id: vm.riderequest.id
         }, vm.riderequest).$promise.then(function(response) {

         console.log('updated riderequest:', response);
         $uibModalInstance.dismiss();
         }, function(error) {
         console.log('error updating riderequest:', error);
         });
         }
         */

        if ($stateParams.requestId) {
            getRideRequest($stateParams.requestId);
        }

        vm.saveRideRequest = function () {
            //TODO if the ride request exists already?
            //if($routeParams.oneCardId)
            RideRequestService.save(vm.riderequest).$promise.then(function (response) {
                $log.debug('saved riderequest:', response);
                $uibModalInstance.dismiss();
            }, function (error) {
                $log.debug('error saving riderequest:', error);
            });
        }; //end vm.saveRideRequest

    });
