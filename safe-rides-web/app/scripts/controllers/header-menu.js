'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:HeaderMenuCtrl
 * @description
 * # HeaderMenuCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('HeaderMenuCtrl', function (authManager, AuthService, AuthTokenService) {
        var vm = this;
        vm.routes = [];

        if (authManager.isAuthenticated()) {
            if (AuthTokenService.isInRole('ROLE_DRIVER') && !AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                vm.routes.push({
                    text: 'Dashboard',
                    path: '#!/driver'
                });
                vm.routes.push({
                    text: 'Request Ride',
                    path: '#!/request'
                });
            }

            if (AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                vm.routes.push({
                    text: 'Dashboard',
                    path: '#!/coordinator'
                });
                vm.routes.push({
                    text: 'Manage Drivers',
                    path: '#!/manage-drivers'
                });
                vm.routes.push({
                    text: 'Settings',
                    path: '#!/settings'
                });
                vm.routes.push({
                    text: 'Reports',
                    path: '#!/reports'
                });
            }

            if (AuthTokenService.isInRole('ROLE_ADMIN')) {
                vm.routes.push({
                    text: 'Manage Coordinators',
                    path: '#!/manage-coordinators'
                });
            }
        }
    });
