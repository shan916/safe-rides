'use strict';

describe('Service: casService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var casService;
  beforeEach(inject(function (_casService_) {
    casService = _casService_;
  }));

  it('should do something', function () {
    expect(!!casService).toBe(true);
  });

});
