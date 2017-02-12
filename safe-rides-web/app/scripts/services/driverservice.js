'use strict';

/**
* @ngdoc service
* @name safeRidesWebApp.DriverService
* @description
* # DriverService
* Factory in the safeRidesWebApp.
*/
angular.module('safeRidesWebApp')
.factory('DriverService', function () {

    var drivers = [
        {
            csusId: '0000001',
            name: 'Zeeshan Khaliq',
            dlNumber: 'Z1234567',
            dlState: 'CA',
            sex: 'Male',
            active: true,
            vehicle: {
                make: 'Toyota',
                model: 'Camry',
                year: '2016',
                licensePlate: '1ABC2345',
                color: 'Silver',
                seats: 5
            }
        },
        {
            csusId: '0000002',
            name: 'Edward Ozeruga',
            dlNumber: 'E1234567',
            dlState: 'CA',
            sex: 'Male',
            active: true,
            vehicle: {
                make: 'Toyota',
                model: 'Corolla',
                year: '1875',
                licensePlate: '1ABC2345',
                color: 'Pink',
                seats: 5
            }
        },
        {
            csusId: '0000003',
            name: 'Ryan Long',
            dlNumber: 'R1234567',
            dlState: 'CA',
            sex: 'Male',
            active: false,
            vehicle: {
                make: 'Toyota',
                model: 'Camry',
                year: '2016',
                licensePlate: '1ABC2345',
                color: 'Silver',
                seats: 5
            }
        },
        {
            csusId: '0000004',
            name: 'Bryce Hairabedian',
            dlNumber: 'B1234567',
            dlState: 'CA',
            sex: 'Male',
            active: false,
            vehicle: {
                make: 'Toyota',
                model: 'Camry',
                year: '2016',
                licensePlate: '1ABC2345',
                color: 'Silver',
                seats: 5
            }
        },
        {
            csusId: '0000005',
            name: 'Nik Sorvari',
            dlNumber: 'N1234567',
            dlState: 'CA',
            sex: 'Male',
            active: true,
            vehicle: {
                make: 'Toyota',
                model: 'Camry',
                year: '2016',
                licensePlate: '1ABC2345',
                color: 'Silver',
                seats: 5
            }
        },
        {
            csusId: '0000006',
            name: 'Justin Mendiguarin',
            dlNumber: 'J1234567',
            dlState: 'CA',
            sex: 'Male',
            active: false,
            vehicle: {
                make: 'Toyota',
                model: 'Camry',
                year: '2016',
                licensePlate: '1ABC2345',
                color: 'Silver',
                seats: 5
            }
        }
    ];

    return {
        getDrivers: function() {
            return drivers;
        },
        getDriver: function(driverId) {
            for (var i = 0; i < drivers.length; i++) {
                if (drivers[i].csusId === driverId) {
                    return drivers[i];
                }
            }
        },
        saveDriver: function(driver) {
            drivers.push(driver);
        },
        deleteDriver: function(driver) {
            drivers.splice(drivers.indexOf(driver), 1);
        }
    };

});
