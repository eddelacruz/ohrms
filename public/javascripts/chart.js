$(function () {
var chart;
$(document).ready(function() {
    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            type: 'column',
            plotBackgroundColor: '#ffffff',
            plotBorderColor: '#c5c5c5',
            plotBorderWidth: 2,
            marginRight: 130,
            marginBottom: 25
        },

// title of the charts

        title: {
            text: 'Monthly Average Patient',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: OHRMS',
            x: -20//
        },

//description in x-axis

        xAxis: {
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },

//description in y-axis

        yAxis: {

            title: {
                text: 'NO  of patient'
            },

   //color of gridlines
            gridLineColor: '#c5c5c5',
            gridLineWidth: 2

          },


//charts tooltips

        tooltip: {
            formatter: function() {
                    return '<b>'+ this.series.name +'</b><br/>'+
                    this.x +': '+ this.y +'person';
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },

//different color of the column bars
        plotOptions: {
        series: {
            color: '#09aae5'

        }
    },

//no of patient per months
        series: [  {

            name: 'Patient',
            data: [ 40, 31, 28, 43, 47, 30, 23, 25,35, 22, 46, 58]
        }]
    });
});

});
