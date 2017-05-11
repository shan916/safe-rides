'use strict';

describe('Controller: CoordinatorreportCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var CoordinatorreportCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        CoordinatorreportCtrl = $controller('CoordinatorreportCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(CoordinatorreportCtrl.awesomeThings.length).toBe(3);
    });
});
