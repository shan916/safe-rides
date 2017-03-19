'use strict';

/**
* @ngdoc filter
* @name safeRidesWebApp.filter:phone
* @function
* @description
* # phone
* Filter in the safeRidesWebApp.
*/
angular.module('safeRidesWebApp')
.filter('phone', function () {
    return function (input) {
        if (input && input.length === 10) {
            return '(' + input.substring(0, 3) + ') ' + input.substring(3, 6) + '-' + input.substring(6);
        }
    };
});
