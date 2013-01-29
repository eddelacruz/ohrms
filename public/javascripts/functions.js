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

	$('#delete_services').live("click",
            function(e) {
                var $this = $(this)
    			var serviceId = $this.attr("data-services_id")
    			e.preventDefault()
    			console.log("The Services Id to be deleted is: "+serviceId)
    			$.ajax({
    			    url : "/dental_services/delete?id="+serviceId,
    			    type : "POST",
    			    success :
    			        function() {
    			            console.log("deleted");
    			            window.location.href = "/dental_services";
    			        }
    			})
    		}
    	);

    	$('#delete_announcement').live("click",
                    function(e) {
                        var $this = $(this)
            			var announcementId = $this.attr("data-announcement_id")
            			e.preventDefault()
            			console.log("The announcement Id to be deleted is: "+announcementId)
            			$.ajax({
            			    url : "/announcements/delete?id="+announcementId,
            			    type : "POST",
            			    success :
            			        function() {
            			            console.log("deleted");
            			            window.location.href = "/announcements";
            			        }
            			})
            		}
            	);

    //url static, only for patients
    var count = 1;
    var start = 0;
    var page = 1;
    var filter = "";
    var endpoint = window.location.pathname;
    var endpointsearch = window.location.search.replace("?","&");
    var urls;





    //show # of entries
	$('select[name="DataTables_Table_0_length"]').on( "change",
        function(evt){
            var $this = $(this);
            count = parseInt($this.val());
            start = 0;
            if (endpointsearch.length == 0){
                    urls = endpoint+"?start="+start+"&count="+count
                }
            else {
                    urls = endpoint+"?start="+start+"&count="+count+endpointsearch
            }
            evt.preventDefault();
            console.log(urls);
            console.log(endpointsearch.length);
            $.ajax({
                url : urls,
                type: "GET",
                success:
                    function(res) {
                        $('.table #DataTables_Table_0').html($(res).find('.table #DataTables_Table_0').html());
                        $('#DataTables_Table_0_previous').addClass('paginate_button_disabled');
                        $('#DataTables_Table_0_next').removeClass('paginate_button_disabled');
                        $('span a.paginate_button').html(1);
                    }
            })
            }
            //alert("start "+start+"count "+count);

	);

    //next
	$('#DataTables_Table_0_next').on("click",
        function(evt){
            var $this = $(this);
            count = parseInt($('select[name="DataTables_Table_0_length"]').val());
            evt.preventDefault();
            start+=count;
            $.ajax({
                url : endpoint+"?start="+start+"&count="+count,
                type: "GET",
                success:
                    function(res) {
                        var data = $(res).find('#DataTables_Table_0 tbody tr').length;
                        if(data > 0){
                            $('#DataTables_Table_0_previous').removeClass('paginate_button_disabled');
                            $('#DataTables_Table_0_next').removeClass('paginate_button_disabled');
                            $('.table #DataTables_Table_0').html($(res).find('.table #DataTables_Table_0').html());
                            $('span a.paginate_button').html(page+=1);
                        } else {
                            $('#DataTables_Table_0_next').addClass('paginate_button_disabled');
                            start-=count;
                        };
                    }
            });
            //alert("start "+start+"count "+count);
        }
    );

    //previous
    $('#DataTables_Table_0_previous').on("click",
        function(evt){
            if($('.previous.paginate_button.paginate_button_disabled').length <= 0) {
                var $this = $(this);
                count = parseInt($('select[name="DataTables_Table_0_length"]').val());
                evt.preventDefault();
                start-=count;
                $.ajax({
                    url : endpoint+"?start="+start+"&count="+count,
                    type: "GET",
                    success:
                        function(res) {
                            if(start <= 0){
                                $('#DataTables_Table_0_previous').addClass('paginate_button_disabled');
                                $('#DataTables_Table_0_next').removeClass('paginate_button_disabled');
                            };
                            $('span a.paginate_button').html(page-=1);
                            $('.table #DataTables_Table_0').html($(res).find('.table #DataTables_Table_0').html());
                        }
                });
                //alert("start "+start+"count "+count);
            }
        }
    );

    $('#dentistToolsDialog').click(function() {
      $('.ui-dialog.ui-widget.ui-widget-content.ui-corner-all.ui-draggable.ui-resizable').hide();
      //alert("pumasok");
    });

    /*$('#search_box').keypress(function(e){
        $(this).val()
        console.log(e);
        if(e.keyCode === '13'){
            $.ajax({
                url: endpoint+"?filter="+filter,
                type: "GET",
                success:
                    console.log('success kuno');
            })
        }
    });*/

    //for saving treatment plan
    $('#update_treatment_plan').click(function(e){
        e.preventDefault();
        var myArray = new Array();
        var motherObject = new Object();
        for(var i = 0 ; i<toothWithService.length; i++){
            var a = toothWithService[i];
            var b = a.split("_");
            var myObject = new Object();

            myObject.service_id = b[1];
            myObject.service_price = "200"; //textbox //TODO dynamic
            myObject.date_performed = "2012-1-1 12:12:12"; //select box //TODO dynamic
            myObject.teeth_name = b[0];
            myObject.patient_id = $('.patient_information input[name=id]').val();
            myObject.dentist_id = "71b8ecdd-33c9-4aaf-aa30-9d77419aeb95"; //dropdown //TODO dynamic
            myObject.image =  $("#canvas"+a)[0].toDataURL();
            myArray.push(myObject);

        }
        var json = {Treatment_Plan : myArray};
        //console.log(JSON.stringify(json));

        $.ajax({
          type: "POST",
          url: "/json/treatment_plan",
          dataType: "json",
          data: json,
          error: function(xhr, ajaxOptions, thrownError){
            alert(xhr.status);
            alert(ajaxOptions);
          },
          beforeSend: function(x) {
            if (x && x.overrideMimeType) {
                x.overrideMimeType("application/j-son;charset=UTF-8");
            }
          },
          success:  $.ajax({
            type: "GET",
            url: "/patients/"+myObject.patient_id+"/treatment_plan/update",
            success: function(res) {
                //window.location = url;
                console.log(res);
                $('.grid_11 table').html($(res).find('.grid_11 table').html());
                //$('.mouth.child').html($(res).find('.mouth.child').html());
            }
          })
        });
    });

    //for loading treatment plan
    $.getJSON("/json/treatment_plan",
        function(data){
            //console.log(data["AppointmentList"][0].id); or console.log(data["AppointmentList"][0]["id"]);
            $.each(data, function(key, value){
                $.each(value, function(ky, vl){
                    var tn = $('#'+vl.teethName+' > canvas');
                    var id = "canvas"+vl.teethName+"_"+vl.serviceId;
                    imageWidth = tn.attr("width");
                    imageHeight = tn.attr("height");

                    if(vl.toolType === '1'){
                        $('#'+vl.teethName).prepend("<div class='absolute'><canvas id='"+id+"' width='"+imageWidth+"' height='"+imageHeight+"'></canvas></div>");
                    } else if(vl.toolType === '2') {
                        $('#'+vl.teethName).prepend("<div class='absolute'><canvas id='"+id+"' width='"+imageWidth+"' height='"+imageHeight+"'></canvas></div>");
                    }

                    var myCvs = document.getElementById(id);
                    var myCtx = myCvs.getContext('2d');

                    var imageObj = new Image();
                    imageObj.onload = function() {
                        myCtx.drawImage(imageObj, 0, 0);
                    }
                    imageObj.src = vl.image;
                })
            })
    });
});