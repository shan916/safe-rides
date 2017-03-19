'use strict';

describe('Controller: RegisterdriverCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var RegisterdriverCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        RegisterdriverCtrl = $controller('RegisterdriverCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(RegisterdriverCtrl.awesomeThings.length).toBe(3);
    });
});
