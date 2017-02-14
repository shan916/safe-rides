'use strict';

describe('Controller: AssigndrivermodalCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var AssigndrivermodalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AssigndrivermodalCtrl = $controller('AssigndrivermodalCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AssigndrivermodalCtrl.awesomeThings.length).toBe(3);
  });
});
