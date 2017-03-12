'use strict';

describe('Service: riderequestservice', function() {

    // load the service's module
    beforeEach(module('safeRidesWebApp'));

    // instantiate service
    var riderequestservice;
    beforeEach(inject(function(_riderequestservice_) {
        riderequestservice = _riderequestservice_;
    }));

    it('should do something', function() {
        expect(!!riderequestservice).toBe(true);
    });

});
