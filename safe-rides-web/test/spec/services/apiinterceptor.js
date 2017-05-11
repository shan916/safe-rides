'use strict';

describe('Service: APIInterceptor', function () {

  // load the service's module
  beforeEach(module('safeRidesWebApp'));

  // instantiate service
  var APIInterceptor;
  beforeEach(inject(function (_APIInterceptor_) {
    APIInterceptor = _APIInterceptor_;
  }));

  it('should do something', function () {
    expect(!!APIInterceptor).toBe(true);
  });

});
