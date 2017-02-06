'use strict';

describe('Controller: ResetpasswordrequestCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var ResetpasswordrequestCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ResetpasswordrequestCtrl = $controller('ResetpasswordrequestCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ResetpasswordrequestCtrl.awesomeThings.length).toBe(3);
  });
});
