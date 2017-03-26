'use strict';

describe('Service: userRegistrationService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var userRegistrationService;
  beforeEach(inject(function (_userRegistrationService_) {
    userRegistrationService = _userRegistrationService_;
  }));

  it('should do something', function () {
    expect(!!userRegistrationService).toBe(true);
  });

});
