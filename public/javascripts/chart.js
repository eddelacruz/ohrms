$(document).ready(function() {

    function(){
        var chart;
        var curYear = 2013;
        var patientVisits;

        $.getJSON("/json/patients/visits/"+curYear,

            function(data){
                patientVisits = data;
        //            console.log(patientVisits);

            console.log(JSON.stringify(patientVisits));

            /*Chart*/
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
                        text: 'January - December '+curYear,
                        x: -20//
                    },

            //description in x-axis

                    xAxis: {
                        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
                    },

            //description in y-axis

                    yAxis: {

                        title: {
                            text: 'Number  of Patient Visits'
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
                        data: patientVisits

                    }]
                });

        });
    }


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
                text: 'from January - December '+curYear
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
                data: [0,4,0,0,0,0,0,0,0,0,0,0]
            }]
        });
    });

});





