'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.DriverService
 * @description
 * # DriverService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('DriverService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'drivers/:id', {
            id: '@id'
        }, {
            update: {
                method: 'PUT'
            }
        });

    });

angular.module('safeRidesWebApp')
    .factory('DriverSaveService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'drivers/endofnight', null, {
            update: {
                method: 'PUT'
            }
        });
    });

angular.module('safeRidesWebApp')
    .factory('DriverRidesService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'drivers/:id/rides', {
            id: '@id'
        }, {
            get: {
                method: 'GET',
                isArray: true
            }
        });

    });

angular.module('safeRidesWebApp')
    .factory('CurrentDriverRidesService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/rides/?status=:status', {
            status: '@status'
        }, {
            get: {
                method: 'GET',
                isArray: true
            }
        });
    });

angular.module('safeRidesWebApp')
    .factory('GetDriverCurrentRideService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/currentride', {
            get: {
                method: 'GET',
                isArray: true
            }
        });
    });

angular.module('safeRidesWebApp')
    .factory('GetDriverMe', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/me', {
            get: {
                method: 'GET',
                isArray: true
            }
        });
    });

angular.module('safeRidesWebApp')
    .factory('DriverLocationService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'drivers/:id/location', {
            id: '@id'
        });
    });

angular.module('safeRidesWebApp')
    .factory('CurrentDriverLocationService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/location');
    });
