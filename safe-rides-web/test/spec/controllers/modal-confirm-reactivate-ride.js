'use strict';

describe('Controller: ModalConfirmReactivateRideCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var ModalConfirmReactivateRideCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ModalConfirmReactivateRideCtrl = $controller('ModalConfirmReactivateRideCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ModalConfirmReactivateRideCtrl.awesomeThings.length).toBe(3);
  });
});
