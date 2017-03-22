'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('LoginCtrl', ['$http', function($http, ENV) {
            var vm = this;
            vm.login = function() {
                var credentials = {
                    username: vm.username,
                    password: vm.password
                };
                $http({
                        method: 'POST',
                        url: ENV.apiEndpoint + "/users/auth",
                        data: credentials,
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .success(function(data, status, headers, config) {
                        console.log(data);
                    });
            };
        }]);
