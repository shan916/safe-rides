'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
var app = angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function ($scope, DriverService, RideRequestService, RideRequest, Driver, DriverRidesService, DriverLocationService, User, UserService, $interval, $uibModal, authManager, $state, AuthTokenService) {
        var vm = this;
        vm.loadingRideRequests = true;
        vm.loadingCoordinatorDrivers = true;

        vm.loadingRideRequests = true;
        vm.loadingCoordinatorDrivers = true;

        // TODO: Move this to an environment file
        vm.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCDx8ucIftYo0Yip9vwxk_FPXwbu01WO-E';

        vm.refreshIntervalOptions = ['15 sec', '30 sec', '45 sec', '60 sec'];
        vm.selectedRefreshInterval = '60 sec';

        vm.timeInterval = $interval(refresh, 60000);

        vm.DANGER_ZONE = 30;

        vm.drivers = [];
        vm.rideRequests = [];
        vm.driversLocation = [];

        // kick user out if not authenticated
        if (!authManager.isAuthenticated()) {
            console.log('Not authenticated');
            $state.go('login');
        }

        // kick user out if not coordinator
        if (!AuthTokenService.isInRole('ROLE_COORDINATOR')) {
            console.log('Not a coordinator');
            $state.go('/');
        }

        getDrivers();
        getRideRequests();

        function refresh() {
            getDrivers();
            getRideRequests();
        }

        vm.refreshIntervalChange = function () {
            $interval.cancel(vm.timeInterval);
            vm.timeInterval = $interval(refresh, getRefreshRate(vm.selectedRefreshInterval));
        };

        // destroy interval on exit
        $scope.$on('$destroy', function () {
            $interval.cancel(vm.timeInterval);
        });

        function getRefreshRate(selectedRefreshRate) {
            switch (selectedRefreshRate) {
                case '15 sec':
                    return 15000;
                case '30 sec':
                    return 30000;
                case '45 sec':
                    return 45000;
                case '60 sec':
                    return 60000;
            }
        }

        function getDrivers() {
            vm.loadingCoordinatorDrivers = true;
            DriverService.query({active: true}).$promise.then(function (response) {
                vm.drivers = response;

                vm.drivers.forEach(function (element, index, drivers) {
                    var driver = new Driver(element);
                    DriverRidesService.query({
                        id: driver.id
                    }).$promise.then(function (ridesResponse) {
                        driver.rides = ridesResponse;
                        console.log('got driver\'s rides:', ridesResponse);
                    }, function (ridesError) {
                        console.log('error getting driver\'s rides:', ridesError);
                    });

                    drivers[index] = driver;
                });

                vm.loadingCoordinatorDrivers = false;

                console.log('got drivers:', response);
                getDriversLocation();
            }, function (error) {
                vm.loadingCoordinatorDrivers = false;
                console.log('error getting drivers:', error);
            });
        }

        function getRideRequests() {
            vm.loadingRideRequests = true;
            RideRequestService.query().$promise.then(function (response) {
                vm.rideRequests = response;

                vm.rideRequests.forEach(function (element, index, rideRequests) {
                    rideRequests[index] = new RideRequest(element);
                });

                vm.loadingRideRequests = false;

                console.log('got ride requests:', response);
            }, function (error) {
                vm.loadingRideRequests = false;
                console.log('error getting ride requests:', error);
            });
        }

        function getDriversLocation() {
            vm.drivers.forEach(function (element) {
                DriverLocationService.get({
                    id: element.id
                }).$promise.then(function (response) {
                    if (response.id !== undefined) {
                        vm.driversLocation.push(response);
                        console.log('got drivers location:', response);
                    }
                }, function (error) {
                    console.log('error getting drivers location:', error);
                });
            });
        }

        vm.mapPinClick = function (evt, rideRequestId) {
            vm.rideRequests.forEach(function (element) {
                if (rideRequestId === element.id) {
                    vm.showRequestDetails(element);
                }
            });
        };

        vm.mapDriverPinClick = function (evt, driverId) {
            vm.drivers.forEach(function (element) {
                if (driverId === element.id) {
                    vm.showDriverDetails(element);
                }
            });
        };


        vm.requestAgeInMinutes = function (start) {
            return moment.duration(moment().diff(moment(start))).asMinutes();
        };

        vm.showRequestDetails = function (req) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/requestdetailsmodal.html',
                controller: 'RequestDetailModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    request: function () {
                        return req;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function () {
                console.log('ok');
            }, function () {
                console.log('cancel');
            });
        };

        vm.showDriverDetails = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/driverdetailsmodal.html',
                controller: 'DriverDetailsModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    driver: function () {
                        return driver;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function () {
                console.log('ok');
            }, function () {
                console.log('cancel');
            });
        };

        vm.showAssignDriver = function (req) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/assigndrivermodal.html',
                controller: 'AssignDriverModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    request: function () {
                        return req;
                    },
                    drivers: function () {
                        return vm.drivers;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function () {
                console.log('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function () {
                console.log('cancel');
            });
        };

        vm.showAssignRequest = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/assignrequestmodal.html',
                controller: 'AssignRequestModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    driver: function () {
                        return driver;
                    },
                    requests: function () {
                        return vm.rideRequests;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function () {
                console.log('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function () {
                console.log('cancel');
            });
        };

        vm.confirmCancelRequest = function (request) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/confirmcancelmodal.html',
                controller: 'ConfirmCancelRequestModalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    request: function () {
                        return request;
                    },
                    drivers: function () {
                        return vm.drivers;
                    }
                },
                size: 'lg'
            });
            modalInstance.result.then(function () {
                console.log('cancelling ride, refreshing Ride Requests table');
                getRideRequests();
                getDrivers();
            }, function () {
                console.log('cancel cancelling ride');
            });
        };

        /* Modal Add ride request */
        vm.showRideRequest = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/addriderequestmodal.html',
                controller: 'AddriderequestmodalCtrl',
                controllerAs: 'ctrl',
                size: 'lg'
            });

            modalInstance.result.then(function () {
                console.log('ok');
            }, function () {
                getRideRequests();
                console.log('cancel');
            });
        }; //end showRideRequest function
    }); //End CoordinatordashboardCtrl

app.filter('FriendlyStatusName', function () {
    return function (text) {
        switch (text) {
            case 'UNASSIGNED':
                return 'Unassigned';
            case 'ASSIGNED':
                return 'Assigned';
            case 'PICKINGUP':
                return 'Driving to pickup';
            case 'DROPPINGOFF':
                return 'Driving to dropoff';
            case 'COMPLETE':
                return 'Complete';
            case 'CANCELEDBYCOORDINATOR':
                return 'Canceled by Coordinator';
            case 'CANCELEDBYRIDER':
                return 'Canceled by Rider';
            case 'AVAILABLE':
                return 'Available';
            case 'CANCELEDOTHER':
                return 'Canceled by Other';
        }
    };
});
