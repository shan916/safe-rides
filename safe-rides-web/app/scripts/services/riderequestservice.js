'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.riderequestservice
 * @description
 * # riderequestservice
 * Service in the safeRidesWebApp.
 */
//TODO I'm not sure what to do with this service
angular.module('safeRidesWebApp')
    .factory('RideRequestService', function ($resource, ENV) {

        return $resource(ENV.apiEndpoint + 'rides/:id', {
            id: '@id'
        }, {
            update: {
                method: 'PUT'
            }
        });

    });
