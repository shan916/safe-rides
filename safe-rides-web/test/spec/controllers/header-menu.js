'use strict';

describe('Controller: HeaderMenuCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var HeaderMenuCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    HeaderMenuCtrl = $controller('HeaderMenuCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(HeaderMenuCtrl.awesomeThings.length).toBe(3);
  });
});
