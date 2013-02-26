$(document).ready(function() {
    $('.mouth.child').hide() //hide first the child teeth chart

    /*$('#delete_patient').live("click",
        function(e) {
            var $this = $(this)
			var patientId = $this.attr("data-patient_id")
			e.preventDefault()
			console.log("The Patient Id to be deleted is: "+patientId)
			if(window.confirm("Are you sure you want to delete this record?")) {
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
		}
	);

    $('#delete_dentist').live("click",
        function(e) {
            var $this = $(this)
			var dentistId = $this.attr("data-dentist_id")
			e.preventDefault()
			console.log("The Dentist Id to be deleted is: "+dentistId)
			if(window.confirm("Are you sure you want to delete this record?")) {
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
		}
	);

	$('#delete_staff').live("click",
                function(e) {
                    var $this = $(this)
        			var staffId = $this.attr("data-staff_id")
        			e.preventDefault()
        			console.log("The Staff Id to be deleted is: "+staffId)
        			if(window.confirm("Are you sure you want to delete this record?")) {
                        $.ajax({
                            url : "/staffs/delete?id="+staffId,
                            type : "POST",
                            success :
                                function() {
                                    console.log("deleted");
                                    window.location.href = "/staffs";
                                }
                        })
                    }
        		}
        	);

	$('#delete_services').live("click",
            function(e) {
                var $this = $(this)
    			var serviceId = $this.attr("data-services_id")
    			e.preventDefault()
    			console.log("The Services Id to be deleted is: "+serviceId)
    			if(window.confirm("Are you sure you want to delete this record?")) {
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
    		}
    	);

    	$('#delete_announcement').live("click",
                    function(e) {
                        var $this = $(this)
            			var announcementId = $this.attr("data-announcement_id")
            			e.preventDefault()
            			console.log("The announcement Id to be deleted is: "+announcementId)
            			if(window.confirm("Are you sure you want to delete this record?")) {
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
            		}
            	);

            	$('#delete_specialization').live("click",
                        function(e) {
                            var $this = $(this)
                			var specializationId = $this.attr("data-specialization_id")
                			e.preventDefault()
                			console.log("The Specialization Id to be deleted is: "+specializationId)
                		if(window.confirm("Are you sure you want to delete this record?")) {
                			$.ajax({
                			    url : "/specializations/delete?id="+specializationId,
                			    type : "POST",
                			    success :
                			        function() {
                			            console.log("deleted");
                			            window.location.href = "/specializations";
                			        }
                			})
                		}
                	    }
                	 );*/

    //url static, only for patients
    var count = 1;
    var start = 0;
    var page = 1;
    var filter = "";
    var endpoint = window.location.pathname;
    var endpointsearch = window.location.search.replace("?","&");
    var urls;



    //show # of entries
	$('select[name="DataTables_Table_0_length"]').live( "change",
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
                beforeSend: function(){
                   $("#loader").show();
                },
                success:
                    function(res) {
                        $("#loader").hide();
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
	$('#DataTables_Table_0_next').live("click",
        function(evt){
            var $this = $(this);
            count = parseInt($('select[name="DataTables_Table_0_length"]').val());
            evt.preventDefault();
            start+=count;
            $.ajax({
                url : endpoint+"?start="+start+"&count="+count,
                type: "GET",
                beforeSend: function(){
                   $("#loader").show();
                },
                success:
                    function(res) {
                        $("#loader").hide();
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

    /*$('.bt.blue.right.clinic').click(function(){
        window.location.href = "/clinic";
        alert("Succesfully Added");
    });
*/
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

        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        var h = date.getHours();
        var min = date.getMinutes();
        var s = date.getSeconds();

        for(var i = 0 ; i<toothWithService.length; i++){
            var a = toothWithService[i];
            var b = a.split("_");
            var myObject = new Object();

            myObject.service_id = b[1];
            myObject.service_price = $("#canvas"+b[0]+"_"+b[1]).attr('data-price');
            //myObject.date_performed = y+"-"+(m+1)+"-"+d+" "+h+":"+min+":"+s;
            myObject.date_performed = $("#canvas"+b[0]+"_"+b[1]).attr('data-date-performed');
            myObject.teeth_name = b[0];
            myObject.patient_id = $('.patient_information input[name=id]').val();
            myObject.dentist_id = $("#canvas"+b[0]+"_"+b[1]).attr('data-dentist');
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
                window.location = "/patients/"+myObject.patient_id+"/treatment_plan";
//                console.log(res);
                //$('.grid_11 table').html($(res).find('.grid_11 table').html());
                //$('.mouth.child').html($(res).find('.mouth.child').html());
            }
          })
        });
    });

    //for loading treatment plan
    var patientId = $('.patient_information').find('input[name=id]').val()
    $.getJSON("/json/treatment_plan/"+patientId,
        function(data){
            $.each(data, function(key, value){
                $.each(value, function(ky, vl){
                    //console.log(vl.toolType);
                    var tn = $('#'+vl.teethName+' > canvas');
                    var ot = otherTooth(vl.teethName);
                    var atn = $('#'+ot+' > canvas');
                    var id = "canvas"+vl.teethName+"_"+vl.serviceId+"_"+vl.id;
                    var id2 = "canvas"+ot+"_"+vl.serviceId+"_"+vl.id;
                    imageWidth = tn.attr("width");
                    imageWidth2 = atn.attr("width");
                    imageHeight = tn.attr("height");
                    imageHeight2 = atn.attr("height");

                    toothWithServiceFromDB.push("canvas"+vl.teethName+"_"+vl.serviceId);
                    toothWithServiceFromDB.push("canvas"+ot+"_"+vl.serviceId);

                    if(vl.toolType === '1'){
                        $('#'+vl.teethName).prepend("<div class='absolute'><canvas id='"+id+"' width='"+imageWidth+"' height='"+imageHeight+"' data-service='"+vl.serviceId+"'></canvas></div>");
                        var myCvs = document.getElementById(id);
                        var myCtx = myCvs.getContext('2d');
                        var imageObj = new Image();
                        imageObj.onload = function() {
                            myCtx.drawImage(imageObj, 0, 0);
                        }
                        imageObj.src = vl.image;
                    } else if(vl.toolType === '2') {
                        $('#'+vl.teethName+'>canvas').before("<div class='absolute'><canvas id='"+id+"' width='"+imageWidth+"' height='"+imageHeight+"'></canvas></div>");
                        drawTemplate(vl.imageTemplate, id);
                        $('#'+ot+'>canvas').before("<div class='absolute'><canvas id='"+id2+"' width='"+imageWidth2+"' height='"+imageHeight2+"'></canvas></div>");
                        drawTemplate(vl.imageTemplate, id2);
                    }


                })
            })
    });

    //adult, child button
    $('ul.teeth-type > li').click(function(e){
        var $this = $(this);
        var value = $(this).attr('name');

        e.preventDefault();
        if (value === "adultButton") {
            $this.siblings().removeClass('active');
            $this.addClass('active');
            $('.mouth.adult').show();
            $('.mouth.child').hide();
        } else if (value === "childButton") {
            $this.siblings().removeClass('active');
            $this.addClass('active');
            $('.mouth.child').show();
            $('.mouth.adult').hide();
        };
    });

    //update appointment data
    $('#update_appointment').click(function (e){
        e.preventDefault();
        //var json = new Object();

        //stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery
        var json = "";
        var a = $('.appointment_update_form').serializeArray();
        $.each(a, function(){
            $.each(this, function(i, val){
                if (i=="name") {
                    json += '"' + val + '":';
                } else if (i=="value") {
                    json += '"' + val.replace(/"/g, '\\"') + '",';
                }
            });
        });
        json = jQuery.parseJSON("{" + json.substring(0, json.length - 1) + "}");

        $.ajax({
          type: "POST",
          url: "/json/appointments/update",
          dataType: "json",
          data: json,
          error: function(xhr, ajaxOptions, thrownError){
            alert(xhr.status);
            alert(thrownError);
          },
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
    });





});
