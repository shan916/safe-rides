'use strict';

/**
* @ngdoc service
* @name safeRidesWebApp.driver
* @description
* # driver
* Factory in the safeRidesWebApp.
*/
angular.module('safeRidesWebApp')
.factory('Driver', function (Vehicle) {

    function Driver(data) {
        this.csusId = undefined;
        this.name = undefined;
        this.dlState = undefined;
        this.dlNumber = undefined;
        this.sex = undefined;
        this.insuranceChecked = undefined;
        this.active = true;
        this.vehicle = new Vehicle();
    }

    return Driver;

});
