'use strict';

describe('Controller: SystemNotificationCtrl', function () {

  // load the controller's module
  beforeEach(module('safeRidesWebApp'));

  var SystemNotificationCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemNotificationCtrl = $controller('SystemNotificationCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(SystemNotificationCtrl.awesomeThings.length).toBe(3);
  });
});
