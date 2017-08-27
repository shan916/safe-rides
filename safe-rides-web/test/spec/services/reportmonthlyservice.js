'use strict';

describe('Service: ReportMonthlyService', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var ReportMonthlyService;
  beforeEach(inject(function (_ReportMonthlyService_) {
    ReportMonthlyService = _ReportMonthlyService_;
  }));

  it('should do something', function () {
    expect(!!ReportMonthlyService).toBe(true);
  });

});
