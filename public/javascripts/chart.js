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

//Pie Chart


/*
$(function () {
  var chart = new Highcharts.Chart({
      chart: {
          renderTo: 'container2',
          type: 'pie'
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


      plotOptions: {
          pie: {
              borderColor: '#000000'
               }
      },

      series: [{
          data: [
              ['January',   40],
              ['February',       31],
              ['March',       28],
              ['April',    43],
              ['May',    47],
              ['June', 30],
              ['July',   23],
              ['August',       25],
              ['September',       35],
              ['October',    22],
              ['November',    46],
              ['December', 58]


          ]
      }]
  });
});
*/

//Line Graph
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container1',
                type: 'line'
            },
            title: {
                text: 'Monthly Average Patient'
            },
            subtitle: {
                text: 'OHRMS'
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'No. of Patient'
                }
            },
            tooltip: {
                enabled: false,
                formatter: function() {
                    return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'°C';
                }
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            series: [{
                name: 'Patient',
                data: [9.5, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }]
        });
    });

});






