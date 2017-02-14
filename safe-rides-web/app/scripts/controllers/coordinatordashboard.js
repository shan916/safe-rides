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

    /* form validation plugin */
    $.fn.goValidate = function() {
        var $form = this,
            $inputs = $form.find('input:text');

        var validators = {
            name: {
                regex: /^[A-Za-z]{2,}$/
                /*must be at least 3 chars long */
            },
            phone: {
                regex: /^[2-9]\d{2}-\d{3}-\d{4}$/,
                /*999-999-999*/
            }
        };
        var validate = function(klass, value) {
            var isValid = true,
                error = '';

                /* If there is no input & it's required*/
            if (!value && /required/.test(klass)) {
                error = 'This field is required';
                isValid = false;
            } else {
                klass = klass.split(/\s/);
                $.each(klass, function(i, k){
                    if (validators[k]) {
                        if (value && !validators[k].regex.test(value)) {
                            isValid = false;
                            error = validators[k].error;
                        }
                    }
                });
            }
            return {
                isValid: isValid,
                error: error
            };
        };
        var showError = function($input) {
            var klass = $input.attr('class'),/* class form-control required */
                value = $input.val(),
                test = validate(klass, value);

            $input.removeClass('invalid');
            $('#form-error').addClass('hide');

            if (!test.isValid) {
                $input.addClass('invalid');

                if(typeof $input.data("shown") == "undefined" || $input.data("shown") == false){
                   $input.popover('show');
                }

            }
          else {
            $input.popover('hide');
          }
        };

        $inputs.keyup(function() {
            showError($(this));
        });

        $inputs.on('shown.bs.popover', function () {
      		$(this).data("shown",true);
    	});

        $inputs.on('hidden.bs.popover', function () {
      		$(this).data("shown",false);
    	});

        $form.submit(function(e) {

            $inputs.each(function() { /* test each input */
            	if ($(this).is('.required') || $(this).hasClass('invalid')) {
                	showError($(this));
            	}
        	});
            if ($form.find('input.invalid').length) { /* form is not valid */
            	e.preventDefault();
                $('#form-error').toggleClass('hide');
            }
        });
        return this;
    };
    $('form').goValidate();
    /************************end modal **/


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
        'time': moment().subtract(60, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 1,
        'name': 'Bryce',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 1,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(20, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 2,
        'name': 'Edward',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 4,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(21, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 3,
        'name': 'Justin',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 20,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(28, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 4,
        'name': 'Nik',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 2,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(29, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 5,
        'name': 'Ryan',
        'status': vm.REQUEST_STATUS.ASSIGNED,
        'riders': 2,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(22, 'm').valueOf(),
        'deleted': false
      },
      {
        'id': 6,
        'name': 'Zeeshan',
        'status': vm.REQUEST_STATUS.WAITING,
        'riders': 4,
        'pickupLocation': '123 Main St.',
        'destination': '234 Main St.',
        'time': moment().subtract(40, 'm').valueOf(),
        'deleted': false
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
        templateUrl: 'views/requestdetails.html',
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

    vm.showAssignDriver = function(req) {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/assigndriver.html',
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
        templateUrl: 'views/assignrequest.html',
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

    vm.confirmCancelRequest = function(request) {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/confirm.html',
        controller: 'ConfirmCancelRequestModalCtrl',
        controllerAs: 'confirmModal',
        resolve: {
          request: function() {
            return request;
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
