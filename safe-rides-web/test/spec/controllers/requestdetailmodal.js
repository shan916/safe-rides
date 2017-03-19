'use strict';

describe('Controller: RequestdetailmodalCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var RequestdetailmodalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        RequestdetailmodalCtrl = $controller('RequestdetailmodalCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(RequestdetailmodalCtrl.awesomeThings.length).toBe(3);
    });
});
