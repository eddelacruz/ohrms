$(document).ready(function() {

    var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];

    var chart;
    var curYear = new Date().getFullYear();
    var curMonth = new Date().getMonth();
    var monthlyPatientVisits;

    $.getJSON("/json/patients/visits/"+curYear,
        function(data){
            monthlyPatientVisits = data;
    //            console.log(patientVisits);

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
                    text: 'Monthly Patient Visits',
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
                        text: 'Number  of Patient'
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
                    data: monthlyPatientVisits

                }]
            });

    });

    $.getJSON("/json/patients/visits/"+curYear+"/"+(curMonth+1),
        function(data){
            yearlyPatientVisits = data;
            //alert(data);
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container1',
                    type: 'line'
                },
                title: {
                    text: 'Daily Patient Visits'
                },
                subtitle: {
                    text: monthNames[curMonth]+" "+curYear
                },
                xAxis: {
                    min: 1,
                    categories: ['0', '1', '2']
                },
                yAxis: {
                    title: {
                        text: 'Number of Patient'
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
                    data: yearlyPatientVisits
                }]
            });
        }
    )






















    /*Calendar*/
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var startVar, endVar, allDayVar;
    var title, firstName, middleName, lastName, dentalServiceId, dentistName, contactNo, address;
    var exists = false;
    var appointments;
    var appointmentsProperties = ["id", "description", "firstName", "middleName", "lastName", "address", "contactNo", "dateEnd", "dateStart", "dentistId"];

    $.getJSON("/json/appointments",
        function(data){
            //console.log(data["AppointmentList"][0].id); or console.log(data["AppointmentList"][0]["id"]);
            $.each(data, function(key, value){
                $.each(value, function(ky, vl){

                    var s = new Date(Date.parse(vl[0].dateStart));
                    var e = new Date(Date.parse(vl[0].dateEnd));
                    //console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>cal.js line 72"+s)
                    var allDay = (s.getHours() === 0) ? true : false;
                    var color, borderColor, textColor;

                    var thisDay = new Date();

                    if(thisDay.getDay() === s.getDay()){
                        color = '#ee5f5b';
                        borderColor = '#bd362f';
                        textColor = '#fff';
                    } else if(thisDay.getDay() < s.getDay()) {
                        color = '#006dcc';
                        borderColor = '#0044cc';
                        textColor = '#fff';
                    } else {
                        color = '#808080';
                        borderColor = '#505050';
                        textColor = '#fff';
                    }

                    /*switch(s.getDay()) {
                    case 0: //sun
                        color = '#808080';
                        borderColor = '#505050';
                        textColor = '#fff';
                        break;
                    case 1: //pending blue mon

                    case 2: //rescheduled yellow tue
                        color = '#f2891e';
                        borderColor = '#cd6619';
                        textColor = '#fff';
                        break;
                    case 4: //completed green thu
                        color = '#68b360';
                        borderColor = '#51a351';
                        textColor = '#fff';
                        break;
                    case 5: //pending blue fri
                        color = '#cc661b';
                        borderColor = '#b32018';
                        textColor = '#fff';
                        break;
                    case 6: //sat
                        color = '#e63e96';
                        borderColor = '#b3005c';
                        textColor = '#fff';
                        break;
                    default:
                        color = '#006dcc';
                        borderColor = '#0044cc';
                        textColor = '#fff';
                    }*/
                    $calendar.fullCalendar('addEventSource',
                        [{
                            title: vl[0].lastName+", "+vl[0].firstName+" "+vl[0].middleName+" - Dr. "+vl[1].dFirstName+" "+vl[1].dMiddleName+" "+vl[1].dLastName,
                            start: new Date(s.getFullYear(), s.getMonth(), s.getDate(), s.getHours(), s.getMinutes()),
                            end: new Date(e.getFullYear(), e.getMonth(), e.getDate(), e.getHours(), e.getMinutes()),
                            allDay: allDay,
                            firstName: vl[0].firstName,
                            middleName: vl[0].middleName,
                            lastName: vl[0].lastName,
                            dentistId: vl[0].dentistId,
                            dentalServiceId: vl[0].dentalServiceId,
                            contactNo: vl[0].contactNo,
                            address: vl[0].address,
                            status: vl[0].status,
                            color: color,
                            borderColor: borderColor,
                            textColor: textColor,
                            id: vl[0].id,
                            dateEnd: new Date(e.getFullYear(), e.getMonth(), e.getDate(), e.getHours(), e.getMinutes()),
                            dentistName: "Dr. "+vl[1].dFirstName+" "+vl[1].dMiddleName+" "+vl[1].dLastName,
                            dentalServiceName: vl[1].dentalServiceName
                        }]);
                })
            })
            $("#loader").hide(); // ajax loader
    });

    function clearFields(){
        $('#addAppointmentModal input[name=first_name]').attr("value",'');
        $('#addAppointmentModal input[name=middle_name]').attr("value",'');
        $('#addAppointmentModal input[name=last_name]').attr("value",'');
        $('#addAppointmentModal textarea[name=description]').attr("value", '');
        $('#addAppointmentModal input[name=dentist_id]').attr("value", '');
        $('#addAppointmentModal input[name=contact_no]').attr("value", '');
        $('#addAppointmentModal textarea[name=address]').attr("value", '');
    }

    //TODO Add Client side only for now
    // a non-ajax option    follow-up light blue pending-blue time_rescheduled-dilaw date_rescheduled-orange cancelled-red completed-green
    //element.css('border-color', '#BD362F');
    //element.children('.fc-event, .fc-agenda .fc-event-time, .fc-event a, .fc-event-inner.fc-event-skin').css({'background-color' : '#EE5F5B', 'border-color' : '#BD362F'}).css('text-shadow: 0 1px 0 #BD362F');

    $('#addButtonAppointmentModal').click(function(e){
        e.preventDefault();

        firstName =  $('#addAppointmentModal input[name=first_name]').val();
        middleName = $('#addAppointmentModal input[name=middle_name]').val();
        lastName =  $('#addAppointmentModal input[name=last_name]').val();
        dentalServiceId = $('#addAppointmentModal select[name=dental_service_id]').val();
        dentistId = $('#addAppointmentModal input[name=dentist_id]').val(); //TODO select box to dapat so, dapat di sya delete
        contactNo = $('#addAppointmentModal input[name=contact_no]').val();
        address = $('#addAppointmentModal textarea[name=address]').val();

        /*$calendar.fullCalendar('clientEvents').map( function(item){
            //check if may kapareho ng araw and oras and dentists.. if meron
            try {
                if(startVar.getMonth() === item.start.getMonth() && startVar.getFullYear() === item.start.getFullYear() && startVar.getHours() >= item.start.getHours() && endVar.getHours() <= item.end.getHours() && dentistId === item.dentistId){
                   alert("Dentist is busy this time! add after");
                   exists = true;
                } else {
                   exists = false;
                }
            } catch(err) {
                exists = false;
            }
        });*/

        $calendar.fullCalendar('clientEvents').map( function(item){
           /* try {
                if(startVar.getMonth() === item.start.getMonth() && startVar.getFullYear() === item.start.getFullYear() && startVar.getHours() >= item.start.getHours() && endVar.getHours() <= item.end.getHours() && dentistId === item.dentistId){
                   alert("Dentist is busy this time! add after");
                   exists = true;
                } else {
                   exists = false;
                }
            } catch(err) {
                exists = false;
            }
            if(item.start === start && item.end === end){
                alert("pareho");
            }*/
           console.log(item.start);
           console.log("=========================>");
           console.log(e);
        });

        //add if the form was completely filled up check if may entry ung fname, lname, description and dentist
        if(firstName != "" && lastName != "" && dentalService != "" && dentistId != "" && exists === false){
            title = lastName+", "+firstName+" "+middleName;
            $calendar.fullCalendar('renderEvent',
                {
                    title: title,
                    start: startVar,
                    end: endVar,
                    allDay: allDayVar,
                    firstName: firstName,
                    middleName: middleName,
                    lastName: lastName,
                    dentistId: dentistId,
                    dentalServiceId: dentalServiceId,
                    contactNo: contactNo,
                    address: address,
                    status: 'pending'
                },
                true
            );
            clearFields();
            $calendar.fullCalendar('option', 'eventColor', '#EE5F5B')
            console.log('added an event!');
        }

    });

    //show all events
    $('#showEventsButton').click(function(e){
        e.preventDefault();
        $calendar.fullCalendar('clientEvents').map( function(item){
            alert("Appointment for "+item.firstName+item.middleName+item.lastName);
            console.log(item);
        });
    })



    var $calendar = $('#calendar');
    $calendar.fullCalendar({
        minTime: 8,
        maxTime: 18,
        defaultView: 'agendaWeek',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        selectable: true,
        selectHelper: true,
        select: function(start, end, allDay, e) {
            opening = 8
            closing = 18
            var startHour = start.getHours();
            var endHour = end.getHours();
            var dateNow = new Date();
            //dateNow = dateNow

            //alert(opening > startHour);
            //alert(closing +"<"+ endHour);

            /*var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1; //January is 0!

            var yyyy = today.getFullYear();
            if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = mm+'/'+dd+'/'+yyyy;
            document.write(today);*/

            if(startHour >= opening && endHour <= closing && endHour !== 0 && (start >= dateNow)){
                //fullfill variables
                if(end.getHours() !== closing || end.getMinutes() < 30){ //for 5 to 5:30  and 8 to 8:30
                    allDayVar = allDay;
                    if(start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear() && start.getDate() === end.getDate()){
                      appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDate()+", "+start.getFullYear()
                    } else {
                      appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDate()+", "+start.getFullYear()+" to "+monthNames[end.getMonth()]+" "+end.getDate()+","+end.getFullYear()
                    }
                    $('#appointmentDate').html(appointmentDate);
                    $('#addAppointmentModal').modal({top: 'center'});
                    $('input[name=date_start]').attr("value", $.fullCalendar.formatDate(start, 'yyyy-MM-dd HH:mm:ss'));
                    $('input[name=date_end]').attr("value", $.fullCalendar.formatDate(end, 'yyyy-MM-dd HH:mm:ss'));
                    e.preventDefault();
                }
            }
        },
        /*eventDrop: function ( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ){
            $calendar.fullCalendar('clientEvents').map( function(item){
                //check if may kapareho ng araw and oras and dentists.. if meron
                try {
                    if(startVar.getMonth() === item.start.getMonth() && startVar.getFullYear() === item.start.getFullYear() && startVar.getHours() >= item.start.getHours() && endVar.getHours() <= item.end.getHours() && dentistId === item.dentistId){
                       alert("Dentist is busy this time! add after");
                       exists = true;
                    } else {
                       exists = false;
                    }
                } catch(err) {
                    exists = false;
                }
            });
        }, */
        eventClick: function(calEvent, jsEvent, view) {
            var appointmentId = calEvent.id;
            $.ajax({
                url : "/appointments/"+appointmentId+"/update",
                type : "GET",
                beforeSend: function(){
                   $("#loader").show();
                },
                success :
                    function(res) {
                        $("#loader").hide();
                        $('#updateAppointmentModalForm .center').html($(res).find('.main-box #updateAppointmentModal .center').html());
                        $('#updateAppointmentModalForm select[name=dentist_id]').html($(res).find('.main-box #updateAppointmentModal select[name=dentist_id]').html());
                        var defaultDentistId = $(res).find(".main-box #updateAppointmentModal select[name=dentist_id]").attr('data-default');
                        var defaultStatus = $(res).find(".main-box #updateAppointmentModal select[name=status]").attr('data-default');
                        $('#updateAppointmentModalForm select[name=dentist_id] option[value='+defaultDentistId+']').attr('selected','selected')
                        $('#updateAppointmentModalForm select[name=status] option[value='+defaultStatus+']').attr('selected','selected')
                        $('#updateAppointmentModalForm header>.inner>.left.title').html('<h1>Update Appointment for </h1>'+$(res).find('.main-box #updateAppointmentModal header>.inner>.left.title').html());
                        var getDay = new Date($('#updateAppointmentModalForm input[name=date_end]').val());
                        var thisDay = new Date();
                        thisDay = new Date(thisDay.getFullYear()+"-"+thisDay.getMonth()+"-"+thisDay.getDate()+" 00:00:00").toString();
                        getDay = new Date(getDay.getFullYear()+"-"+getDay.getMonth()+"-"+getDay.getDate()+" 00:00:00").toString();
                        //alert(thisDay + getDay);
                        if(thisDay === getDay){
                            $('#add_patient').show();
                        } else {
                            $('#add_patient').hide();
                        }
                    }
            })
            $('#updateAppointmentModalForm').modal({top: 'center'});
        },
        eventResize: function( event, dayDelta, minuteDelta, allDay, revertFunc ){
            var r = confirm("Move this event?");
            if (r == true){
                var json = new Object();

                event.start = $.fullCalendar.formatDate(event.start, 'yyyy-MM-dd HH:mm:ss');
                alert(event.end);
                event.end = $.fullCalendar.formatDate(event.end, 'yyyy-MM-dd HH:mm:ss');

                json.id = event.id;
                json.dental_service_id = event.dentalServiceId;
                json.first_name = event.firstName;
                json.middle_name = event.middleName;
                json.last_name = event.lastName;
                json.dentist_id = event.dentistId;
                json.contact_no =  event.contactNo;
                json.address =  event.address;
                json.status =  2; //rescheduled
                json.date_start =  event.start;
                json.date_end =  event.end;

                console.log(JSON.stringify(json));

                $.ajax({
                  type: "POST",
                  url: "/json/appointments/update",
                  dataType: "json",
                  data: json,
                  /*error: function(xhr, ajaxOptions, thrownError){
                    //alert(xhr.status);
                  },*/
                  beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/j-son;charset=UTF-8");
                    }
                  },
                  success:  $.ajax({
                    type: "GET",
                    url: "/scheduler",
                    success: function(res) {
                        window.location = url;
                    }
                  })
                });
            } else {
                window.location = url;
            };
        },
        eventDrop: function( event, dayDelta, minuteDelta, allDay, revertFunc ) {
            var r = confirm("Move this event?");
            if (r == true){
                var json = new Object();

                event.start = $.fullCalendar.formatDate(event.start, 'yyyy-MM-dd HH:mm:ss');
                event.end = $.fullCalendar.formatDate(event.end, 'yyyy-MM-dd HH:mm:ss');
                alert(event.end);

                json.id = event.id;
                json.dental_service_id = event.dentalServiceId;
                json.first_name = event.firstName;
                json.middle_name = event.middleName;
                json.last_name = event.lastName;
                json.dentist_id = event.dentistId;
                json.contact_no =  event.contactNo;
                json.address =  event.address;
                json.status =  2; //rescheduled
                json.date_start =  event.start;
                json.date_end =  event.end;

                console.log(JSON.stringify(json));

                $.ajax({
                  type: "POST",
                  url: "/json/appointments/update",
                  dataType: "json",
                  data: json,
                  /*error: function(xhr, ajaxOptions, thrownError){
                    //alert(xhr.status);
                  },*/
                  beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/j-son;charset=UTF-8");
                    }
                  },
                  success:  $.ajax({
                    type: "GET",
                    url: "/scheduler",
                    success: function(res) {
                        window.location = url;
                    }
                  })
                });
            } else {
                window.location = url;
            };
        },
        editable: true
    });

});
