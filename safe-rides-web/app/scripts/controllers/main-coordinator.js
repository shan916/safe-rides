'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
var app = angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function ($scope, DriverService, RideRequestService, RideRequest, Driver, User, AuthService, $interval, $uibModal, authManager, $state, AuthTokenService, Notification, SettingsService, ENV, $log) {
        var vm = this;
        vm.loadingRideRequests = true;
        vm.loadingCoordinatorDrivers = true;

        vm.loadingRideRequests = true;
        vm.loadingCoordinatorDrivers = true;

        // TODO: Move this to an environment file
        vm.googleMapsUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCDx8ucIftYo0Yip9vwxk_FPXwbu01WO-E';

        vm.refreshIntervalOptions = ['15 sec', '30 sec', '45 sec', '60 sec'];
        vm.selectedRefreshInterval = '60 sec';

        vm.timeInterval = $interval(loadData, 60000);

        vm.DANGER_ZONE = 30;

        vm.drivers = [];
        vm.rideRequests = [];

        /*
         * Kick user out if not authenticated or if not a coordinator
         * */
        if (authManager.isAuthenticated()) {
            if (!AuthTokenService.isInRole('ROLE_COORDINATOR')) {
                Notification.error({
                    message: 'You must be logged in as a coordinator to view the coordinator dashboard.',
                    positionX: 'center',
                    delay: 10000,
                    replaceMessage: true
                });
                $state.go('/');
                $log.debug('Not a coordinator');
            } else {
                loadData();
            }
        } else {
            $state.go('/');
            $log.debug('Not authenticated');
        }

        function loadData() {
            getDrivers();
            getRideRequests();
        }

        vm.refreshIntervalChange = function () {
            $interval.cancel(vm.timeInterval);
            vm.timeInterval = $interval(loadData, getRefreshRate(vm.selectedRefreshInterval));
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
                    drivers[index] = new Driver(element);
                });

                vm.loadingCoordinatorDrivers = false;

                $log.debug('got drivers:', response);
            }, function (error) {
                vm.loadingCoordinatorDrivers = false;
                $log.debug('error getting drivers:', error);
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

                $log.debug('got ride requests:', response);
            }, function (error) {
                vm.loadingRideRequests = false;
                $log.debug('error getting ride requests:', error);
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
                templateUrl: 'views/modal-request-details.html',
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
                $log.debug('ok');
            }, function () {
                $log.debug('cancel');
            });
        };

        vm.showDriverDetails = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-driver-details.html',
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
                $log.debug('ok');
            }, function () {
                $log.debug('cancel');
            });
        };

        vm.showAssignDriver = function (req) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-assign-driver.html',
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
                $log.debug('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function () {
                $log.debug('cancel');
            });
        };

        vm.showAssignRequest = function (driver) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-assign-request.html',
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
                $log.debug('ok clicked, refreshing drivers and rides');
                getRideRequests();
                getDrivers();
            }, function () {
                $log.debug('cancel');
            });
        };

        vm.confirmCancelRequest = function (request) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-cancel.html',
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
                $log.debug('cancelling ride, refreshing Ride Requests table');
                getRideRequests();
                getDrivers();
            }, function () {
                $log.debug('cancel cancelling ride');
            });
        };

        vm.openConfirmReactivateRide = function (ride) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-confirm-reactivate-ride.html',
                controller: 'ModalConfirmReactivateRideCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    ride: function () {
                        return ride;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function (ride) {
                ride.status = 'UNASSIGNED';
                RideRequestService.update(ride).$promise.then(function (response) {
                    $log.debug('ride reactivated:', response);
                    getRideRequests();
                    getDrivers();
                }, function (error) {
                    $log.debug('error reactivating ride:', error);
                });
            }, function () {
                // cancel clicked
            });
        };

        /* Modal Add ride request */
        vm.showRideRequest = function (ride) {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/modal-add-ride-request.html',
                controller: 'AddriderequestmodalCtrl',
                controllerAs: 'ctrl',
                resolve: {
                    ride: function () {
                        return ride;
                    }
                },
                size: 'lg'
            });

            modalInstance.result.then(function () {
                $log.debug('ok');
            }, function () {
                getRideRequests();
                $log.debug('cancel');
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
            case 'ATPICKUPLOCATION':
                return 'Driver at pickup location';
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
