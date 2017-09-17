'use strict';

describe('Controller: AssignrequestmodalCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var AssignrequestmodalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        AssignrequestmodalCtrl = $controller('AssignrequestmodalCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(AssignrequestmodalCtrl.awesomeThings.length).toBe(3);
    });
});
