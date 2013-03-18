$(function() {
    /*Dentist Tools Dialog*/
    $( "#dentistTools" ).dialog({
        autoOpen: false
    }).dialog('option', 'position', [500,500]);

    $( "#accordion" ).accordion({
        autoHeight: false,
        clearStyle: true
    });
    $( "button" ).button();

    $('#dental_services button').each(function() {
        $(this).css('background-image', 'url()');
        $(this).css('background-color', $(this).attr('data-color'));
        $(this).css('color', '#FFF');
        $(this).css('text-shadow', 'rgba(0, 0, 0, 0.796875) 0px 1px 4px, rgba(255, 255, 255, 0.296875) 0px 0px 10px');
    });

    //selecting dental service
    $("#dental_services button").click( function() {
        flag = 0;
        var $this = $(this);
        var $id = $this.attr('data-id');
        var $toolType = $this.attr('data-type');
        var $price = $this.attr('data-price');
        var $name = $this.attr('name');
        serviceName = $this.attr('name');
        var $imageTemplate = $this.attr('data-image-template');
        $('#dentistTools').find('.dental-services-details.ui-box input[name=price]').val($price);
        $('#dentistTools').find('.dental-service-name').html($name);

        //populate services in dentist tool dialog box
        //retrieval of dental services
        bannedServices = [];
        $.getJSON("/json/dental_services/banned/"+$id,
            function(data){
                $.each(data, function(key, value){
                    $.each(value, function(ky, vl){
                        bannedServices.push(vl);
                    })
                })
        });

        //console.log(bannedServices); print the banned services

        if ($toolType === "1" && $id != "ERASER") {
            toolType = "paint";
            toolData = $id;
            toolImageTemplate = $imageTemplate;
            curColor = $this.attr("data-color"); //get the color if paint
        } else if ($toolType === "2") {
            toolType = "symbol";
            toolData = $id;
            curColor = $this.attr("data-color");
            toolImageTemplate = $imageTemplate;
        } else if ($toolType === "3") {
            toolType = "selective";
            toolData = $id;
            curColor = $this.attr("data-color");
            toolImageTemplate = $imageTemplate;
        } else if ($toolType === "4") {
            toolType = "inclusive";
            toolData = $id;
            curColor = $this.attr("data-color");
            toolImageTemplate = $imageTemplate;
            console.log("you selected inclusive");
        }

        //if eraser is selected
        if ($this.attr('id') === "ERASER") {
            curColor = $this.attr("data-color");
            curTool = "eraser";
            console.log("curTool: "+curTool+" for "+toolData); //output this log
        };

    });

    //for pushing and removing items in array
    $('.gum input[type=checkbox], .gum2 input[type=checkbox]').click(function() {
        if($(this).attr("checked") === "checked"){
            var tooth = $(this).parent().attr('id');
            $('.selected-tooth').append('<button id="'+tooth+'"class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false" style="margin-right: 5px;"><span class="ui-button-text">'+tooth+'</span></button>');
            var index = $.inArray(tooth, curTooth);
            (index === -1 )? curTooth.push(tooth): console.log("nag else line 78");
            if(tooth === "UPA"){
                for(var i=0;i<UPA.length;i++){
                    var t = UPA[i];
                    var ot = otherTooth(UPA[i]);
                    var idx = $.inArray(t, curTooth);
                    var idx2 = $.inArray(ot, curTooth);
                    (idx === -1 )? curTooth.push(t) : "";
                    (idx2 === -1 )? curTooth.push(ot) : "";
                }
            } else if(tooth === "LOWA"){
                for(var i=0;i<LOWA.length;i++){
                    var t = LOWA[i];
                    var ot = otherTooth(LOWA[i]);
                    var idx = $.inArray(t, curTooth);
                    var idx2 = $.inArray(ot, curTooth);
                    (idx === -1 )? curTooth.push(t) : "";
                    (idx2 === -1 )? curTooth.push(ot) : "";
                }
            }
            console.log("if "+curTooth);
        } else {
            var tooth = $(this).parent().attr('id');
            $('.selected-tooth button#'+tooth).remove();
            var index = $.inArray(tooth, curTooth);
            curTooth.remove(index);
            if(tooth === "UPA"){
                for(var i=0;i<UPA.length;i++){
                    var t = UPA[i];
                    var ot = otherTooth(UPA[i]);
                    curTooth.remove($.inArray(t, curTooth));
                    curTooth.remove($.inArray(ot, curTooth));
                }
            } else if(tooth === "LOWA"){
                for(var i=0;i<LOWA.length;i++){
                    var t = LOWA[i];
                    var ot = otherTooth(LOWA[i]);
                    curTooth.remove($.inArray(t, curTooth));
                    curTooth.remove($.inArray(ot, curTooth));
                }
            }
            console.log("else "+curTooth);
        }
    });
});

