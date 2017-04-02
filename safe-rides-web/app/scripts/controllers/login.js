'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('LoginCtrl', function($http, $window, $cookies, $stateParams, $state, UserService, authManager) {
        if(authManager.isAuthenticated()){
            $state.go('/');
            return;
        }

        var vm = this;
        vm.username = undefined;
        vm.password = undefined;
        vm.message = undefined;

        vm.login = function() {
            var credentials = {
                username: vm.username,
                password: vm.password
            };

            UserService.userAuthentication(credentials).then(function(response) {
                    console.log(response.data.token);
                    var expirationDate = new Date();
                    expirationDate.setTime(expirationDate.getTime() + 6 * 60 * 60 * 1000);
                    $window.localStorage.safeRidesToken = response.data.token;
                    $cookies.put('safeRidesToken', response.data.token, {
                        expires: expirationDate
                    });

                    if ($stateParams.redirect) {
                        $state.go($stateParams.redirect);
                    } else {
                        $state.go('/');
                    }
                },
                function(response) {
                    console.log('login error: ', response.data);
                    if (response.status === 422 && response.data.message === 'Bad credentials') {
                        vm.message = 'Bad credentials';
                    }
                });
        };
    });
