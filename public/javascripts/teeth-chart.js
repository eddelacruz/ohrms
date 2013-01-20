$(function() {
    $( "#dentistTools" ).dialog({
        autoOpen: false,
        width: 360
    });
    $( "#accordion" ).accordion({
        autoHeight: false,
        clearStyle: true
    });
    $( "button" ).button();
    $('#selectable button').each(function() {
        $(this).css('background-image', 'url()');
        $(this).css('background-color', $(this).attr('data-color'));
        $(this).css('color', '#FFF');
        $(this).css('text-shadow', 'rgba(0, 0, 0, 0.796875) 0px 1px 4px, rgba(255, 255, 255, 0.296875) 0px 0px 10px');
    });
});

//hide/show of dentist tools dialog
$('#dentistToolsButton').click(function(){
    //alert('peekaboo!');
    $('.ui-dialog.ui-widget.ui-widget-content.ui-corner-all.ui-draggable.ui-resizable').show("blind");
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

var tartar = '#CBA735';
var violet = '#cb3594'

var $tempCanvas, $gum = $('.gum'), canvas, tempCanvas, maskCanvas, ctx, tempCtx, maskCtx, outlineCtx, tooth, toolType, toolData, service="", $id, $tooth, listedPaint = new Array("OP", "CVTY", "DCY"), listedSymbol = new Array("EXT"), maskDataUrl;
var imageObj2;


var curTooth = new Array();
var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var clickColor = new Array();
var clickTool = new Array();
var curTool = "crayon";
var curColor;
var paint;


function setVariables(tooth, toolType, toolData) {
    //dito may error dapat ifix to, ang nanyayare ay if wala pang canvasFA_EXT or shit like dat, sinesave nya muna ung paint area...
    //console.log("+++++"+tooth+toolType+toolData);
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
    console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ang bagong susundin na "+maskCanvas.id);
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

//function to repaint the area just by entering the position

//function to save the previously painted teeth area
function loadPaint(tooth) {
    var imageObj = new Image();
    imageObj.onload = function() {
        ctx.drawImage(imageObj, 0, 0);
        var imageData = ctx.getImageData(0,0, imageWidth, imageHeight);
        var pixel = imageData.data;



        // if white then change alpha to 0
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
    /*maskCtx.globalCompositeOperation = 'source-in';
    maskCtx.save()*/
};

/*PAINT FUNCTION*/


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
        if(toolType === 'paint' && ($.inArray(tooth, curTooth) > -1)){
            //console.log('mousedown'+toolData);
            var mouseX = e.pageX - this.offsetLeft;
            var mouseY = e.pageY - this.offsetTop;

            paint = true;
            addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
            redraw();
        } else if(toolType === 'symbol' && ($.inArray(tooth, curTooth) > -1)){
            console.log('whynot?'+tooth+toolType+toolData);
            setSymbol(tooth, toolType, toolData)
            var cv = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
            $(cv).each(function() {
                var id = $(this).attr('id');
                var c=document.getElementById(id);
                var ctx=c.getContext("2d");
                var ctxWidth = parseInt($('#'+c.id).attr('width'));
                var ctxHeight = parseInt($('#'+c.id).attr('height'));
                var ctxZero = 0

                ctx.moveTo(ctxZero+10, ctxZero+5);
                ctx.lineTo(ctxWidth-10, ctxHeight-5);
                ctx.moveTo(ctxWidth-10, ctxZero+5);
                ctx.lineTo(ctxZero+10, ctxHeight-5);
                ctx.lineCap = 'round';
                ctx.lineWidth = 6;
                ctx.stroke();
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
        if (toolType === 'paint' && ($.inArray(tooth, curTooth) > -1)) {
            //console.log("posibleng i-save sa db x:"+clickX+" at y:"+clickY); //for printing the click Array
            paint = false;
            clearPaint(tooth);
            loadPaint(tooth);
        };
    });

    $tempCanvas.mouseleave(function(e) {
        if (toolType === 'paint' && ($.inArray(tooth, curTooth) > -1)) {
            paint = false;
            clearPaint(tooth);
        };
    });
};

/*END OF PAINT FUNCTION*/

//selecting dental service
$('#selectable button').click(function() {
    var $this = $(this);
    var $id = $this.attr('id');
    var $toolType = $this.attr('data-type')

    if ($toolType === 'paint' && $id != 'ERASER') {
        toolType = 'paint';
        toolData = $id;
        curColor = $this.attr('data-color'); //get the color if paint
    } else if ($toolType === 'symbol') {
        toolType = 'symbol';
        toolData = $id;
        curColor = '';
    }
    //if eraser is selected
    if ($id === 'ERASER') {
        curColor = $this.attr('data-color');
        curTool = 'eraser';
        console.log("curTool: "+curTool+" for "+toolData); //output this log
    };
});

$('.gum canvas').hover(function() {
    var $this = $(this);
    tooth = $this.parent().attr('id');
    imageWidth = $this.attr('width');
    imageHeight = $this.attr('height');
    if( $.inArray(tooth, curTooth) != -1){
        switch(toolType){
            case 'symbol':
                //setSymbol(tooth, toolType, toolData);
                setVariables(tooth, toolType, toolData);
                redefineFunctions();
                break;
            case 'paint':
                setPaint(tooth, toolType, toolData);
                break;
            default:
                tooth = "";
                console.log("no toolType selected");
                break;
        }
    }
});

//creating canvas to put paint on
function setPaint(tooth, toolType, toolData) {
    if ( $.inArray(toolData, listedPaint) >= -1 || $.inArray(toolData, listedSymbol) >= -1) {
        console.log('===========================> setPaint'+tooth);
        var gum = "#"+tooth+".gum";
        var t = tooth+"_"+toolData;
        var $c = $("#canvas"+tooth+"_"+toolData);
        //check if not-exists ung canvas, if-not exists add div
        if ($c.length <= 0 && toolType != "") {
            $(gum).prepend("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"'></canvas></div>");
        }
        setVariables(tooth, toolType, toolData);
        redefineFunctions();
    };
};

//creating canvas to put symbol on
function setSymbol(tooth, toolType, toolData) {
    switch(toolData) {
        case 'EXT':
            console.log("==========================> setSymbol: "+tooth);
            var c = '#'+tooth+' div #canvas'+tooth+'_'+toolData;
            var tempCanvas = "#"+tooth+".gum>canvas";
            var t = tooth+"_"+toolData;
            if ($(c).length === 0) { //check if not-exists ung canvas, if-not exists add div
                $('#'+tooth+' div').filter(':last').before("<div class='absolute'><canvas id='canvas"+t+"' width='"+imageWidth+"' height='"+imageHeight+"'></div>");
            } else {
                $(c).parent().remove()
            }
            break;
        default:
            console.log('pumasok sa default ng setSymbol');
    };
};

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