//hide/show of dentist tools dialog
$('#dentistToolsButton').click(function(){
    $('.ui-dialog.ui-widget.ui-widget-content.ui-corner-all.ui-draggable.ui-resizable').show("fold");
});

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

//teeth-chart
var imageWidth;
var imageHeight;

var $tempCanvas, $gum = $('.gum'), service="", $id, $tooth, maskDataUrl;
var ctx, tempCtx, maskCtx, outlineCtx;
var canvas, tempCanvas, maskCanvas;
var tooth, toolType, toolData, toolImageTemplate;
var imageObj2, curPrice;

var anotherTooth;//if MA then FA, vice versa

var dentalServices = new Array();
var toothWithService = new Array();
var toothWithServiceFromDB = new Array();
var bannedServices = new Array(); //['PASTA', 'OP'] for EXT
var curTooth = new Array();
var curService = new Array();
var toothRegion = []; //will contain tooth of the selected region, if click ung upper, lahat ng upper from FA - M...
var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var clickColor = new Array();
var clickTool = new Array();
var curTool = 'crayon';
var curColor;
var serviceName;
var paint;
var ex;
//var UPA = ['F1','F2','F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12', 'F13', 'F14', 'F15', 'F16']; //must come from db via ajax call
//var LOWA = ['F17','F18','F19', 'F20', 'F21', 'F22', 'F23', 'F24', 'F25', 'F26', 'F27', 'F28', 'F29', 'F30', 'F31', 'F32'];

function setVariables(tooth, toolType, toolData) {
    //dito may error dapat ifix to, ang nanyayare ay if wala pang canvasFA_EXT or shit like dat, sinesave nya muna ung paint area...

    var t = tooth+"_"+toolData;
    $tempCanvas = $("#tempCanvas"+tooth);
    //error handler if clicked again
    if (document.getElementById("canvas"+t) != null) {
        canvas = document.getElementById("canvas"+t);
    } else {
        canvas = document.getElementById("tempCanvas"+tooth);
    };
    tempCanvas = document.getElementById("tempCanvas"+tooth);
    maskCanvas = document.getElementById("maskCanvas"+tooth);
    ctx = canvas.getContext('2d');
    tempCtx = tempCanvas.getContext('2d');
    maskCtx = maskCanvas.getContext('2d');
    //console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ang bagong susundin na "+canvas.id);
};

var imageDataUrl;

//drawing the outline of the teeth
drawFAOutline();
drawFBOutline();
drawFCOutline();
drawFDOutline();
drawFEOutline();
drawFFOutline();
drawFGOutline();
drawFHOutline();
drawFIOutline();
drawFJOutline();

drawMAOutline();
drawMBOutline();
drawMCOutline();
drawMDOutline();
drawMEOutline();
drawMFOutline();
drawMGOutline();
drawMHOutline();
drawMIOutline();
drawMJOutline();

drawFKOutline();
drawFLOutline();
drawFMOutline();
drawFNOutline();
drawFOOutline();
drawFPOutline();
drawFQOutline();
drawFROutline();
drawFSOutline();
drawFTOutline();

drawMKOutline();
drawMLOutline();
drawMMOutline();
drawMNOutline();
drawMOOutline();
drawMPOutline();
drawMQOutline();
drawMROutline();
drawMSOutline();
drawMTOutline();

drawF1Outline();
drawF2Outline();
drawF3Outline();
drawF4Outline();
drawF5Outline();
drawF6Outline();
drawF7Outline();
drawF8Outline();
drawF9Outline();
drawF10Outline();
drawF11Outline();
drawF12Outline();
drawF13Outline();
drawF14Outline();
drawF15Outline();
drawF16Outline();

