'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:CoordinatordashboardCtrl
 * @description
 * # CoordinatordashboardCtrl
 * Controller of the safeRidesWebApp
 */
angular.module('safeRidesWebApp')
    .controller('CoordinatordashboardCtrl', function ($scope, $interval) {
        this.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma',
        ];

        /*
        // TODO: Move this to an environment file
        $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE";
        var vm = this;
        vm.positions = [
            [38.55, -121.45], [38.54, -121.44], [38.53, -121.43], [38.52, -121.42]
        ];
        $interval(function () {
            var numMarkers = 4;
            vm.positions = [];
            for (var i = 0; i < numMarkers; i++) {
                var lat = 38.55 + (Math.random() / 100);
                var lng = -121.45 + (Math.random() / 100);
                vm.positions.push([lat, lng]);
            }
        }, 2000);
        */

/****** Modal *******/
/* form validation plugin */
$.fn.goValidate = function() {
    var $form = this,
        $inputs = $form.find('input:text');

    var validators = {
        name: {
            regex: /^[A-Za-z]{3,}$/
        },
        pass: {
            regex: /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/
        },
        email: {
            regex: /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/
        },
        phone: {
            regex: /^[2-9]\d{2}-\d{3}-\d{4}$/,
        }
    };
    var validate = function(klass, value) {
        var isValid = true,
            error = '';

        if (!value && /required/.test(klass)) {
            error = 'This field is required';
            isValid = false;
        } else {
            klass = klass.split(/\s/);
            $.each(klass, function(i, k){
                if (validators[k]) {
                    if (value && !validators[k].regex.test(value)) {
                        isValid = false;
                        error = validators[k].error;
                    }
                }
            });
        }
        return {
            isValid: isValid,
            error: error
        }
    };
    var showError = function($input) {
        var klass = $input.attr('class'),
            value = $input.val(),
            test = validate(klass, value);

        $input.removeClass('invalid');
        $('#form-error').addClass('hide');

        if (!test.isValid) {
            $input.addClass('invalid');

            if(typeof $input.data("shown") == "undefined" || $input.data("shown") == false){
               $input.popover('show');
            }

        }
      else {
        $input.popover('hide');
      }
    };

    $inputs.keyup(function() {
        showError($(this));
    });

    $inputs.on('shown.bs.popover', function () {
  		$(this).data("shown",true);
	});

    $inputs.on('hidden.bs.popover', function () {
  		$(this).data("shown",false);
	});

    $form.submit(function(e) {

        $inputs.each(function() { /* test each input */
        	if ($(this).is('.required') || $(this).hasClass('invalid')) {
            	showError($(this));
        	}
    	});
        if ($form.find('input.invalid').length) { /* form is not valid */
        	e.preventDefault();
            $('#form-error').toggleClass('hide');
        }
    });
    return this;
};
$('form').goValidate();
/************************end modal **/



    });/* end controller*/
