'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('LoginCtrl', function ($http, $window, $cookies, $rootScope, $state, UserService, authManager, AuthTokenService) {
        if (authManager.isAuthenticated()) {
            $state.go('/');
            return;
        }

        var vm = this;
        vm.loading = false;
        vm.username = undefined;
        vm.password = undefined;
        vm.message = undefined;

        vm.login = function () {
            vm.loading = true;

            var credentials = {
                username: vm.username,
                password: vm.password
            };

            UserService.userAuthentication(credentials).then(function (response) {
                    vm.loading = false;
                    console.log(response.data.token);
                    AuthTokenService.setToken(response.data.token);

                    if ($rootScope.redirect) {
                        $state.go($rootScope.redirect);
                    } else {
                        $state.go('/');
                    }
                },
                function (response) {
                    vm.loading = false;
                    console.log('login error: ', response.data);
                    if (response.status === 422 && response.data.message === 'Bad credentials') {
                        vm.message = 'Bad credentials';
                    } else if (response.status === 422 && response.data.message === 'Account is deactivated')
                        vm.message = 'Account is deactivated';
                });
        };
    });
