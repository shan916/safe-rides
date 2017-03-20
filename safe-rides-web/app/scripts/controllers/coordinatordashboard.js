'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
var app = angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function(DriverService, RideRequestService, RideRequest, Driver, DriverRidesService, $interval, $uibModal) {
        var vm = this;
        vm.loadingRideRequests = true;
        vm.loadingCoordinatorDrivers = true;
        vm.loadingCoordinatorTables = true;



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

        function getDrivers() {
            vm.loadingCoordinatorDrivers = true;

            DriverService.query({
                active: true
            }).$promise.then(function(response) {
                vm.drivers = response;

                vm.drivers.forEach(function(element, index, drivers) {
                    var driver = new Driver(element);
                    DriverRidesService.query({
                        id: driver.id
                    }).$promise.then(function(ridesResponse) {
                        driver.rides = ridesResponse;
                        console.log('got driver\'s rides:' + ridesResponse);
                    }, function(ridesError) {
                        console.log('error getting driver\'s rides:' + ridesError);
                    })

                    drivers[index] = driver;
                });

                vm.loadingCoordinatorDrivers = false;
                if (vm.loadingRideRequests === false && vm.loadingCoordinatorDrivers === false) {
                    vm.loadingCoordinatorTables = false;
                }

                console.log('got drivers:', response);
            }, function(error) {
                console.log('error getting drivers:', error);
            });
        }

        function getRideRequests() {
            vm.loadingRideRequests = true;
            RideRequestService.query().$promise.then(function(response) {
                vm.rideRequests = response;

                vm.rideRequests.forEach(function(element, index, rideRequests) {
                    var rideRequest = new RideRequest(element);
                    rideRequests[index] = rideRequest;
                });
                vm.loadingRideRequests = false;
                if (vm.loadingRideRequests === false && vm.loadingCoordinatorDrivers === false) {
                    vm.loadingCoordinatorTables = false;
                }
                console.log('got ride requests:', response);
            }, function(error) {
                console.log('error getting ride requests:', error);
            });
        }

        // if (vm.loadingRideRequests === false && vm.loadingCoordinatorDrivers === false){
        //     vm.loadingCoordinatorTables = false;
        // }
        vm.DANGER_ZONE = Object.freeze(30);

        vm.drivers = [];

        getDrivers();

        vm.rideRequests = [];

        getRideRequests();

        vm.requestAgeInMinutes = function(start) {
            return moment.duration(moment().diff(moment(start))).asMinutes();
        };

        vm.showRequestDetails = function(req) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/requestdetailsmodal.html',
                controller: 'RequestDetailModalCtrl',
                controllerAs: 'ctrl',
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

        vm.showDriverDetails = function(driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/driverdetailsmodal.html',
                controller: 'DriverDetailsModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    driver: function() {
                        return driver;
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
                templateUrl: 'views/assigndrivermodal.html',
                controller: 'AssignDriverModalCtrl',
                controllerAs: 'ctrl',
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
                console.log('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function() {
                console.log('cancel');
            });
        };

        vm.showAssignRequest = function(driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/assignrequestmodal.html',
                controller: 'AssignRequestModalCtrl',
                controllerAs: 'ctrl',
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
                console.log('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function() {
                console.log('cancel');
            });
        };

        vm.confirmCancelRequest = function(request) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmcancelmodal.html',
                controller: 'ConfirmCancelRequestModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    request: function() {
                        return request;
                    },
                    drivers: function() {
                        return vm.drivers;
                    }
                },
                size: 'lg'
            });
            modalInstance.result.then(function() {
                console.log('cancelling ride, refreshing Ride Requests table');
                getRideRequests();
                getDrivers();
            }, function() {
                console.log('cancel cancelling ride');
            });
        };

        /* Modal Add ride request */
        vm.showRideRequest = function() {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/addriderequestmodal.html',
                controller: 'AddriderequestmodalCtrl',
                controllerAs: 'ctrl',
                size: 'lg'
            });

            modalInstance.result.then(function() {
                console.log('ok');
            }, function() {
                getRideRequests();
                console.log('cancel');
            });
        }; //end showRideRequest function
    }); //End CoordinatordashboardCtrl

app.filter('FriendlyStatusName', function() {
    return function(text) {
        switch (text) {
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
                return 'Canceled by Rider';
            case 'AVAILABLE':
                return 'Available';
            case 'CANCELEDOTHER':
                return 'Canceled by Other';
        }
    };
});
