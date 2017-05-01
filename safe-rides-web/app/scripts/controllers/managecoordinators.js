'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ManageCoordinatorsCtrl
 * @description
 * # ManageCoordinatorsCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('ManageCoordinatorsCtrl', function(user-service, $uibModal, authManager, $state, AuthTokenService) {
      // kick user out if not authenticated
      if(!authManager.isAuthenticated()){
          $state.go('login');
          console.log('Not authenticated');
          return;
      }

      // kick user out if not admin
      if(!AuthTokenService.isInRole('ROLE_ADMIN')){
          $state.go('/');
          console.log('Not an admin');
          return;
      }

        var vm = this;

        vm.activeCoordinators = [];
        vm.inactiveCoordinators = [];
        vm.searchString = undefined;

        vm.loadingActiveCoordinators = true;
        vm.loadingInactiveCoordinators = true;

        function getCoordinators() {
            vm.loadingActiveCoordinators = true;
            vm.loadingInactiveCoordinators = true;

            user-service.query({enabled: true}).$promise.then(function(response) {
                vm.loadingActiveCoordinators = false;
                vm.activeCoordinators = response;
                console.log('got active Coordinators:', response);
            }, function(error) {
                vm.loadingActiveCoordinators = false;
                console.log('error getting active coordinators:', error);
            });

            user-service.query({enabled: false}).$promise.then(function(response) {
                vm.loadingInactiveCoordinators = false;
                vm.inactiveCoordinators = response;
                console.log('got inactive coordinators:', response);
            }, function(error) {
                vm.loadingInactiveCoordinators = false;
                console.log('error getting inactive coordinators:', error);
            });
        }

        getCoordinators();

        vm.openConfirmDeleteModal = function(user) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmdeletecoordinatormodal.html',
                controller: 'ConfirmDeleteCoordinatorModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function() {
                        return user;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function(user) {
                deleteCoordinator(user);
            }, function() {
                // cancel clicked
            });
        };

        vm.openConfirmChangeCoordinatorActiveModal = function(user) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmchangecoordinatoractivemodal.html',
                controller: 'ConfirmChangeCoordinatorActiveModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    coordinator: function() {
                        return user;
                    }
                },
                size: 'md'
            });

            modalInstance.result.then(function() {
                user.enabled = !user.enabled;
                user-service.update({id: user.id}, user).$promise.then(function(response) {
                    console.log('updated coordinator, now refreshing', response);
                    getCoordinators();
                }, function(error) {
                    console.log('error updating coordinator:', error);
                });
            }, function() {
                // cancel clicked
            });
        };

        function deleteCoordinator(user) {
            console.log(user);
            user-service.remove({
                id: coordinator.id
            }).$promise.then(function(response) {
                console.log('deleted coordinator:', response);
                getCoordinators();
            }, function(error) {
                console.log('error deleting Coordinator:', error);
            });
        }

    });
