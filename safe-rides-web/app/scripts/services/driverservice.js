'use strict';

/**
* @ngdoc service
* @name safeRidesWebApp.DriverService
* @description
* # DriverService
* Factory in the safeRidesWebApp.
*/
angular.module('safeRidesWebApp')
.factory('DriverService', function ($resource) {

    return $resource('http://localhost:8080/safe-rides-api/drivers/:id', { id: '@id' }, {
        update: {
            method: 'PUT'
        }
    });

});
