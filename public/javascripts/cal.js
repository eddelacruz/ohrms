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
    $('#addButton').click(function(e){
        e.preventDefault();
        firstName = $('#addPatientModal input[name=first_name]').attr("value")
        middleName = $('#addPatientModal input[name=middle_name]').attr("value")
        lastName = $('#addPatientModal input[name=last_name]').attr("value")
        title = lastName+", "+firstName+" "+middleName;
        $calendar.fullCalendar('renderEvent', {
            title: title,
            start: startVar,
            end: endVar,
            allDay: allDayVar
        });
        clearFields();
    });

    //show all events
    $('#showEventsButton').click(function(e){
        e.preventDefault();
        $calendar.fullCalendar('clientEvents').map( function(item) { console.log(item.title, item) } );
        /*for(var i=0; i > $calendar.fullCalendar('clientEvents').length; i++){
            alert($calendar.fullCalendar('clientEvents').title);
        }*/
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
            console.log("leche!");

            $('#addPatientModal').modal({top: 'center'});
            e.preventDefault();
        },
        eventClick: function(calEvent, jsEvent, view) {
            alert('Event: ' + calEvent.title);
            alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
            alert('View: ' + view.name);

            // change the border color just for fun
            $(this).css('border-color', 'red');
        },
        editable: true
    });

});
