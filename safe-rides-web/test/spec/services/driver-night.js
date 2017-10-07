'use strict';

describe('Service: driverNight', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var driverNight;
  beforeEach(inject(function (_driverNight_) {
    driverNight = _driverNight_;
  }));

  it('should do something', function () {
    expect(!!driverNight).toBe(true);
  });

});
