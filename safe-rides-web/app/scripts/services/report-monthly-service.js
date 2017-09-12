'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.DriverService
 * @description
 * # DriverService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('MonthlyStats', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'reports/monthStats?', {
            month: '@month',
            year: '@year'
        }, {
            get: {
                method: 'GET',
                isArray: true
            }
        });

    });
