'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function ($interval, $uibModal) {
        var vm = this;
		
        // TODO: Move this to an environment file
        vm.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=AIzaSyCDx8ucIftYo0Yip9vwxk_FPXwbu01WO-E";
        
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
        }, 15000);
		
		vm.STATUS = Object.freeze({
			WAITING : {value: 0, name: "Waiting"}, 
			ASSIGNED: {value: 1, name: "Assigned"}, 
			COMPLETE : {value: 2, name: "Complete"},
			CANCELED : {value: 3, name: "Canceled"}
		});
		
		// Waiting time until the row turns red. 
		// Variable set by admin?
		vm.DANGER_ZONE = Object.freeze(30);
		
		vm.rideRequests = [
			{ 	
				'name': 	'Bill',
				'status':	vm.STATUS.COMPLETE,
				'riders': 	1,
				'time':		moment().subtract(60, 'm').valueOf()
			},
			{ 	
				'name': 	'Bryce',
				'status':	vm.STATUS.ASSIGNED,
				'riders': 	1,
				'time':		moment().subtract(20, 'm').valueOf()
			},
			{ 	
				'name': 	'Edward',
				'status':	vm.STATUS.ASSIGNED,
				'riders': 	4,
				'time':		moment().subtract(21, 'm').valueOf()
			},
			{	
				'name': 	'Justin',
				'status':	vm.STATUS.WAITING,
				'riders': 	20,
				'time':		moment().subtract(28, 'm').valueOf()
			},
			{ 	
				'name': 	'Nik',
				'status':	vm.STATUS.WAITING,
				'riders': 	2,
				'time':		moment().subtract(29, 'm').valueOf()
			},
			{ 	
				'name': 	'Ryan',
				'status':	vm.STATUS.ASSIGNED,
				'riders': 	2,
				'time':		moment().subtract(22, 'm').valueOf()
			},
			{ 
				'name': 	'Zeeshan',
				'status':	vm.STATUS.WAITING,
				'riders': 	4,
				'time':		moment().subtract(40, 'm').valueOf()
			}
		];
		
		vm.requestAgeInMinutes = function(start){
			return moment.duration(moment().diff(moment(start))).asMinutes() 
		}
		
		vm.showRequestDetails = function(req) {
			var modalInstance = $uibModal.open({
				templateUrl: 'views/partials/requestdetails.html',
				controller: 'RequestDetailModalCtrl',
				controllerAs: 'rdModal',
				resolve: {
					request: function () {
						return req;
					}
				},
				size: 'lg'
			});
		
			modalInstance.result.then(function() {
				console.log('ok');
			}, function() {
				console.log('cancel');
			});
		};
		
});	
	
angular.module('safeRidesWebApp')
	.controller('RequestDetailModalCtrl', function ($uibModalInstance, request) {
	  var vm = this;
	  vm.request = request;
	  
	  vm.cancel = function() {
		$uibModalInstance.dismiss('cancel');  
	  };
	  
	  vm.ok = function() {
		$uibModalInstance.close();  
	  };
});