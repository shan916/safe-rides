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
    'ngMap',
    'ui.bootstrap',
    'ui.select'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'ctrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'ctrl'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'ctrl'
      })
      .when('/resetpasswordrequest', {
        templateUrl: 'views/resetpasswordrequest.html',
        controller: 'ResetpasswordrequestCtrl',
        controllerAs: 'ctrl'
      })
      .when('/resetpassword', {
        templateUrl: 'views/resetpassword.html',
        controller: 'ResetpasswordCtrl',
        controllerAs: 'ctrl'
      })
      .when('/coordinatordashboard', {
        templateUrl: 'views/coordinatordashboard.html',
        controller: 'CoordinatordashboardCtrl',
        controllerAs: 'ctrl'
      })
      .when('/coordinatorreport', {
        templateUrl: 'views/coordinatorreport.html',
        controller: 'CoordinatorreportCtrl',
        controllerAs: 'ctrl'
      })
      .when('/editdriver/:driverId?', {
        templateUrl: 'views/editdriver.html',
        controller: 'EditdriverCtrl',
        controllerAs: 'ctrl'
      })
      .when('/driverdashboard', {
        templateUrl: 'views/driverdashboard.html',
        controller: 'DriverdashboardCtrl',
        controllerAs: 'ctrl'
      })
      .when('/riderdashboard', {
        templateUrl: 'views/riderdashboard.html',
        controller: 'RiderdashboardCtrl',
        controllerAs: 'ctrl'
      })
      .when('/managedrivers', {
          templateUrl: 'views/managedrivers.html',
          controller: 'ManagedriversCtrl',
          controllerAs: 'ctrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
