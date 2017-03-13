'use strict';

describe('Controller: DriverdetailsmodalCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var DriverdetailsmodalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        DriverdetailsmodalCtrl = $controller('DriverdetailsmodalCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(DriverdetailsmodalCtrl.awesomeThings.length).toBe(3);
    });
});
