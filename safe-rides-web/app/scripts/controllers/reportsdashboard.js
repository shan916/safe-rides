'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ReportsdashboardCtrl
 * @description
 * # ReportsdashboardCtrl
 * Controller of the Reports view
 */
angular.module('safeRidesWebApp')
    .controller('ReportsdashboardCtrl', function (MonthlyStats, authManager, $state, AuthTokenService, Notification) {
        var vm = this;
		vm.userSelected = undefined;
		vm.isBarChartGenerated = false;
		vm.barChartOptions = ['Total Requests', 'Total Distance', 'Total Completion Time', 'Total Campus - Pickup location',
							'Average Distance', 'Average Completion Time', 'Average Campus - Pickup location'];
		google.charts.load('current', {'packages':['table', 'bar']});
		google.charts.setOnLoadCallback(drawTable);

		vm.monthTableTest = [
				//date, Total Requests, Distance, Completion Time, Campus to Pickup location,
				//    Average distance, Completion time, campus to pickup locaiton
				[new Date(2017, 1, 1), 26, 4, 39, 98, 10, 54, 76], //index [0][0]
				[new Date(2017, 1, 2), 21, 4, 39, 98, 77, 54, 76],
				[new Date(2017, 1, 3), 22, 4, 39, 98, 70, 54, 70],
				[new Date(2017, 1, 4), 26, 4, 39, 98, 74, 54, 76],
				[new Date(2017, 1, 5), 21, 4, 39, 98, 73, 54, 76],
				[new Date(2017, 1, 6), 22, 4, 39, 98, 72, 54, 70],
				[new Date(2017, 1, 7), 26, 4, 39, 98, 71, 54, 76],
				[new Date(2017, 1, 8), 21, 4, 39, 98, 75, 54, 76],
				[new Date(2017, 1, 9), 22, 4, 39, 98, 77, 54, 70],
				[new Date(2017, 1, 10), 26, 4, 39, 98, 67, 54, 76],
				[new Date(2017, 1, 11), 21, 4, 39, 98, 66, 54, 76],
				[new Date(2017, 1, 12), 22, 4, 39, 98, 64, 54, 70],
				[new Date(2017, 1, 13), 26, 4, 39, 98, 65, 54, 76],
				[new Date(2017, 1, 14), 21, 4, 39, 98, 69, 54, 76],
				[new Date(2017, 1, 15), 21, 4, 39, 98, 69, 54, 76],
				[new Date(2017, 1, 16), 22, 4, 39, 98, 7, 54, 70]
		];

		/********************************************************
								Sortable Table
		*********************************************************/
		function drawTable() {
		        var data = new google.visualization.DataTable();
				data.addColumn('date', 'Date');
				data.addColumn('number', 'Total Requests');
				data.addColumn('number', 'Total Distance');
				data.addColumn('number', 'Total Completion Time');
				data.addColumn('number', 'Total Campus - Pickup location');

				data.addColumn('number', 'Avg Distance');
				data.addColumn('number', 'Avg Completion Time');
				data.addColumn('number', 'Avg Campus - Pickup location');

				var numRows = vm.monthTableTest.length;
				for(var i = 0; i < numRows;i++) {
					data.addRow(vm.monthTableTest[i]);
				}

		        var table = new google.visualization.Table(document.getElementById('monthly_table'));

		        table.draw(data, {showRowNumber: false, width: '100%', height: '100%'});
		}


		/********************************************************
						Bar Chart
		*********************************************************/
		vm.showBarChart = function() {
			vm.test =[{date: undefined}, {number: undefined}];

			var data = new google.visualization.DataTable();
			data.addColumn('date', 'Date');
			data.addColumn('number', 'Total Requests');

			var numRows = vm.monthTableTest.length;

			if(vm.userSelected === vm.barChartOptions[0]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][1];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}
			} else if (vm.userSelected === vm.barChartOptions[1]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][2];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else if (vm.userSelected === vm.barChartOptions[2]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][3];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else if (vm.userSelected === vm.barChartOptions[3]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][4];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else if (vm.userSelected === vm.barChartOptions[4]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][5];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else if (vm.userSelected === vm.barChartOptions[5]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][6];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else if (vm.userSelected === vm.barChartOptions[6]) {
				for(var i = 0; i < numRows;i++) {
					vm.test.date = vm.monthTableTest[i][0];
					vm.test.number = vm.monthTableTest[i][7];
					data.addRow([vm.test.date, vm.test.number]);
					//console.log('vm.test.date =:  '+vm.test.date+'   vm.test.number =:  '+vm.test.number);
				}

			} else {
				console.log('nothing selected OR the selection does not match');
			}


			var options = {
	          chart: {
	            title: vm.userSelected,
	            subtitle: 'This is a subtitle for '+vm.userSelected,
	          }
			};



			var chart = new google.charts.Bar(document.getElementById('monthly_barchart'));

	        chart.draw(data, google.charts.Bar.convertOptions(options));
		};

    });
