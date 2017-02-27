'use strict';

describe('Service: riderservice', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var riderservice;
  beforeEach(inject(function (_riderservice_) {
    riderservice = _riderservice_;
  }));

  it('should do something', function () {
    expect(!!riderservice).toBe(true);
  });

});
