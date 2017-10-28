'use strict';

describe('Service: reportsService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var reportsService;
  beforeEach(inject(function (_reportsService_) {
    reportsService = _reportsService_;
  }));

  it('should do something', function () {
    expect(!!reportsService).toBe(true);
  });

});
