'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.settingsService
 * @description Contains wrappers for API calls
 * # settingsService
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('SettingsService', function ($http, ENV) {
        /**
         * Get a booolean response whether the API is accepting new Ride Requests
         */
        this.isLive = function () {
            return $http.get(ENV.apiEndpoint + 'config/isLive');
        };

        this.message = function () {
            return $http.get(ENV.apiEndpoint + 'config/message');
        };

        /**
         * Get the current application configuration
         */
        this.current = function () {
            return $http.get(ENV.apiEndpoint + 'config/current');
        };

        /**
         * Update the application configuration
         */
        this.update = function (settings) {
            return $http.put(ENV.apiEndpoint + 'config/update', settings);
        };

        return {
            isLive: this.isLive,
            message: this.message,
            current: this.current,
            update: this.update
        };
    });
