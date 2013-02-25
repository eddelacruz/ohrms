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

        console.log(bannedServices);

        if ($toolType === "1" && $id != "ERASER") {
            toolType = "paint";
            toolData = $id;
            toolImageTemplate = $imageTemplate
            curColor = $this.attr("data-color"); //get the color if paint
        } else if ($toolType === "2") {
            toolType = "symbol";
            toolData = $id;
            curColor = $this.attr("data-color");
            toolImageTemplate = $imageTemplate
        }

        //if eraser is selected
        if ($this.attr('id') === "ERASER") {
            curColor = $this.attr("data-color");
            curTool = "eraser";
            console.log("curTool: "+curTool+" for "+toolData); //output this log
        };

    });

    //for pushing and removing items in array
    $('.gum input[type=checkbox]').click(function() {
        if($(this).attr("checked") === "checked"){
            var tooth = $(this).parent().attr('id');
            $('.selected-tooth').append('<button id="'+tooth+'"class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false" style="margin-right: 5px;"><span class="ui-button-text">'+tooth+'</span></button>');
            curTooth.push(tooth);
            console.log("if "+curTooth);
        } else {
            var tooth = $(this).parent().attr('id');
            $('.selected-tooth button#'+tooth).remove();
            var index = $.inArray(tooth, curTooth);
            curTooth.remove(index);
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
var paint;
var ex;

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
    console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ang bagong susundin na "+canvas.id);
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
        } else if(toolType === 'symbol' && ($.inArray(tooth, curTooth) > -1) && checkIfNotBan(tooth)){
            //console.log('whynot?'+tooth+toolType+toolData);
            setSymbol(tooth, toolType, toolData);
            var cv = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
            var cv2 = '#'+anotherTooth+' div #canvas'+anotherTooth+'_'+toolData;
            var pair = [cv, cv2];

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
                        case 'CHAR':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();

                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("B", ctxWidth/2, ctxHeight/2);
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
            default:
                tooth = "";
                console.log("No Tool Selected.");
                break;
        }
    }
});

function checkIfNotBan(tooth){
    console.log("\n"+tooth);
    for(var i=0; i < bannedServices.length; i++){
        if($.inArray(tooth+'_'+bannedServices[i], toothWithService) > -1){
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

        //check if not-exists ung canvas, if-not exists add div
        if ($(c).length <= 0) {
            $(gum).prepend("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"' data-price='"+price+"' data-dentist='"+dentist+"'></canvas></div>");
        }
    };
};

//creating canvas to put symbol on
function setSymbol(tooth, toolType, toolData) {
    var cvs = 'canvas'+tooth+'_'+toolData;

    //imageWidth for the other tooth
    var imageWidth2 = $('#'+anotherTooth+' canvas').attr('width');
    var imageHeight2 = $('#'+anotherTooth+' canvas').attr('height');

    //console.log("===>toothWithService "+toothWithService);
    var c = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
    var c2 = '#'+anotherTooth+' div #canvas'+anotherTooth+'_'+toolData;
    var t = tooth+"_"+toolData;
    var t2 = anotherTooth+"_"+toolData;

    //price
    var price = $('#dentistTools').find('.dental-services input[name=price]').val();
    var dentist = $('#dentistTools').find('.dental-services select[name=dentist_id]').val();

    if (checkIfNotBan(tooth) && checkIfNotBan(anotherTooth)) {
        console.log("==========================> setSymbol: "+tooth);

        //check if not-exists ung canvas, if-not exists add div
        /*if($('#UPA input[type=checkbox]').attr("checked") === "checked"){
            //toothRegion = ['F1', 'F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12', 'F13', 'F14', 'F15', 'F16', 'F17', 'F18', ,'F19', 'F20', 'F21', 'F22']

            $.getJSON("/json/teeth/type/a/position/upper",
                function(data){
                    $.each(data, function(key, value){
                        toothRegion.push(value);
                    })
            });
            //console.log("===> utut region "+toothRegion);

            $.each(toothRegion, function(k, v){
                if ( $('#'+v+' div #canvas'+v+'_'+toolData).length <= 0 ){
                    var imgWidth = $('#'+v).width();
                    var imgHeight = 130;
                    $('#'+v+' div').filter(':last').before("<div class='absolute'><canvas id='canvas"+v+"_"+toolData+"' width='"+imgWidth+"' height='"+imageHeight+"' data-price=price data-dentist=dentist ></div>");

                    var cv = '#'+v+' div #canvas'+v+'_'+toolData;
                    var id = $(cv).attr('id');
                    var c = document.getElementById(id);
                    var ctx = c.getContext("2d");
                    var ctxWidth = parseInt($('#'+c.id).attr('width'));
                    var ctxHeight = parseInt($('#'+c.id).attr('height'));

                    switch('CHAR'){
                        case 'CROSSOUT':   //from db
                            ctx.moveTo(0+10, 0+5);
                            ctx.lineTo(ctxWidth-10, ctxHeight-5);
                            ctx.moveTo(ctxWidth-10, 0+5);
                            ctx.lineTo(0+10, ctxHeight-5);
                            ctx.lineCap = 'round';
                            ctx.lineWidth = 6;
                            ctx.stroke();
                            break;
                        case 'CHAR':
                            ctx.fillStyle = curColor;
                            ctx.stroke();
                            ctx.closePath();

                            ctx.font = "bold 20px Verdana";
                            ctx.textBaseline = "right";
                            ctx.fillText("B", ctxWidth/2, ctxHeight/2);
                            break;
                        default:
                            console.log("No Image Template");
                            break;
                    }


                }
            })


    } else*/ if ($(c).length <= 0 || $(c2).length <= 0 ) {
        $('#'+tooth+' div').filter(':last').before("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"' data-price='"+price+"' data-dentist='"+dentist+"'></div>");
        $('#'+anotherTooth+' div').filter(':last').before("<div class='absolute'><canvas id='canvas"+t2+"' width='"+imageWidth2+"' height='"+imageHeight2+"' data-price='"+price+"' data-dentist='"+dentist+"'></div>");
        if($.inArray(tooth+"_"+toolData, toothWithService) <= -1 || $.inArray(anotherTooth+"_"+toolData, toothWithService) <= -1){
            toothWithService.push(tooth+"_"+toolData); //end of symbol
            toothWithService.push(anotherTooth+"_"+toolData); //end of symbol
            console.log(">>> toothWithService"+toothWithService);
        }
    } else {
        $(c).parent().remove();
        $(c2).parent().remove();
        var index = $.inArray(tooth, toothWithService);
        var index2 = $.inArray(anotherTooth, toothWithService);
        toothWithService.remove(index);
        toothWithService.remove(index2);
        //symbol only has remove from toothWithService
    }
    };
};

function otherTooth(tooth){
    var word = tooth;
    var len = word.length;
    var fLetter = word.substr(0, 1);
    var rLetters = word.substr(1, len);

    var newWord = (fLetter === 'M') ? 'F' : 'M';
    newWord = newWord + rLetters;

    return newWord;
}

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
        case 'CHAR':
            ctx.fillStyle = curColor;
            ctx.stroke();
            ctx.closePath();

            ctx.font = "bold 20px Verdana";
            ctx.textBaseline = "right";
            ctx.fillText("B", ctxWidth/2, ctxHeight/2);
            break;
        default:
            console.log("No Image Template");
            break;
    }
}
