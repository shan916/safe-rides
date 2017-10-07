'use strict';

describe('Controller: ModalDriverNightSummaryCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var ModalDriverNightSummaryCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ModalDriverNightSummaryCtrl = $controller('ModalDriverNightSummaryCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ModalDriverNightSummaryCtrl.awesomeThings.length).toBe(3);
  });
});
