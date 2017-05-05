'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.user
 * @description
 * # user
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
    .factory('User', function () {

        function User(data) {
            this.username = undefined;
            this.firstname = undefined;
            this.lastname = undefined;
            this.email = undefined;
            this.authorities = undefined;

            if (data) {
                angular.extend(this, data);
            }
        }

        /**
         * roleName options: ['ROLE_ADMIN','ROLE_COORDINATOR','ROLE_DRIVER','ROLE_RIDER']
         * returns true or false
         */

        User.prototype.isInRole = function (roleName) {
            var isInRole = false;
            this.authorities.forEach(function (element) {
                if (element.authority === roleName) {
                    isInRole = true;
                    return;
                }
            });
            return isInRole;
        };

        return User;
    });
