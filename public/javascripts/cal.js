$(document).ready(function() {

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	
	var calendar = $('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		selectable: true,
		selectHelper: true,
		select: function(start, end, allDay, e) {
            $('#addPatientModal').modal({top: 'center'});
            e.preventDefault();
            var firstName = $('#addPatientModal input[name=first_name]').attr("value")
            var middleName = $('#addPatientModal input[name=middle_name]').attr("value")
            var lastName = $('#addPatientModal input[name=last_name]').attr("value")
			var title = lastName+", "+firstName+" "+middleName;
			if (title) {
				calendar.fullCalendar('renderEvent',
					{
						title: title,
						start: start,
						end: end,
						allDay: allDay
					},
					true // make the event "stick"
				);
			}
			calendar.fullCalendar('unselect');
		},
		editable: true
	});
});
