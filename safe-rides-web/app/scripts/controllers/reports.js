'use strict';

/**
 * @ngdoc function
 * @name safeRidesWebApp.controller:ReportsdashboardCtrl
 * @description
 * # ReportsdashboardCtrl
 * Controller of the Reports view
 */
angular.module('safeRidesWebApp')
    .controller('ReportsdashboardCtrl', function (ReportsService, $log, Notification) {
        var vm = this;
        vm.startDate = moment().subtract(1, 'months').format('YYYY-MM-DD');
        vm.endDate = moment().format('YYYY-MM-DD');
        vm.reportData = [];
        vm.minDate = vm.endDate;
        vm.maxDate = vm.startDate;
        vm.totalFulfilled = 0;
        vm.totalDistance = 0;
        vm.totalRiders = 0;
        vm.loadingData = false;

        vm.days = [];
        vm.fastestATime = [];
        vm.averageATime = [];
        vm.medianATime = [];
        vm.slowestATime = [];
        vm.shortestDistance = [];
        vm.averageDistance = [];
        vm.medianDistance = [];
        vm.furthestDistance = [];
        vm.fastestFTime = [];
        vm.averageFTime = [];
        vm.medianFTime = [];
        vm.slowestFTime = [];
        vm.riders = [];
        vm.fulfilled = [];
        vm.cancelled = [];

        vm.selectedChartReportType = '1';
        vm.chartData = [];
        vm.chartLabels = [];
        vm.chartSeries = [];

        vm.generate = function () {
            getReportData();
        };

        var getReportData = function () {
            // get tabular data and get overview
            vm.totalFulfilled = 0;
            vm.totalDistance = 0;
            vm.totalRiders = 0;
            vm.loadingData = true;
            ReportsService.query({beginDate: vm.startDate, endDate: vm.endDate}).$promise.then(
                function (response) {
                    $log.debug(response);
                    vm.reportData = response;
                    updateGraphs();
                    vm.loadingData = false;
                }, function (error) {
                    $log.debug(error);
                    Notification.error({
                        message: 'An error occurred while retrieving the report data.',
                        positionX: 'center',
                        delay: 10000,
                        replaceMessage: true
                    });
                    vm.loadingData = false;
                });
        };

        getReportData();

        var updateGraphs = function () {
            vm.reportData.forEach(function (el) {
                if (moment(vm.minDate) > moment(el.dateAggregated[0] + '-' + el.dateAggregated[1] + '-' + el.dateAggregated[2])) {
                    vm.minDate = moment(el.dateAggregated[0] + '-' + el.dateAggregated[1] + '-' + el.dateAggregated[2]).toISOString();

                }
                if (moment(vm.maxDate) < moment(el.dateAggregated[0] + '-' + el.dateAggregated[1] + '-' + el.dateAggregated[2])) {
                    vm.maxDate = moment(el.dateAggregated[0] + '-' + el.dateAggregated[1] + '-' + el.dateAggregated[2]).toISOString();
                }

                vm.totalFulfilled += el.requestsFulfilled;
                vm.totalDistance += el.averageDistance;
                vm.totalRiders += el.riders;

                vm.days.push(el.dateAggregated[0] + '-' + el.dateAggregated[1] + '-' + el.dateAggregated[2]);

                vm.fastestATime.push(el.fastestTimeToAssignment);
                vm.averageATime.push(el.averageTimeToAssignment);
                vm.medianATime.push(el.medianTimeToAssignment);
                vm.slowestATime.push(el.slowestTimeToAssignment);

                vm.shortestDistance.push(el.shortestDistance);
                vm.averageDistance.push(el.averageDistance);
                vm.medianDistance.push(el.medianDistance);
                vm.furthestDistance.push(el.longestDistance);

                vm.fastestFTime.push(el.fastestFulfillmentTime);
                vm.averageFTime.push(el.averageFulfillmentTime);
                vm.medianFTime.push(el.medianFulfillmentTime);
                vm.slowestFTime.push(el.slowestFulfillmentTime);

                vm.riders.push(el.riders);
                vm.fulfilled.push(el.requestsFulfilled);
                vm.cancelled.push(el.requestsCanceled);
            });

            vm.aTimeData = [
                vm.fastestATime,
                vm.averageATime,
                vm.medianATime,
                vm.slowestATime
            ];

            vm.distanceData = [
                vm.shortestDistance,
                vm.averageDistance,
                vm.medianDistance,
                vm.furthestDistance
            ];

            vm.fTimeData = [
                vm.fastestFTime,
                vm.averageFTime,
                vm.medianFTime,
                vm.slowestFTime
            ];

            vm.requestData = [
                vm.riders,
                vm.fulfilled,
                vm.cancelled
            ];
        };

        vm.chartReportTypeChanged = function () {
            switch (vm.selectedChartReportType) {
                case '1':
                    vm.chartTitle = 'Time to Assignment';
                    vm.chartData = vm.aTimeData;
                    vm.chartLabels = vm.days;
                    vm.chartSeries = vm.timeSeries;
                    break;
                case '2':
                    vm.chartTitle = 'Total Distance for a Request';
                    vm.chartData = vm.distanceData;
                    vm.chartLabels = vm.days;
                    vm.chartSeries = vm.distanceSeries;
                    break;
                case '3':
                    vm.chartTitle = 'Time to Fulfillment';
                    vm.chartData = vm.fTimeData;
                    vm.chartLabels = vm.days;
                    vm.chartSeries = vm.timeSeries;
                    break;
                case '4':
                    vm.chartTitle = 'Requests';
                    vm.chartData = vm.requestData;
                    vm.chartLabels = vm.days;
                    vm.chartSeries = vm.requestSeries;
                    break;
                default:
                    vm.chartData = [];
                    vm.chartLabels = [];
                    vm.chartSeries = [];
            }
        };

        vm.timeSeries = ['Fastest', 'Average', 'Median', 'Slowest'];
        vm.distanceSeries = ['Shortest', 'Average', 'Median', 'Furthest'];
        vm.requestSeries = ['Riders', 'Fulfilled', 'Canceled'];

        vm.datasetOverride = [{yAxisID: 'y-axis-1'}];
        vm.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    }
                ]
            }
        };

        vm.aggregateData = function () {
            ReportsService.save().$promise.then(function () {
                    Notification.success({
                        message: 'Data aggregated',
                        positionX: 'center',
                        delay: 10000,
                        replaceMessage: true
                    });
                }, function (error) {
                    Notification.error({
                        message: error.data.message,
                        positionX: 'center',
                        delay: 10000,
                        replaceMessage: true
                    });
                }
            );
        };
    });
