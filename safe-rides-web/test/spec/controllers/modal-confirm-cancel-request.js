'use strict';

describe('Controller: ConfirmcancelrequestmodalCtrl', function() {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var ConfirmcancelrequestmodalCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function($controller, $rootScope) {
        scope = $rootScope.$new();
        ConfirmcancelrequestmodalCtrl = $controller('ConfirmcancelrequestmodalCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function() {
        expect(ConfirmcancelrequestmodalCtrl.awesomeThings.length).toBe(3);
    });
});
