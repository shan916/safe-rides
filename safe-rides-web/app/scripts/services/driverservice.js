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

    return $resource(ENV.apiEndpoint + 'drivers/:id', { id: '@id' }, {
        update: {
            method: 'PUT'
        }
    });

});