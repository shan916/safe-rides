'use strict';

describe('Controller: ConfirmdeletedrivermodalCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var ConfirmdeletedrivermodalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ConfirmdeletedrivermodalCtrl = $controller('ConfirmdeletedrivermodalCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ConfirmdeletedrivermodalCtrl.awesomeThings.length).toBe(3);
  });
});
