$(document).ready(function() {
    $('#delete_patient').live("click",
        function(e) {
            var $this = $(this)
			var patientId = $this.attr("data-patient_id")
			e.preventDefault()
			console.log("The Patient Id to be deleted is: "+patientId)
			$.ajax({
			    url : "/patients/delete?id="+patientId,
			    type : "POST",
			    success :
			        function() {
			            console.log("deleted");
			            window.location.href = "/patients";
			        }
			})
		}
	);

    $('#delete_dentist').live("click",
        function(e) {
            var $this = $(this)
			var dentistId = $this.attr("data-dentist_id")
			e.preventDefault()
			console.log("The Dentist Id to be deleted is: "+dentistId)
			$.ajax({
			    url : "/dentists/delete?id="+dentistId,
			    type : "POST",
			    success :
			        function() {
			            console.log("deleted");
			            window.location.href = "/dentists";
			        }
			})
		}
	);

});