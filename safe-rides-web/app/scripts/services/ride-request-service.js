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

    })

    .factory('MyRideService', function ($resource, ENV) {
        return $resource(ENV.apiEndpoint + 'rides/mine', {}, {
            get: {
                method: 'GET',
                // add http response to returned object
                interceptor: {
                    response: function (response) {
                        var result = response.resource;
                        result.$status = response.status;
                        return result;
                    }
                }
            },
            cancel: {
                method: 'POST',
                url: ENV.apiEndpoint + 'rides/mine/cancel'
            }
        });
    });