drawM1Outline();
drawM2Outline();
drawM3Outline();
drawM4Outline();
drawM5Outline();
drawM6Outline();
drawM7Outline();
drawM8Outline();
drawM9Outline();
drawM10Outline();
drawM11Outline();
drawM12Outline();
drawM13Outline();
drawM14Outline();
drawM15Outline();
drawM16Outline();

drawM17Outline();
drawM18Outline();
drawM19Outline();
drawM20Outline();
drawM21Outline();
drawM22Outline();
drawM23Outline();
drawM24Outline();
drawM25Outline();
drawM26Outline();
drawM27Outline();
drawM28Outline();
drawM29Outline();
drawM30Outline();
drawM31Outline();
drawM32Outline();

drawF17Outline();
drawF18Outline();
drawF19Outline();
drawF20Outline();
drawF21Outline();
drawF22Outline();
drawF23Outline();
drawF24Outline();
drawF25Outline();
drawF26Outline();
drawF27Outline();
drawF28Outline();
drawF29Outline();
drawF30Outline();
drawF31Outline();
drawF32Outline();

//function to repaint the area just by entering the position
//function to save the previously painted teeth area
function loadPaint() {
    var imageObj = new Image();
    imageObj.onload = function() {
        ctx.drawImage(imageObj, 0, 0);

        var imageData = ctx.getImageData(0,0, imageWidth, imageHeight);
        var pixel = imageData.data;

        // if white then change alpha to 0  for the eraser
        var r=0, g=1, b=2,a=3;
        for (var p = 0; p<pixel.length; p+=4)
        {
          if (pixel[p+r] == 255 && pixel[p+g] == 255 && pixel[p+b] == 255){
            pixel[p+a] = 0;
          }
        }
        ctx.putImageData(imageData,0,0);
    }
    imageObj.src = tempImageDataUrl;

    if($.inArray(tooth+"_"+toolData, toothWithService) <= -1){
        toothWithService.push(tooth+"_"+toolData); //end of paint
    }
};

$('#clearButton').click(function() {
    clear();
});

function clearPaint(tooth) {
    tempCanvas.width = tempCanvas.width;
    maskCanvas.width = maskCanvas.width;
    clickX = new Array();
    clickY = new Array();
    clickDrag = new Array();
    clickColor = new Array();
    drawMask(tooth);
};

function drawMask(tooth) {
    maskCtx.fillStyle = '#fff';
    maskCtx.fillRect(0, 0, maskCanvas.width, maskCanvas.height);
    maskCtx.globalCompositeOperation = 'xor';
    drawToothMask(tooth);
    ctx.drawImage(maskCanvas, 0, 0);
    //maskCtx.globalCompositeOperation = 'source-in';/*maskCtx.save()*/
};

/*Paint and Symbol Function*/
function addClick(x, y, dragging) {
    clickX.push(x);
    clickY.push(y);
    clickDrag.push(dragging);
    if(curTool == "eraser"){
        clickColor.push(curColor);
    } else {
        clickColor.push(curColor);
    }
};

function redraw() {
    $tempCanvas.width = $tempCanvas.width; //Clears the canvas

    tempCtx.lineJoin = "round";
    tempCtx.lineWidth = 10;

    for (var i = 0; i < clickX.length; i++) {
        tempCtx.beginPath();
        if (clickDrag[i] && i) {
            tempCtx.moveTo(clickX[i-2], clickY[i-2]);
        } else {
            tempCtx.moveTo(clickX[i]-2, clickY[i]);
        }
        tempCtx.lineTo(clickX[i], clickY[i]);
        tempCtx.closePath();
        tempCtx.strokeStyle = clickColor[i];
        tempCtx.stroke();
    }
    tempImageDataUrl = tempCanvas.toDataURL(); ///KABOOOM!
};

