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
    .factory('GetDriverSelf', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/me');
    });

angular.module('safeRidesWebApp')
    .factory('CurrentDriverLocationService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'drivers/location');
    });

