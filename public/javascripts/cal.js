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

    //show all events  hiuyewhd3519
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
            console.log(start, end);
            if(start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear() && start.getDay() === end.getDay()){
                appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDay()+", "+start.getFullYear()
            } else {
                appointmentDate = "on "+monthNames[start.getMonth()]+" "+start.getDay()+", "+start.getFullYear()+" to "+monthNames[end.getMonth()]+" "+end.getDay()+","+end.getFullYear()
            }

            console.log("selected!");
            $('#appointmentDate').html(appointmentDate);
            $('#addAppointmentModal').modal({top: 'center'});
            e.preventDefault();
        },
        eventDrop: function ( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ){
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
        },
        editable: true
    });

});