function redefineFunctions() {
    $tempCanvas.mousedown(function(e){
        if(toolType === 'paint' && ($.inArray(tooth, curTooth) > -1) && checkIfNotBan(tooth)){
            //console.log('mousedown'+toolData);
            var mouseX = e.pageX - this.offsetLeft;
            var mouseY = e.pageY - this.offsetTop;
            paint = true;
            addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
            redraw();
        } else if(toolType === 'inclusive'){
            //alert(tooth);
            setInclusive(tooth, toolType, toolData, serviceName);
        } else if((toolType === 'symbol' || toolType === 'selective') && ($.inArray(tooth, curTooth) > -1) && checkIfNotBan(tooth)){
            var pair = [];
            if(toolType === 'symbol'){
                setSymbol(tooth, toolType, toolData);
                setSymbol(otherTooth(tooth), toolType, toolData);
                var cv = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
                var cv2 = '#'+otherTooth(tooth)+' div #canvas'+anotherTooth+'_'+toolData;
                pair = [cv, cv2];
            } else if(toolType === 'selective'){
                if($('#UPA input[type=checkbox]').attr("checked") === "checked" && $('#LOWA input[type=checkbox]').attr("checked") === "checked"){
                    setSymbol("ALLA", toolType, toolData);
                }
                if($('#UPA input[type=checkbox]').attr("checked") === "checked"){
                    $.each(UPA, function(k, v){
                        setSymbol(v, toolType, toolData);
                        setSymbol(otherTooth(v), toolType, toolData);
                        var cv = '#'+v+' div #canvas'+v+'_'+toolData;
                        var cv2 = '#'+otherTooth(v)+' div #canvas'+otherTooth(v)+'_'+toolData;
                        if($.inArray(v, pair) === -1 && $.inArray(v, pair) === -1){
                            pair.push(cv);
                            pair.push(cv2);
                        }
                    });
                    setSymbol("UPA", toolType, toolData);
                }
                if($('#LOWA input[type=checkbox]').attr("checked") === "checked"){
                    $.each(LOWA, function(k, v){
                        setSymbol(v, toolType, toolData);
                        setSymbol(otherTooth(v), toolType, toolData);
                        var cv = '#'+v+' div #canvas'+v+'_'+toolData;
                        var cv2 = '#'+otherTooth(v)+' div #canvas'+otherTooth(v)+'_'+toolData;
                        if($.inArray(v, pair) === -1 && $.inArray(v, pair) === -1){
                            pair.push(cv);
                            pair.push(cv2);
                        }
                    });
                    setSymbol("LOWA", toolType, toolData);
                }
                /*}*/
            }

            $.each(pair, function(k, v){
                $(v).each(function() {
                    var id = $(this).attr('id');
                    var c = document.getElementById(id);
                    var ctx = c.getContext("2d");
                    var ctxWidth = parseInt($('#'+c.id).attr('width'));
                    var ctxHeight = parseInt($('#'+c.id).attr('height'));
                    switch(toolImageTemplate){
                        case 'CROSSOUT':   //from db
                            ctx.moveTo(0+6, 0+3);
                            ctx.lineTo(ctxWidth-6, ctxHeight-3);
                            ctx.moveTo(ctxWidth-6, 0+3);
                            ctx.lineTo(0+6, ctxHeight-3);
                            ctx.lineCap = 'round';
                            ctx.lineWidth = 6;
                            ctx.stroke();
                            break;
                        case 'CHAR-A':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();
                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("A", ctxWidth/2, ctxHeight/2);
                            break;
                        case 'CHAR-B':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();
                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("B", ctxWidth/2, ctxHeight/2);
                            break;
                        case 'CHAR-C':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();
                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("C", ctxWidth/2, ctxHeight/2);
                            break;
                        case 'CHAR-D':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();
                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("D", ctxWidth/2, ctxHeight/2);
                            break;
                        default:
                            console.log("No Image Template");
                            break;
                    }
                });
            });
        };
    });

    $tempCanvas.mousemove(function(e) {
        if(toolType === 'paint'){
            if(paint){
                addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
                redraw();
            }
        };
    });

    $tempCanvas.mouseup(function(e) {
        if (toolType === 'paint' && ($.inArray(tooth, curTooth) > -1) && checkIfNotBan(tooth)) {
            //console.log("posibleng i-save sa db x:"+clickX+" at y:"+clickY); //for printing the click Array
            paint = false;
            clearPaint(tooth);
            loadPaint();
        };
    });

    $tempCanvas.mouseleave(function(e) {
        if (toolType === 'paint' && ($.inArray(tooth, curTooth) > -1) && checkIfNotBan(tooth)) {
            paint = false;
            clearPaint(tooth);
        };
    });
};

