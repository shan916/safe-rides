'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('LoginCtrl', function($http, $window, ENV, $cookies) {
        var vm = this;
        vm.username = undefined;
        vm.password = undefined;
        vm.message = undefined;

        vm.login = function() {
            var credentials = {
                username: vm.username,
                password: vm.password
            };
            $http({
                    method: 'POST',
                    url: ENV.apiEndpoint + "users/auth/",
                    data: credentials,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(function(response) {
                        console.log('SUCCESS');
                        console.log(response.data.token);
                        var expirationDate = new Date();
                        expirationDate.setTime(expirationDate.getTime() + 6 * 60 * 60 * 1000);
                        $window.localStorage.token = response.data.token;
                        $cookies.put("Authorization", response.data.token, {
                            expires: expirationDate
                        });
                    },
                    function(response) {
                        console.log('login error: ', response.data);
                        if (response.status === 422 && response.data.message === 'Bad credentials') {
                            vm.message = 'Bad credentials';
                        }
                    });

        };
    });
