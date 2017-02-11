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
        $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=AIzaSyCDx8ucIftYo0Yip9vwxk_FPXwbu01WO-E";
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
		
		$scope.STATUS = Object.freeze({
			WAITING : {value: 0, name: "Waiting"}, 
			ASSIGNED: {value: 1, name: "Assigned"}, 
			COMPLETE : {value: 2, name: "Complete"},
			CANCELED : {value: 3, name: "Canceled"}
		});
		
		// Waiting time until the row turns red. 
		// Variable set by admin?
		$scope.DANGER_ZONE = Object.freeze(30);
		
		$scope.rideRequests = [
			{ 	
				'name': 	'Bill',
				'status':	$scope.STATUS.COMPLETE,
				'riders': 	1,
				'time':		moment().subtract(60, 'm').valueOf()
			},
			{ 	
				'name': 	'Bryce',
				'status':	$scope.STATUS.ASSIGNED,
				'riders': 	1,
				'time':		moment().subtract(20, 'm').valueOf()
			},
			{ 	
				'name': 	'Edward',
				'status':	$scope.STATUS.ASSIGNED,
				'riders': 	4,
				'time':		moment().subtract(21, 'm').valueOf()
			},
			{	
				'name': 	'Justin',
				'status':	$scope.STATUS.WAITING,
				'riders': 	20,
				'time':		moment().subtract(28, 'm').valueOf()
			},
			{ 	
				'name': 	'Nik',
				'status':	$scope.STATUS.WAITING,
				'riders': 	2,
				'time':		moment().subtract(29, 'm').valueOf()
			},
			{ 	
				'name': 	'Ryan',
				'status':	$scope.STATUS.ASSIGNED,
				'riders': 	2,
				'time':		moment().subtract(22, 'm').valueOf()
			},
			{ 	
				'name': 	'Zeeshan',
				'status':	$scope.STATUS.WAITING,
				'riders': 	4,
				'time':		moment().subtract(40, 'm').valueOf()
			}
		];
		
		$scope.requestAgeInMinutes = function(start){
			return moment.duration(moment().diff(moment(start))).asMinutes() 
		}
    });
