'use strict';

describe('Service: settingsService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var settingsService;
  beforeEach(inject(function (_settingsService_) {
    settingsService = _settingsService_;
  }));

  it('should do something', function () {
    expect(!!settingsService).toBe(true);
  });

});
