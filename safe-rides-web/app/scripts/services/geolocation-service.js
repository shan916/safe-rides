'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.GeolocationService
 * @description
 * # GeolocationService
 * Factory in the safeRidesWebApp.
 * http://stackoverflow.com/a/33403957
 */
angular.module('safeRidesWebApp')
    .factory('GeolocationService', ['$q', '$window', function($q, $window) {

        function getCurrentPosition() {
            var deferred = $q.defer();

            if (!$window.navigator.geolocation) {
                deferred.reject('Geolocation not supported.');
            } else {
                $window.navigator.geolocation.getCurrentPosition(
                    function(position) {
                        deferred.resolve(position);
                    },
                    function(err) {
                        deferred.reject(err);
                    });
            }

            return deferred.promise;
        }

        return {
            getCurrentPosition: getCurrentPosition
        };
    }]);
