'use strict';

/**
 * @ngdoc directive
 * @name safeRidesWebApp.directive:compareTo
 * @description
 * # compareTo
 */
angular.module('safeRidesWebApp')
    .directive('compareTo', function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, ctrl) {

                ctrl.$validators.compareTo = function (val) {
                    var compareTo = scope.$eval(attrs.compareTo);

                    // valid if both fields null
                    if (!val && !compareTo) {
                        return true;
                    }

                    return val === compareTo;
                };

                scope.$watch(attrs.compareTo, function () {
                    ctrl.$validate();
                });
            }
        };
    });