/*END OF PAINT FUNCTION*/

//selecting the current tooth focus by mouse
$('.gum canvas').hover(function() {
    var $this = $(this);
    tooth = $this.parent().attr('id');
    imageWidth = $this.attr('width');
    imageHeight = $this.attr('height');
    if($.inArray(tooth, curTooth) != -1){
        switch(toolType){
            case 'paint':
                setPaint(tooth, toolType, toolData);
                setVariables(tooth, toolType, toolData);
                redefineFunctions();
                break;
            case 'symbol':
                anotherTooth = otherTooth(tooth);
                setVariables(tooth, toolType, toolData);
                redefineFunctions();
                break;
            case 'selective':
                setVariables(tooth, toolType, toolData);
                redefineFunctions();
                break;
            case 'inclusive':
                setVariables(tooth, toolType, toolData);
                redefineFunctions();
                break;
            default:
                tooth = "";
                console.log("No Tool Selected.");
                break;
        }
    }
});

/*brace type services*/

/*$('#UPA input[type=checkbox]').click(function(){
    if($(this).attr("checked") === "checked"){
        curTooth.push("UPA");
    } else {
        u
    }
});

$('#LOWA input[type=checkbox]').click(function(){
    if($(this).attr("checked") === "checked"){
        for(var i=0;i<LOWA.length;i++){
            var t = LOWA[i];
            var ot = otherTooth(LOWA[i]);
            curTooth.push(t);
            curTooth.push(ot);
        }
    } else { curTooth = []; }
});*/

function checkIfNotBan(tooth){
    //console.log("\n"+tooth);
    for(var i=0; i < bannedServices.length; i++){
        if($.inArray(tooth+'_'+bannedServices[i], toothWithService) > -1){
            flag = 1;
            return false;
        }
    }
    for(var i=0; i < bannedServices.length; i++){
        if($.inArray("canvas"+tooth+'_'+bannedServices[i], toothWithServiceFromDB) > -1){
            flag = 1;
            return false;
        }
    }
    return true;
}

//creating canvas to put paint on
function setPaint(tooth, toolType, toolData) {
    var cvs = 'canvas'+tooth+'_'+toolData;

    if (checkIfNotBan(tooth)) {
        console.log('===========================> setPaint'+tooth);
        var gum = "#"+tooth+".gum";
        var c = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
        var t = tooth+"_"+toolData;
        //price & dentist
        var price = $('#dentistTools').find('.dental-services input[name=price]').val();
        var dentist = $('#dentistTools').find('.dental-services select[name=dentist_id]').val();
        var datePerformed = $('#dentistTools').find('.dental-services input[name=date_performed]').val();

        //check if not-exists ung canvas, if-not exists add div
        if ($(c).length <= 0) {
            $(gum).prepend("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></canvas></div>");
        }
    };
};

//creating canvas to put inclusive on
function setInclusive(tooth, toolType, toolData, serviceName){
    var cvs;
    if($.inArray(tooth, LOWA.concat(UPA)) >= 0){
        cvs = 'canvasALLA'+'_'+toolData;
        tooth = "ALLA";
    }
    if (checkIfNotBan(tooth)) {
        //console.log('===========================> setInclusive'+tooth);
        var gum = "#"+tooth+".gum2";
        var c = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
        var t = tooth+"_"+toolData;

        var price = $('#dentistTools').find('.dental-services input[name=price]').val();
        var dentist = $('#dentistTools').find('.dental-services select[name=dentist_id]').val();
        var datePerformed = $('#dentistTools').find('.dental-services input[name=date_performed]').val();

        if ($(c).length <= 0) {
            var id = tooth+toolData;
            $('#inclusive_dental_services').append('<li><p id="'+id+'" class="center inclusive-dental-service-name">'+serviceName+'</p></li>');
            $(gum).prepend("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></canvas></div>");
            if($.inArray(tooth+"_"+toolData, toothWithService) <= -1){
                toothWithService.push(tooth+"_"+toolData);
            }
        } else {
            var id = tooth+toolData;
            $(c).parent().remove();
            $('#inclusive_dental_services #'+id).remove();
            var t = tooth+"_"+toolData;
            var index = $.inArray(t, toothWithService);
            toothWithService.remove(index);
        }
    }
}

