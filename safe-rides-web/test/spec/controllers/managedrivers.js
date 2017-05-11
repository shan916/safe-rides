'use strict';

describe('Controller: ManagedriversCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var ManagedriversCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        ManagedriversCtrl = $controller('ManagedriversCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(ManagedriversCtrl.awesomeThings.length).toBe(3);
    });
});
