'use strict';

describe('Service: riderequest', function() {

    // load the service's module
    beforeEach(module('safeRidesWebApp'));

    // instantiate service
    var riderequest;
    beforeEach(inject(function(_riderequest_) {
        riderequest = _riderequest_;
    }));

    it('should do something', function() {
        expect(!!riderequest).toBe(true);
    });

});
