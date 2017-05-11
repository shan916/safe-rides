'use strict';

describe('Controller: RiderdashboardCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var RiderdashboardCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        RiderdashboardCtrl = $controller('RiderdashboardCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(RiderdashboardCtrl.awesomeThings.length).toBe(3);
    });
});
