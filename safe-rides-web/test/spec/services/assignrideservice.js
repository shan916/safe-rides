'use strict';

describe('Service: assignRideService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var assignRideService;
  beforeEach(inject(function (_assignRideService_) {
    assignRideService = _assignRideService_;
  }));

  it('should do something', function () {
    expect(!!assignRideService).toBe(true);
  });

});
