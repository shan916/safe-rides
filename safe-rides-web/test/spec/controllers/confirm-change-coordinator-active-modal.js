'use strict';

describe('Controller: ConfirmChangeCoordinatorActiveModalCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var ConfirmChangeCoordinatorActiveModalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ConfirmChangeCoordinatorActiveModalCtrl = $controller('ConfirmChangeCoordinatorActiveModalCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ConfirmChangeCoordinatorActiveModalCtrl.awesomeThings.length).toBe(3);
  });
});
