'use strict';

describe('Controller: CoordinatordashboardCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var CoordinatordashboardCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CoordinatordashboardCtrl = $controller('CoordinatordashboardCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(CoordinatordashboardCtrl.awesomeThings.length).toBe(3);
  });
});
