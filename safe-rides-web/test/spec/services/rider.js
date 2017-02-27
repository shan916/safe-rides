'use strict';

describe('Service: rider', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var rider;
  beforeEach(inject(function (_rider_) {
    rider = _rider_;
  }));

  it('should do something', function () {
    expect(!!rider).toBe(true);
  });

});
