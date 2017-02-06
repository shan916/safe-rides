'use strict';

describe('Controller: DriverdashboardCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var DriverdashboardCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DriverdashboardCtrl = $controller('DriverdashboardCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(DriverdashboardCtrl.awesomeThings.length).toBe(3);
  });
});