//creating canvas to put symbol on
function setSymbol(tooth, toolType, toolData) {
    var cvs = 'canvas'+tooth+'_'+toolData;

    var group = '#'+tooth+' #canvas'+tooth+'_'+toolData;
    var c = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
    //var c2 = '#'+otherTooth(tooth)+' div #canvas'+otherTooth(tooth)+'_'+toolData;
    var t = tooth+"_"+toolData;

    //price
    var price = $('#dentistTools').find('.dental-services input[name=price]').val();
    var dentist = $('#dentistTools').find('.dental-services select[name=dentist_id]').val();
    var datePerformed = $('#dentistTools').find('.dental-services input[name=date_performed]').val();


    if (checkIfNotBan(tooth) && checkIfNotBan(anotherTooth)) {
        //console.log("==========================> setSymbol: "+tooth);
        if ($(c).length <= 0) {
            width = $('#'+tooth+' canvas').attr('width');
            height = $('#'+tooth+' canvas').attr('height');
            $('#'+tooth+' div').filter(':last').before("<div class='absolute'><canvas id='canvas"+t+"' width='"+width+"' height='"+height+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></div>");

            //special cases
            if(tooth === "UPA" && $(group).length <= 0){
                $('#'+tooth).append("<div class='absolute'><canvas id='canvas"+t+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></div>");
            }

            if(tooth === "LOWA" && $(group).length <= 0){
                $('#'+tooth).append("<div class='absolute'><canvas id='canvas"+t+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></div>");
            }

            if(tooth === "ALLA" && $(group).length <= 0){
                $('#'+tooth).append("<div class='absolute'><canvas id='canvas"+t+"' data-price='"+price+"' data-dentist='"+dentist+"' data-date-performed='"+datePerformed+"'></div>");
            }


            if($.inArray(tooth+"_"+toolData, toothWithService) <= -1){
                toothWithService.push(tooth+"_"+toolData); //end of symbol
            }
        } else {
            $(c).parent().remove();
            var t = tooth+"_"+toolData;
            var index = $.inArray(t, toothWithService);
            toothWithService.remove(index);
            //symbol only has remove from toothWithService
        }
    };
};

function drawTemplate(template, id){
    var c = document.getElementById(id);
    var ctx = c.getContext("2d");
    var ctxWidth = parseInt($('#'+c.id).attr('width'));
    var ctxHeight = parseInt($('#'+c.id).attr('height'));
    switch(template){
        case 'CROSSOUT':   //from db
            ctx.moveTo(0+10, 0+5);
            ctx.lineTo(ctxWidth-10, ctxHeight-5);
            ctx.moveTo(ctxWidth-10, 0+5);
            ctx.lineTo(0+10, ctxHeight-5);
            ctx.lineCap = 'round';
            ctx.lineWidth = 6;
            ctx.stroke();
            break;
        case 'CHAR-B':
            ctx.fillStyle = curColor;
            ctx.stroke();
            ctx.closePath();
            ctx.font = "bold 20px Verdana";
            ctx.textBaseline = "right";
            ctx.fillText("B", ctxWidth/2, ctxHeight/2);
            break;
        case 'CHAR-A':
            ctx.fillStyle = curColor;
            ctx.stroke();
            ctx.closePath();
            ctx.font = "bold 20px Verdana";
            ctx.textBaseline = "right";
            ctx.fillText("A", ctxWidth/2, ctxHeight/2);
            break;
        case 'CHAR-C':
            ctx.fillStyle = curColor;
            ctx.stroke();
            ctx.closePath();
            ctx.font = "bold 20px Verdana";
            ctx.textBaseline = "right";
            ctx.fillText("C", ctxWidth/2, ctxHeight/2);
            break;
        case 'CHAR-D':
            ctx.fillStyle = curColor;
            ctx.stroke();
            ctx.closePath();
            ctx.font = "bold 20px Verdana";
            ctx.textBaseline = "right";
            ctx.fillText("D", ctxWidth/2, ctxHeight/2);
            break;
        default:
            console.log("No Image Template");
            break;
    }
}
