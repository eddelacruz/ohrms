$(document).ready(function() {

    /*Calendar*/
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var startVar, endVar, allDayVar;
    var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
    var title, firstName, middleName, lastName, description, dentistName, contactNo, address;
    var exists = false;
    var appointments;
    var appointmentsProperties = ["id", "description", "firstName", "middleName", "lastName", "address", "contactNo", "dateEnd", "dateStart", "dentistId"]


    $.getJSON("/json/appointments",
        function(data){
            //console.log(data["AppointmentList"][0].id); or console.log(data["AppointmentList"][0]["id"]);
            $.each(data, function(key, value){
                $.each(value, function(ky, vl){
                    var s = new Date(Date.parse(vl.dateStart));
                    var e = new Date(Date.parse(vl.dateEnd));
                    var allDay = (s.getHours() === 0) ? true : false;
                    var color, borderColor, textColor;
                    switch(vl.status) {
                    case 1: //pending blue
                        color = '#006dcc';
                        borderColor = '#0044cc';
                        textColor = '#fff';
                        break;
                    case 2: //rescheduled yellow
                        color = '#fbb450';
                        borderColor = '#f89406';
                        textColor = '#fff';
                        break;
                    case 3: //cancelled red
                        color = '#ee5f5b';
                        borderColor = '#bd362f';
                        textColor = '#fff';
                        break;
                    case 4: //completed green
                        color = '#62c462';
                        borderColor = '#51a351';
                        textColor = '#fff';
                        break;
                    default:
                        color = '#006dcc';
                        borderColor = '#0044cc';
                        textColor = '#fff';
                    }
                    $calendar.fullCalendar('addEventSource',
                        [{
                            title: vl.lastName+", "+vl.firstName+" "+vl.middleName+" - "+vl.description,
                            start: new Date(s.getFullYear(), s.getMonth(), s.getDate(), s.getHours(), s.getMinutes()),
                            end: new Date(e.getFullYear(), e.getMonth(), e.getDate(), e.getHours(), e.getMinutes()),
                            allDay: allDay,
                            firstName: vl.firstName,
                            middleName: vl.middleName,
                            lastName: vl.lastName,
                            dentistId: vl.dentistId,
                            description: vl.description,
                            contactNo: vl.contactNo,
                            address: vl.address,
                            status: vl.status,
                            color: color,
                            borderColor: borderColor,
                            textColor: textColor,
                            id: vl.id
                        }]);
                })
            })
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
        description = $('#addAppointmentModal textarea[name=description]').val();
        dentistId = $('#addAppointmentModal input[name=dentist_id]').val(); //TODO select box to dapat so, dapat di sya delete
        contactNo = $('#addAppointmentModal input[name=contact_no]').val();
        address = $('#addAppointmentModal textarea[name=address]').val();

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

        //add if the form was completely filled up check if may entry ung fname, lname, description and dentist
        if(firstName != "" && lastName != "" && description != "" && dentistId != "" && exists === false){
            title = lastName+", "+firstName+" "+middleName+" - "+description;
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
                    description: description,
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
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        selectable: true,
        selectHelper: true,
        select: function(start, end, allDay, e) {
            //fullfill variables
            startVar = start
            endVar = end
            allDayVar = allDay
            if(start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear() && start.getDate() === end.getDate()){
                appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDate()+", "+start.getFullYear()
            } else {
                appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDate()+", "+start.getFullYear()+" to "+monthNames[end.getMonth()]+" "+end.getDate()+","+end.getFullYear()
            }
            //TODO lagyan ng get hour at minutes
            start = start.getFullYear()+"-"+(start.getMonth()+1)+"-"+start.getDate()+" "+start.getHours()+":"+start.getMinutes()+":"+start.getSeconds();
            end = end.getFullYear()+"-"+(end.getMonth()+1)+"-"+end.getDate()+" "+end.getHours()+":"+end.getMinutes()+":"+end.getSeconds();
            $('#appointmentDate').html(appointmentDate);
            $('#addAppointmentModal').modal({top: 'center'});
            $('input[name=date_end]').attr("value", end);
            e.preventDefault();
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
        },
        eventResize: function( event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
            alert("hi");
        },*/
        eventClick: function(calEvent, jsEvent, view) {
            var appointmentId = calEvent.id;
            $.ajax({
                url : "/appointments/"+appointmentId+"/update",
                type : "GET",
                success :
                    function(res) {
                        //alert("saksespul");
                        //window.location.href = "/patients";
                        //alert(appointmentId);
                        //window.location.href = "/appointments/"+appointmentId+"/update"
                        //$('input[name=date_start]').attr("value", start);
                        //$('.main-box').html($(res).find('.main-box').html());
                        //$('#updateAppointmentModal .columns').html($(res).find('.main-box #updateAppointmentModal .columns').html());
                        $('#updateAppointmentModal .box-content').html($(res).find('#updateAppointmentModal .box-content').html());
                        //alert($(res).find('#updateAppointmentModal').html())
                    }
            })
            $('#updateAppointmentModal').modal({top: 'center'});
        },
        editable: true
    });

});
