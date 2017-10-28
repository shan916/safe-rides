'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.reportsService
 * @description
 * # reportsService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('ReportsService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'reports');
    });
