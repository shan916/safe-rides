'use strict';

describe('Directive: bsActiveLink', function () {

  // load the directive's module
  beforeEach(module('safeRidesWebApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<bs-active-link></bs-active-link>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the bsActiveLink directive');
  }));
});
