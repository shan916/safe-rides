'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.settings
 * @description
 * # settings
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('Settings', function () {
        function Settings(data) {
            this.startTime = undefined;
            this.endTime = undefined;
            this.daysOfWeek = undefined;
            this.active = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        return Settings;
    });
