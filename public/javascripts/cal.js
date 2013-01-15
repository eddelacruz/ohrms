$(document).ready(function() {

    /*Calendar*/
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var startVar, endVar, allDayVar;
    var title, firstName, middleName, lastName;

    function clearFields(){
        $('#addPatientModal input[name=first_name]').attr("value",'')
        $('#addPatientModal input[name=middle_name]').attr("value",'')
        $('#addPatientModal input[name=last_name]').attr("value",'')
    }

    //TODO Add Client side only for now
    // a non-ajax option    follow-up light blue pending-blue time_rescheduled-dilaw date_rescheduled-orange cancelled-red completed-green
    //element.css('border-color', '#BD362F');
    //element.children('.fc-event, .fc-agenda .fc-event-time, .fc-event a, .fc-event-inner.fc-event-skin').css({'background-color' : '#EE5F5B', 'border-color' : '#BD362F'}).css('text-shadow: 0 1px 0 #BD362F');

    $('#addButton').click(function(e){
        e.preventDefault();
        firstName = $('#addPatientModal input[name=first_name]').attr("value")
        middleName = $('#addPatientModal input[name=middle_name]').attr("value")
        lastName = $('#addPatientModal input[name=last_name]').attr("value")
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
                status: 'pending'
            },
            true
        );
        clearFields();
        $calendar.fullCalendar('option', 'eventColor', '#EE5F5B')
        console.log('added an event!');
    });


    //show all events  hiuyewhd3519
    $('#showEventsButton').click(function(e){
        e.preventDefault();
            $calendar.fullCalendar('clientEvents').map( function(item){
            alert("Appointment for "+item.firstName+item.middleName+item.lastName);
            alert("Appointment for "+item.status);
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
            console.log("selected!");

            $('#addPatientModal').modal({top: 'center'});
            e.preventDefault();
        },
        editable: true
    });

});
