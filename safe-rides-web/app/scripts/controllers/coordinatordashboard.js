'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
var REQUEST_STATUS = Object.freeze({
	WAITING: {
		value: 0,
		name: 'Waiting'
	},
	ASSIGNED: {
		value: 1,
		name: 'Assigned'
	},
	COMPLETE: {
		value: 2,
		name: 'Complete'
	},
	CANCELED: {
		value: 3,
		name: 'Canceled'
	}
});

var DRIVER_STATUS = Object.freeze({
	AVAILABLE: {
		value: 0,
		name: 'Available'
	},
	DRIVING: {
		value: 1,
		name: 'Driving'
	},
	INACTIVE: {
		value: 2,
		name: 'Inactive'
	}
});

angular.module('safeRidesWebApp')
  .controller('CoordinatordashboardCtrl', function($interval, $uibModal) {
    var vm = this;

    // TODO: Move this to an environment file
    vm.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCDx8ucIftYo0Yip9vwxk_FPXwbu01WO-E';

    vm.positions = [
      [38.55, -121.45],
      [38.54, -121.44],
      [38.53, -121.43],
      [38.52, -121.42]
    ];
    $interval(function() {
      var numMarkers = 4;
      vm.positions = [];
      for (var i = 0; i < numMarkers; i++) {
        var lat = 38.55 + (Math.random() / 100);
        var lng = -121.45 + (Math.random() / 100);
        vm.positions.push([lat, lng]);
      }
    }, 15000);

    vm.REQUEST_STATUS = REQUEST_STATUS;
    vm.DRIVER_STATUS = DRIVER_STATUS;

    // Waiting time until the row turns red.
    // Variable set by admin?
    vm.DANGER_ZONE = Object.freeze(30);

    vm.rideRequests = [{
				'id': 0,
        'name': 'Bill',
        'status': vm.REQUEST_STATUS.COMPLETE,
        'riders': 1,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(60, 'm').valueOf()
      },
      {
				'id': 1,
        'name': 'Bryce',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 1,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(20, 'm').valueOf()
      },
      {
				'id': 2,
        'name': 'Edward',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 4,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(21, 'm').valueOf()
      },
      {
				'id': 3,
        'name': 'Justin',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 20,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(28, 'm').valueOf()
      },
      {
				'id': 4,
        'name': 'Nik',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 2,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(29, 'm').valueOf()
      },
      {
				'id': 5,
        'name': 'Ryan',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 2,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(22, 'm').valueOf()
      },
      {
				'id': 6,
        'name': 'Zeeshan',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 4,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(40, 'm').valueOf()
      }
    ];

    vm.drivers = [{
				'id': 0,
        'name': 'Mark',
        'status': vm.DRIVER_STATUS.AVAILABLE,
        'driverLicense': 'A1234567',
        'vehicle': {
          'make': 'Toyota',
          'model': 'Corolla',
          'year': 1876,
          'color': 'yellow',
          'licensePlate': 'ABC123',
          'seats': 5
        }
      },
      {
				'id': 1,
        'name': 'Jacob',
        'status': vm.DRIVER_STATUS.DRIVING,
        'driverLicense': 'A1234567',
        'vehicle': {
          'make': 'Toyota',
          'model': 'Corolla',
          'year': 1876,
          'color': 'yellow',
          'licensePlate': 'ABC123',
          'seats': 4
        }
      },
      {
				'id': 2,
        'name': 'Larry',
        'status': vm.DRIVER_STATUS.DRIVING,
        'driverLicense': 'A1234567',
        'vehicle': {
          'make': 'Toyota',
          'model': 'Corolla',
          'year': 1876,
          'color': 'yellow',
          'licensePlate': 'ABC123',
          'seats': 3
        }
      },
      {
				'id': 3,
        'name': 'Penny',
        'status': vm.DRIVER_STATUS.DRIVING,
        'driverLicense': 'A1234567',
        'vehicle': {
          'make': 'Toyota',
          'model': 'Corolla',
          'year': 1876,
          'color': 'yellow',
          'licensePlate': 'ABC123',
          'seats': 3
        }
      }
    ];

    vm.requestAgeInMinutes = function(start) {
      return moment.duration(moment().diff(moment(start))).asMinutes();
    };

    vm.showRequestDetails = function(req) {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/partials/coordinator/requestdetails.html',
        controller: 'RequestDetailModalCtrl',
        controllerAs: 'rdModal',
        resolve: {
          request: function() {
            return req;
          }
        },
        size: 'sm'
      });

      modalInstance.result.then(function() {
        console.log('ok');
      }, function() {
        console.log('cancel');
      });
    };

		vm.showAssignDriver= function(req) {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/partials/coordinator/assigndriver.html',
        controller: 'AssignDriverModalCtrl',
        controllerAs: 'adModal',
        resolve: {
          request: function() {
            return req;
          },
					drivers: function() {
            return vm.drivers;
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

		vm.showAssignRequest = function(driver) {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/partials/coordinator/assignrequest.html',
        controller: 'AssignRequestModalCtrl',
        controllerAs: 'arModal',
        resolve: {
					driver: function() {
            return driver;
          },
          requests: function() {
            return vm.rideRequests;
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
  .controller('RequestDetailModalCtrl', function($uibModalInstance, request) {
    var vm = this;
    vm.request = request;

    vm.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };

    vm.ok = function() {
      $uibModalInstance.close();
    };
  });

	angular.module('safeRidesWebApp')
	  .controller('AssignDriverModalCtrl', function($uibModalInstance, request, drivers) {
	    var vm = this;
	    vm.request = request;
			vm.drivers = drivers;
			vm.selectedDriver = null;
			vm.selectedDriverID = null;
			vm.DRIVER_STATUS = DRIVER_STATUS;

	    vm.cancel = function() {
	      $uibModalInstance.dismiss('cancel');
	    };

	    vm.ok = function() {
				vm.selectedDriver.status = DRIVER_STATUS.DRIVING;
				vm.request.status = REQUEST_STATUS.ASSIGNED;
	      $uibModalInstance.close();
	    };

			vm.changed = function(){
				// assuming id = index :o
				vm.selectedDriver = drivers[vm.selectedDriverID];
			};
	  });

		angular.module('safeRidesWebApp')
		  .controller('AssignRequestModalCtrl', function($uibModalInstance, driver, requests) {
		    var vm = this;
				vm.driver = driver;
				vm.requests = requests;
				vm.selectedRequest = null;
				vm.selectedRequestID = null;
				vm.REQUEST_STATUS = REQUEST_STATUS;

		    vm.cancel = function() {
		      $uibModalInstance.dismiss('cancel');
		    };

		    vm.ok = function() {
					vm.selectedRequest.status = REQUEST_STATUS.ASSIGNED;
					vm.driver.status = DRIVER_STATUS.DRIVING;
		      $uibModalInstance.close();
		    };

				vm.changed = function(){
					// assuming id = index :o
					vm.selectedRequest = vm.requests[vm.selectedRequestID];
				};
		  });
