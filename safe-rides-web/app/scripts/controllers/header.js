'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:HeaderCtrl
 * @description
 * # HeaderCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('HeaderCtrl', function () {
        var vm = this;
        vm.routes = [{
            text: 'Home',
            path: '#!/'
        },
            // { text: 'Login', path: '#!/login '},
            {
                text: 'Coordinator Dashboard',
                path: '#!/coordinatordashboard'
            },
            // { text: 'Driver Dashboard', path: '#!/driverdashboard' },
            // { text: 'Rider Dashboard', path: '#!/riderdashboard' },
            {
                text: 'Manage Drivers',
                path: '#!/managedrivers'
            }
        ];

        vm.activeRoute = vm.routes[0];

        vm.setActive = function (route) {
            vm.activeRoute = route;
        };

    });
