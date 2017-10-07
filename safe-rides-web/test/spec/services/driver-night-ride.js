'use strict';

describe('Service: driverNightRide', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var driverNightRide;
  beforeEach(inject(function (_driverNightRide_) {
    driverNightRide = _driverNightRide_;
  }));

  it('should do something', function () {
    expect(!!driverNightRide).toBe(true);
  });

});
