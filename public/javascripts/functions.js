$(document).ready(function() {
    $('#delete_patient').live("click",
        function(e) {
            var $this = $(this)
			var patientId = $this.attr("data-patient_id")
			e.preventDefault()
			console.log("The Patient Id to be deleted is: "+patientId)
			$.ajax({
			    url : "/patient/delete?id="+patientId,
			    type : "POST",
			    success :
			        function() {
			            console.log("deleted");
			            window.location.href = "/patients";
			        }
			})
		}
	);
});