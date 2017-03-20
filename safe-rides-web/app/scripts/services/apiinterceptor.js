'use strict';

/**
 * @ngdoc service
 * @name safeRidesWebApp.APIInterceptor
 * @description
 * # APIInterceptor
 * Factory in the safeRidesWebApp.
 */
angular.module('safeRidesWebApp')
  .factory('APIInterceptor', function ($injector) {
      return {
          request: function(req) {
              // TODO: add JWT token here
              req.headers.Authorization = '';
              return req;
          },
          responseError: function(rejection) {
              if (rejection.status === 401) {
                  $injector.get('$state').go('login');
              }
          }
      };
  });
