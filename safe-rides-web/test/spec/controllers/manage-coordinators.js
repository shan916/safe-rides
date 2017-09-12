'use strict';

describe('Controller: ManagecoordinatorsCtrl', function () {

    // load the controller's module
    beforeEach(module('safeRidesWebApp'));

    var ManagecoordinatorsCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        ManagecoordinatorsCtrl = $controller('ManagecoordinatorsCtrl', {
            $scope: scope
            // place here mocked dependencies
        });
    }));

    it('should attach a list of awesomeThings to the scope', function () {
        expect(ManagecoordinatorsCtrl.awesomeThings.length).toBe(3);
    });
});
