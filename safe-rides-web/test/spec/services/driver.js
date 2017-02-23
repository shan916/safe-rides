'use strict';

describe('Service: driver', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var driver;
  beforeEach(inject(function (_driver_) {
    driver = _driver_;
  }));

  it('should do something', function () {
    expect(!!driver).toBe(true);
  });

});
