'use strict';

describe('Service: AuthTokenService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var AuthTokenService;
  beforeEach(inject(function (_AuthTokenService_) {
    AuthTokenService = _AuthTokenService_;
  }));

  it('should do something', function () {
    expect(!!AuthTokenService).toBe(true);
  });

});
