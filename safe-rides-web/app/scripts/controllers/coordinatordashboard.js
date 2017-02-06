'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function ($scope, $interval) {
        this.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma',
        ];
        // TODO: Move this to an environment file
        $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE";
        var vm = this;
        vm.positions = [
            [38.55, -121.45], [38.54, -121.44], [38.53, -121.43], [38.52, -121.42]
        ];
        $interval(function () {
            var numMarkers = 4;
            vm.positions = [];
            for (var i = 0; i < numMarkers; i++) {
                var lat = 38.55 + (Math.random() / 100);
                var lng = -121.45 + (Math.random() / 100);
                vm.positions.push([lat, lng]);
            }
        }, 2000);
    });
