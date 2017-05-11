'use strict';

describe('Controller: AddriderequestmodalCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var AddriderequestmodalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        AddriderequestmodalCtrl = $controller('AddriderequestmodalCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(AddriderequestmodalCtrl.awesomeThings.length).toBe(3);
    });
});
