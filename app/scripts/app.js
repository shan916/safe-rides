'use strict';

/**
 * @ngdoc overview
 * @name safeRidesWebApp
 * @description
 * # safeRidesWebApp
 *
 * Main module of the application.
 */
angular
  .module('safeRidesWebApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngMap'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .when('/resetpasswordrequest', {
        templateUrl: 'views/resetpasswordrequest.html',
        controller: 'ResetpasswordrequestCtrl',
        controllerAs: 'resetPasswordRequest'
      })
      .when('/resetpassword', {
        templateUrl: 'views/resetpassword.html',
        controller: 'ResetpasswordCtrl',
        controllerAs: 'resetPassword'
      })
      .when('/coordinatordashboard', {
        templateUrl: 'views/coordinatordashboard.html',
        controller: 'CoordinatordashboardCtrl',
        controllerAs: 'coordinatordashboard'
      })
      .when('/coordinatorreport', {
        templateUrl: 'views/coordinatorreport.html',
        controller: 'CoordinatorreportCtrl',
        controllerAs: 'coordinatorreport'
      })
      .when('/registerdriver', {
        templateUrl: 'views/registerdriver.html',
        controller: 'RegisterdriverCtrl',
        controllerAs: 'registerdriver'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
