'use strict';

describe('Service: DriverService', function() {

    // load the service's module
    beforeEach(module('safeRidesWebApp'));

    // instantiate service
    var DriverService;
    beforeEach(inject(function(_DriverService_) {
        DriverService = _DriverService_;
    }));

    it('should do something', function() {
        expect(!!DriverService).toBe(true);
    });

});
