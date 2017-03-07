'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
var app = angular.module('safeRidesWebApp')
  .controller('CoordinatordashboardCtrl', function(DriverService, RideRequestService, $interval, $uibModal) {
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

    // Waiting time until the row turns red.
    // Variable set by admin?
    vm.DANGER_ZONE = Object.freeze(30);

    vm.drivers = [];

    getDrivers();

    function getDrivers() {
        DriverService.query().$promise.then(function(response) {
            vm.drivers = response;
            console.log('got drivers:', response);
        }, function(error) {
            console.log('error getting drivers:', error);
        });
    }

    vm.rideRequests = [];

    getRideRequests();

    function getRideRequests() {
        RideRequestService.query().$promise.then(function(response) {
            vm.rideRequests = response;
            console.log('got ride requests:', response);
        }, function(error) {
            console.log('error getting ride requests:', error);
        });
    }

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
        size: 'lg'
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

    /* Modal Add ride request */
    vm.showRideRequest = function() {
      var modalInstance = $uibModal.open({
        templateUrl: 'views/addriderequestmodal.html',
        controller: 'AddriderequestmodalCtrl',
        controllerAs: 'addrideCtrl',
        size: 'lg'
      });

      modalInstance.result.then(function(){
          console.log('ok');
        }, function(){
          getRideRequests();
          console.log('cancel');
        });
      };//end showRideRequest function
}); //End CoordinatordashboardCtrl

app.filter('FriendlyStatusName', function () {
    return function(text) {
      switch(text){
        case 'UNASSIGNED':
          return 'Unassigned';
        case 'ASSIGNED':
          return 'Assigned';
        case 'INPROGRESS':
          return 'In Progress';
        case 'COMPLETE':
          return 'Complete';
        case 'CANCELEDBYCOORDINATOR':
          return 'Canceled by Coordinator';
        case 'CANCELEDBYREQUESTOR':
          return 'Canceled by Requestor';
      }
    };
});
