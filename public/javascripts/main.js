var UPA = ['F1','F2','F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12', 'F13', 'F14', 'F15', 'F16']; //must come from db via ajax call
var LOWA = ['F17','F18','F19', 'F20', 'F21', 'F22', 'F23', 'F24', 'F25', 'F26', 'F27', 'F28', 'F29', 'F30', 'F31', 'F32'];


function otherTooth(tooth){
    var word = tooth;
    var len = word.length;
    var fLetter = word.substr(0, 1);
    var rLetters = word.substr(1, len);

    var newWord = (fLetter === 'M') ? 'F' : 'M';
    newWord = newWord + rLetters;

    return newWord;
}

// onready
jQuery(function($){

    eventHistory.add(function(){

        /*============
            Modal
        ==============*/
        $('.modal').click(function(e){
            $($(this).attr('href')).modal({top: 'center'});
            e.preventDefault();
        });


        /*============
            Submenus
        ==============*/
        (function(){
            // show/hide submenu
            $('#wrapper > header nav > ul > li:has(.submenu)').mouseenter(function(){
                $(this).find('.submenu').stop(true, true).slideDown('slow', 'easeOutQuad');
            }).mouseleave(function(){
                $(this).find('.submenu').slideUp('normal');
            })

            $('#wrapper > header nav > ul > li > a').click(function(e){
                if($(this).attr('href') == '#') e.preventDefault();
            });
        })();


        /*============
            Bar
        ==============*/
        $('.bar .search input[type=text]').hide();

        $('.bar .search').click(function(e){
            var $this = $(this);
            var $text = $this.find('input[type=text]');
            if(!$this.is('.open')){
                $this.addClass('open');
                $text.show().focus();
                if($(e.target).is('input[type=submit]'))
                    e.preventDefault();
            }
        });

        $('body').click(function(e){
            var $search = $('.bar .search input[type=text]');
            if($search.closest('.search').is('.open')){
                var $target = $(e.target);
                if(!$target.is($search) && !$target.is($search.closest('.search')) && !$target.is($search.parent()) && !$target.is($search.prev())){
                    $search.hide().closest('.search').removeClass('open');
                }
            }
        });

        /*============
            Form
        ==============*/
        // icons
        $('input[data-icon]').each(function(){
            var $this = $(this);
            var icon = $this.attr('data-icon');
            
            $this.before('<label class="glyph ' + icon + '" for="' + $this.attr('id') + '"></label>').parent().addClass('iconized').end().prev().css({top: $this.position().top+7, left: $this.position().left+8})
        });

        // wysiwyg
        if($.browser.msie && $.browser.version < 9){
            $('.editor').html('Content');
            $('html').addClass('ie');
        }
        $('.editor').elrte({
            toolbar: 'normal',
            styleWithCSS : false,
            height: 300
        });

        // validation
        $('.validate').validate({
            onclick: false,
            onkeyup: false,
            onfocusout: false,
            success: function(label) {
                label.parent().removeClass('error-container');
            },
            errorPlacement: function(error, element) {
                element.parent().addClass('error-container').append(error);
            }
        });

        // filemanger
	    $('.filemanager').elfinder({
            url : 'connectors/php/connector.php',
            toolbar : [
                ['back', 'reload'],
                ['select', 'open'],
                ['quicklook', 'rename'],
                ['resize', 'icons', 'list']
            ],
            contextmenu : {
                cwd : ['reload', 'delim', 'info'], 
                file : ['select', 'open', 'rename'], 
            }
	    });

        // masks
        $.mask.definitions['~']='[+-]';
        $('.mask-date').mask('99/99/9999');
        $('.mask-phone').mask('(999) 999-9999');
        $('.mask-phoneext').mask("(999) 999-9999? x99999");
        $(".mask-tin").mask("99-9999999");
        $(".mask-ssn").mask("999-99-9999");
        $(".mask-product").mask("a*-999-a999",{placeholder:" ",completed:function(){alert("You typed the following: "+this.val());}});
        $(".mask-eyescript").mask("~9.99 ~9.99 999");

        // colorpicker
        $('.colorpicker').miniColors();

        // datepicker
        $('.datepicker').datepicker();

        // datepicker
        $('.datetimepicker').datetimepicker();

        // full calendar
        $('.fullcalendar').fullCalendar({
            editable: true,
		    header: {
			    left: 'prev,next',
			    center: 'title',
			    right: 'month,basicWeek,basicDay'
	        }
        });

        // textarea
        $('.autogrow').autoGrow();

        // custom file input
        $('.custom-file-input').customFileInput();

        // chosen select
        $('.chosen').chosen();

        // custom checkboxes and radios
        $('input').checkBox();

        // spinner
        $('.number:not(.currency)').spinner();

        // currency spinner
        $('.currency.number').spinner({
            prefix: '$',
            group: ',',
            step: 0.5,
            largeStep: 5,
            min: -1000000,
            max: 1000000
        });

        // check/uncheck all
        $('.check-all input').click(function(){
            var $checkAll = $(this);
            var $allChecks = $checkAll.closest('table').find('tbody .checkbox input[type=checkbox]');
            var $datatable = $checkAll.closest('.datatable');

            if($checkAll.prop('checked')){
                $allChecks.checkBox('changeCheckStatus', true);
            }else{
                $allChecks.checkBox('changeCheckStatus', false);
            }

            if($datatable[0])
                $datatable.closest('.boxed-table').find('.paginate_button').click(function(){ $checkAll.checkBox('changeCheckStatus', false); });
        });

        /*============
            Miscellaneous
        ==============*/

        /* FORMALIZE.init.placeholder();

        (function(){

            var ajaxOptions = {
                dataType: 'text',
                beforeSend: function(jqXHR){
                    fadeContentNicely();
                    $.loading().start();
                },
                error: function(jqXHR, textStatus){
                    alert('AJAX request error status. Status: ' + jqXHR.status + ' ' + jqXHR.statusText);
                },
                complete: function(){
                    $.loading().dismiss();
                }
            };

            function fadeContentNicely(onComplete, c){
                if(c == undefined)
                    c = $('#content');
                c.animate({
                    marginTop: '+=150px',
                    opacity: 0
                }, {
                    complete: onComplete
                });
            }

            $('.compact-page a.ajax').click(function(e){
                ajaxOptions.success = function(data){
                    var body = data.split('<body>')[1].split('</body>')[0];
                    var $body = $('<div>').html(body);
                    
                    var $oldContent = $('#content');

                    if($body.find('.compact-page')[0]){
                        $('body').html(body);
                        $('#content').css({marginTop: '-=150px', opacity: 0}).animate({
                            marginTop: '+=150px',
                            opacity: 1
                        });
                        eventHistory.rebindAll();
                    }                  
                }

                $.ajax($(this).attr('href'), ajaxOptions);

                e.preventDefault();
            });

            $('.compact-page form.jmenu').bind('submit.jmenu', function(e){
                var $this = $(this);

                ajaxOptions.data = $this.serialize();
                ajaxOptions.type = $this.attr('method') ? $this.attr('method') : 'GET';
                var parent = ajaxOptions.beforeSend;
                ajaxOptions.beforeSend = function(jqXHR){
                    jqXHR.setRequestHeader('X-Requested-With', {toString: function(){ return ''; }})
                    parent();
                };
                ajaxOptions.success = function(data){
                    var body = data.split('<body>')[1].split('</body>')[0];
                    var $body = $('<div>').html(body);
                    var $nav = $body.find('#wrapper > header nav'), $newNav = $('<nav class="menu" />');

                    // if is the page we are wating for
                    if($nav[0]){
                        var $ul = $newNav.html('<ul></ul>').find('ul');
                        
                        // format the menu
                        $nav.find('> ul > li').each(function(i, el){
                            var $li = $(el),
                                $a = $li.find('> a'),
                                $icon = $a.find('span'),
                                text = $.trim($a.text()),
                                $submenu = $li.find('.submenu'),
                                isSubmenu = $submenu[0]

                                $a.html('<div class="wrapIcon"></div><div class="wrapText"></div>').find('.wrapIcon').html($icon).end().find('.wrapText').html(text);
                                $li.html($a);
                                if(isSubmenu){
                                    $li.append($submenu).data('submenu', true);
                                }
                                $ul.append($li);
                        });

                        $('body').append($newNav).css('overflow', 'hidden');

                        var $logo = $('#logo').clone(),
                            $compact = $('.compact-page'),
                            $wrapper = $('#wrapper'),
                            perLine = 3,
                            liHeight = $newNav.find('li:first').height(),
                            liWidth = $newNav.find('li:first').width(),
                            totalLi = $newNav.find('> ul > li').size(),
                            numLines = Math.ceil(totalLi / perLine);

                        $compact.append($logo);
                        var logoHeight = $logo.find('img').height(),
                            logoWidth = $logo.find('img').width();
                        $logo.css('opacity', 0);
                        $compact.find('> div').remove();
                        $wrapper.append($logo).append($newNav);

                        var o = 0, top = 0, isLastLine = false;
                        
                        // animate each one of the itens
                        $newNav.find('> ul > li').each(function(i, el){
                            var $li = $(el), mLeft, mTop;

                            if((i % perLine) == 0 && i != 0){
                                o = 0;
                                top++;
                            }
                            if(i > totalLi-perLine)
                                isLastLine = true;

                            $li.css({
                                position: 'absolute',
                                top: '50%',
                                left: '50%',
                                opacity: .5,
                                marginLeft: mLeft = (-((liWidth/2) * (perLine)) + (o * liWidth)),
                                marginTop: mTop = (-(liHeight/2 * numLines) + (top*liHeight))
                            });

                            var offset = $li.offset(), n = i % 2 == 0 ? -1 : 1;
                            $li.data('n', n);
                              
                            $li.animate({
                                opacity: 1,
                                path : new $.path.bezier({
                                    start: { 
                                        x: Math.floor(Math.random()*$(window).width()),
                                        y: Math.floor(Math.random()*$(window).height()), 
                                        angle: 100
                                    },
                                    end: { 
                                        x: offset.left+(mLeft)*-1,
                                        y: offset.top+(mTop)*-1, 
                                        angle: -100
                                    }
                                })}, {
                                duration: 800,
                                complete: function(){
                                    $(this).css({top: '50%', left: '50%'});
                                    if(i == totalLi-1){
                                        $logo.css({position: 'absolute', top: '50%', left: '50%', marginLeft: -(logoWidth/2), marginTop: -(100 + logoHeight + (numLines*liHeight)/2)}).animate({opacity: 1, marginTop: '+=100'}, 300);
                                        $('body').trigger('jcomplete');
                                        eventHistory.rebindAll();
                                        //$newNav.find('> ul > li:has(.submenu) > a').click();
                                    }
                                }
                            });

                            $li.find('a').click(function(e){
                                var $a = $(this);
                                // do submenu
                                if($li.data('submenu')){
                                    var $back = $('<p class="center"><a href="#" class="jback">‹ back</a></p>'),
                                        oldTop = $logo.css('margin-top'),
                                        sub = $('<nav class="jsubmenu" />').append('<header><h2>' + $a.text() + '</h2></header>').append($li.find('.submenu').clone()).append($back);

                                    $back.click(function(e){
                                        fadeContentNicely(function(){
                                            sub.remove();
                                        }, sub);

                                        $newNav.find('> ul > li').each(function(){ $(this).css('margin-top', $(this).data('oldTop')).css({marginTop: '-=100px'}).show().animate({marginTop: '+=100px', opacity: 1}); })
                                        $logo.animate({marginTop: oldTop })
                                        e.preventDefault();
                                    });

                                    $('body').append(sub);
                                    var subWidth = sub.width(),
                                        subHeight = sub.height();

                                    $newNav.find('> ul > li').each(function(i){
                                        var $li = $(this), n = $li.data('n') < 1 ? '-' : '+', ni = $li.data('n') > -1 ? '-' : '+';
                                        $li.data('oldTop', $li.css('margin-top')).animate({marginTop: ni + '=100px', opacity: 0}, function(){ $(this).hide(); });
                                    });
                                    sub.css({opacity: 0, marginLeft: -(subWidth/2), marginTop: -(subHeight/2 + 100)}).animate({opacity: 1, marginTop: '+=100px'}, 'slow');
                                    $logo.animate({marginTop: -(logoHeight/2) - (subHeight/2) - 50 });
                                    eventHistory.rebindAll();
                                    e.preventDefault();
                                }
                            });
                            
                            o++;
                        });
                        $('#content').animate({opacity: 1});
                    } else {
                        // if is not, submit the form normally
                        $this.unbind('submit.jmenu').submit();
                    }
                }

                $.ajax($this.attr('action'), ajaxOptions);

                e.preventDefault();
            });
        })();    */

        // sliders
        $('.slider').each(function(){
            var options = {},
                $this = $(this);
            
            if($this.is('.vertical'))
                options.orientation = 'vertical';

            if($this.is('.range')){
                options.range = true;
                options.values = [5, 25];
            }

            $this.slider(options);
        });

        // tooltip tipsy
        $('.tooltip').each(function(){
            var gravity = $(this).attr('data-position');

            if(!gravity)
                gravity = $.fn.tipsy.autoNS;

            $(this).tipsy({gravity: gravity, fade: true});
        });

        // progressbar
        (function(){
            $( ".progressbar" ).each(function(){
                var $this = $(this), options = {};

                $this.progressbar();

                if($this.attr('data-value')){
                    options.value = parseInt($this.attr('data-value'));
                    growTo(options.value, $this);
                }
            });

            function growTo(value, $progress){
                $progress.find('.ui-progressbar-value').show().animate({width: value + '%'}, {
                    duration: 1500,
                    complete: function(){
                        $progress.progressbar('option', 'value', value);
                    },
                    easing: 'easeOutBounce'
                });
            }
        })();

        // open link as modal
        $('.open-modal').click(function(e){
            var modal = $($(this).attr('href')).leanModal({closeButton: '.box .close'});
            e.preventDefault();
        });

        // modal
        $('.prettyPhoto').prettyPhoto({social_tools:false});

        // fixes
        $('.head .divider').before('<div class="clear">');

        $('.userlist li').click(function(){
            window.location.href = $(this).find('a').attr('href');
        });

        $('.miniColors-trigger').each(function(){
            var $this = $(this);
            if($this.prev().is('input[type=text]')){
                $this.prev().css('padding-right', 35);
                $this.css({position: 'relative', top: 6, left: -30});
            }
        });

        $('.table table:not(.datatable) tbody tr:nth-child(even)').addClass('even');
        $('.boxed-table table:not(.datatable) tbody tr:nth-child(even)').addClass('even');

        $('li:first-child, th:first-child').addClass('first');
        $('li:last-child, th:last-child').addClass('last');
        $('table.statistics tbody tr:last-child').addClass('last');

        /*============
            Box
        ==============*/
        // box tabs
        $('.box').each(function(){
            var $this = $(this)
            var currentActiveTab = $this.find('header nav li.active a').attr('href');
            $this.find('.tab:not(' + currentActiveTab + ')').hide();

            $this.find('header nav li a').click(function(e){
                var $self = $(this);
                if(!$self.is($('a[href=' + currentActiveTab + ']'))){
                    var cur = $(currentActiveTab);
                    var origHeight = cur.css('height');
                    cur.hide();
                    currentActiveTab = $self.attr('href');
                    cur = $(currentActiveTab);
                    var realHeight = cur.show().height(); cur.hide();
                    $self.closest('nav').find('li').removeClass('active').filter($self.parent()).addClass('active');


                    cur.show().css('opacity', 0).height(origHeight).animate({height: realHeight}, function(){
                        cur.height('auto').css('opacity', '1').hide().fadeIn();
                    });
                }
                e.preventDefault();
            });
        });

        // close
        $('.box header').on('click', '.close', function(e){
            var $box = $(this).closest('.box');

            if(!$box.is('.modal-window-hidden')){
                var $boxContent = $box.find('.box-content');
                if($boxContent.is(':visible')){
                    $boxContent.slideUp();
                    $box.toggleClass('closed');
                }else{
                    $boxContent.slideDown();
                    $box.toggleClass('closed');
                }
                e.preventDefault();
            }
        });

        /*============
            Notifications
        ==============*/
        // air
        $('.air').mouseenter(function(){
            $(this).stop(true, true).animate({opacity: 1});
        }).mouseleave(function(){
            $(this).animate({opacity: .85});
        });

        // close alert
        $('.alert .close').click(function(e){
            $(this).closest('.alert').slideUp(function(){
                $(this).remove();
            });
            e.preventDefault();
        });

        /*============
            Media
        ==============*/
        $('.gallery li').mouseenter(function(){
            $(this).find('.actions').animate({bottom: '-=20px'}, 'fast');
        }).mouseleave(function(){
            $(this).find('.actions').stop(true, true).animate({bottom: '+=20px'}, 'fast');
        });

        /*============
            DataTable
        ==============*/
        (function(){
            $.fn.wrapInnerTexts = function($with){
                if(!$with)
                    $with = '<span class="textnode">';

                $(this).each(function(){
                    var kids = this.childNodes;
                            for (var i=0,len=kids.length;i<len;i++){
                                if (kids[i].nodeName == '#text'){
                                    $(kids[i]).wrap($with);
                                }
                            }
                });
                return $(this);
            };

            var dataTableOptions = {
                sDom: '<"extension top fullwidth"lf>rt<"extension bottom inright pagination"p>',
                sPaginationType: 'full_numbers',
                fnInitComplete: function(t){
                    var $table = $(t.nTable),
                        $boxedTable = $table.parents('.boxed-table');

                    $table.find('.sorting, .sorting_asc, .sorting_desc').wrapInner($('<div class="parentsort" />')).find('.parentsort').append('<div class="sorticon" />');
                    $table.prev().find('.dataTables_length select').removeAttr('size').parent().wrapInnerTexts();
                    $table.prev().find('.dataTables_filter label').wrapInnerTexts();

                    if($boxedTable[0]){
                        // moves length input in box header
                        $boxedTable.find('header .inner .right').prepend($('<div class="per-page">').html('<label><span class="textnode">itens per page</span></label>').find('label').append($boxedTable.find('.dataTables_length select')).end());
                        // moves search input
                       $table.prev().find('.dataTables_filter input').addClass('right').attr('placeholder', 'Search').appendTo($boxedTable.find('.box .box-content .pane'));  

                        // remove original div
                        $table.prev().remove();

                        // moves pagination to the right place
                        $boxedTable.find('.box').after($table.next());
                        $('th.checkbox, th.sorting_disabled').removeClass('sorting sorting_asc').unbind('click').removeAttr('role').find('.sorticon').remove();
                        
                    }else{
                        $table.prev().append('<div class="clear">');
                    }
                }
            };

            /*$('.datatable').each(function(){
                var $this = $(this);
                var ownOptions = dataTableOptions;

                if(!$this.is('.five-per-page')){
                    if($this.data('datatable-options')){
                        $.extend(ownOptions, $this.data('datatable-options'));
                    }
                }else{
                    ownOptions.aLengthMenu = [[5, 10, 25, 50, 100], [5, 10, 25, 50, 100]];
                    ownOptions.iDisplayLength = 5;
                }
                $this.dataTable(ownOptions);
            });*/
        })();
    });
});

$(window).load(function(){
    setTimeout($.loading().dismiss, 500);
});

var eventHistory = (function(){
    var methods = {
        add: function(event){
            events.push(event);
            event();
            return methods;
        },
        rebindAll: function(){
            for(var i in events){
                events[i]();
            }
            return methods;
        }
    }, events = [];

    return methods;
})();

$(window).load(function(){

    $('#specialization_list a').live("click",
        function(e){
           var currentIndex = parseInt($('#specialization_list input[type="text"]:last').attr("data-count"))+1;
           var dataCount = currentIndex
           var name = "specializationName["+(currentIndex)+"]";
           e.preventDefault();
           $('#specialization_list').append('<li><input type="text" class="specialization" name='+name+'  data-count='+dataCount+' value="" style="width: 85%;"><a href="#" class="bt blue left" style="width: 10px;"><span class="glyph zoom-in"></span></a></li><br/>');
        }
    );


 $(document).ready(function() {
       $('#image_uploader').hover(function() {
         $('#blah').resize({
           scale: 0.5,
           maxWidth: 200
         })
       });
     });

function res() {
                        $('#blah').resize({
                        scale: 0.5, // 0.5 = 50%
                        maxWidth: 200 //,
                        // maxHeight: 150
                        });
                        console.log("res pumasok");
                        }

                        function readURL(input) {
                        console.log(input);
                        if (input.files && input.files[0]) {
                        var reader = new FileReader();

                        reader.onload = function (e) {
                        $('#blah').attr('src', e.target.result)
                        };

                        /*$('#blah').;*/
                        reader.readAsDataURL(input.files[0]);
                        };
                        }

                        /*$('input[type=file]').change(function() {
                        console.log("pumasok sa image upasdfasdf")
                        readURL($(this));
                        })*/

    /*$(function() {
        $( "#datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd'
        });
    });*/

    //dashboard live effect
    var $latestPatients = $('.box-content.no-inner-space.latest-patients ul li');

    var delay = 2000;

    $latestPatients.each(function() {
        $(this).slideUp(300).delay(delay).fadeIn(400);
        delay+=1000;
        console.log(delay);
        if (delay >= 6000){
            //$(this).parent().children(':first').hide();
            //console.log(hide);
            $('.box-content.no-inner-space.latest-patients ul li').hide();
            //console.log('behind');
        }
    });

    //context menu
    $('.gum').contextPopup({
        items: [
            {label:'Dentist Tools',
            icon:'/assets/images/favicon.png',
            action: function(){
                $('.ui-dialog.ui-widget.ui-widget-content.ui-corner-all.ui-draggable.ui-resizable').show("fold")
                }
            }
            ]
    });

    //banned dental service
    $('.field .button-set a.forward').click(function(e){
        e.preventDefault();
        var selected = $('select.special.services-list option:selected').attr('selected','selected');
        $.each(selected, function(i, l){
            $('.field select.services-banned').append(l);
            //var v = $(l).val();
            //var i = $('.field.center div.services-banned input').length;
            //var n = 'banned_service['+i+']';
            //$('.field.center div.services-banned').append('<input name="'+n+'" type="hidden" value="'+v+'"/>');
        });
        $('.field select option').removeAttr('selected');
    });

    //back button
    $('.field .button-set a.back').click(function(e){
        e.preventDefault();
        var selected = $('select.special.services-banned option:selected').attr('selected','selected');
        $.each(selected, function(i, l){
            $('.field select.services-list').append(l);
            var v = $(l).val();
            $('.field div.services-banned').remove('input[value="'+v+'"]');
        });
        $('.field select option').removeAttr('selected');
    });

    $('#addServiceButton').click(function(){
        var selected = $('select.special.services-banned option').attr('selected','');
        $.each(selected, function(i, l){
            var v = $(l).val();
            var i = $('.field.special div.services-banned input').length;
            var n = 'banned_service['+i+']';
            $('.field.special div.services-banned').append('<input name="'+n+'" type="hidden" value="'+v+'"/>');
        });
    });

    /*Birthdays*/
    var d, m, y;
    var $dateOfBirth = $('input[name=date_of_birth]')
    var curDate = new Date();
    var curDay = curDate.getDate(), curMonth = curDate.getMonth()+1, curYear = curDate.getFullYear();
    var yearRange = 100;
    var startYear = curYear - 6; //youngest age possible to the system
    var earlyYear = startYear - yearRange;
    var $day = $('select[id=day]');
    var $month = $('select[id=month]');
    var $year = $('select[id=year]');
    var $presentYear = $('select[id=present_year]');
    var monthArr = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]

    function daysInMonth(month, year) {
        return new Date(year, month, 0).getDate();
    }

    //populate day - initial
    for(var i=1;i<=daysInMonth(curMonth, curYear);i++){
        $day.append('<option value='+i+'>'+i+'</option>');
    }

    //populate month
    for(var i=1;i<=12;i++){
        $month.append('<option value='+i+'>'+monthArr[i-1]+'</option>');
    }

    //populate year
    for(var i=startYear;i>=earlyYear;i--){
        $year.append('<option value='+i+'>'+i+'</option>');
    }

    //populate year
    for(var i=curYear;i>=earlyYear;i--){
        $presentYear.append('<option value='+i+'>'+i+'</option>');
    }

    //set number of days per month
    $day.change(function(){
        d = $(this).val();
        m = $month.val();
        y = $year.val();
        $dateOfBirth.val(y+"-"+m+"-"+d);

        $month.html('')
        for(var i=1;i<=12;i++){
            if(parseInt(m) === i){
                $month.append('<option value='+i+' selected>'+monthArr[i-1]+'</option>');
            } else {
                $month.append('<option value='+i+'>'+monthArr[i-1]+'</option>');
            }
        }
        $year.html('')
        for(var i=startYear;i>=earlyYear;i--){
            if(parseInt(y) === i){
                $year.append('<option value='+i+' selected>'+i+'</option>');
            } else {
                $year.append('<option value='+i+'>'+i+'</option>');
            }
        }
    });

    //set number of days per month
    $month.change(function(){
        d = $day.val();
        m = $(this).val();
        y = $year.val();
        $dateOfBirth.val(y+"-"+m+"-"+d);

        $day.html('')
        for(var i=1;i<=daysInMonth(m, y);i++){
            if(parseInt(d) === i){
                $day.append('<option value='+i+' selected>'+i+'</option>');
            } else {
                $day.append('<option value='+i+'>'+i+'</option>');
            }
        }
        $year.html('')
        for(var i=startYear;i>=earlyYear;i--){
            if(parseInt(y) === i){
                $year.append('<option value='+i+' selected>'+i+'</option>');
            } else {
                $year.append('<option value='+i+'>'+i+'</option>');
            }
        }
    });

    //set if the date is correct
    $year.change(function(){
        d = $day.val();
        m = $month.val();
        y = $(this).val();
        $dateOfBirth.val(y+"-"+m+"-"+d);

        $day.html('')
        for(var i=1;i<=daysInMonth(m, y);i++){
            if(parseInt(d) === i){
                $day.append('<option value='+i+' selected>'+i+'</option>');
            } else {
                $day.append('<option value='+i+'>'+i+'</option>');
            }
        }
        $month.html('')
        for(var i=1;i<=12;i++){
            if(parseInt(m) === i){
                $month.append('<option value='+i+' selected>'+monthArr[i-1]+'</option>');
            } else {
                $month.append('<option value='+i+'>'+monthArr[i-1]+'</option>');
            }
        }
    });

    //retrieve birthday in update forms
    if($('input[name=date_of_birth]').val() !== null){
        var dob = new Date($('input[name=date_of_birth]').val());
        $day.val(dob.getDate());
        $month.val(dob.getMonth()+1);
        $year.val(dob.getFullYear());
    }

    /*Password Complexity*/
    $('input[name=password]').on('keypress', function(e){
        var len = $(this).val().length;
        if(len < 5){
            $('.password-label').addClass("too-short").html('too-short')
            $('.password-label').removeClass("weak")
            $('.password-label').removeClass("safe")
            $(this).addClass("password-too-short")
            $(this).removeClass("password-weak")
            $(this).removeClass("password-safe")
        } else if(len < 8) {
            $('.password-label').removeClass("too-short")
            $('.password-label').addClass("weak").html('weak')
            $('.password-label').removeClass("safe")
            $(this).removeClass("password-too-short")
            $(this).addClass("password-weak")
            $(this).removeClass("password-safe")
        } else if(len < 15){
            $('.password-label').removeClass("too-short")
            $('.password-label').removeClass("weak")
            $('.password-label').addClass("safe").html('safe')
            $(this).removeClass("password-too-short")
            $(this).removeClass("password-weak")
            $(this).addClass("password-safe")
        } else if(len <= 0) {
            $('.password-label').removeClass("too-short").html('')
            $('.password-label').removeClass("weak")
            $('.password-label').removeClass("safe")
            $(this).removeClass("password-too-short")
            $(this).removeClass("password-weak")
            $(this).removeClass("password-safe")
        } else {}
    }).on('keydown', function(e) {
        if (e.keyCode==8)
            $(this).trigger('keypress');
    });



    /*Settings Links*/
    /*var mod = ["dentists","clinic","staffs","reminders"];
    for (var i=0;i<mod.length;i++){
        //alert(mod[i]);
        var selector = 'a[name='+mod[i]+']';
        var url = '/'+mod[i];
        $(selector).click(function(){
            $.ajax({
                type: "GET",
                url: url,
                success: function(res) {
                    $('.main-box .grid_9').html($(res).find('.main-box .grid_9').html());
                }
            });
        });
    };

    //clinic
    $('a[name=clinic]').click(function(){
        $.ajax({
            type: "GET",
            url: "/clinic",
            beforeSend: function(){
               $("#loader").show();
            },
            success: function(res) {
                $("#loader").hide();
                $('.main-box .grid_9').html($(res).find('.main-box .grid_9').html());
            }
        });
    });

    //dentists
    $('a[name=dentists]').click(function(){
        $.ajax({
            type: "GET",
            url: "/dentists",
            beforeSend: function(){
               $("#loader").show();
            },
            success: function(res) {
                $("#loader").hide();
                $('.main-box .grid_9').html($(res).find('.main-box .grid_9').html());
            }
        });
    });

    //reminders
    $('a[name=reminders]').click(function(){
        $.ajax({
            type: "GET",
            url: "/reminders",
            beforeSend: function(){
               $("#loader").show();
            },
            success: function(res) {
                $("#loader").hide();
                $('.main-box .grid_9').html($(res).find('.main-box .grid_9').html());
            }
        });
    });

    //staffs
    $('a[name=staffs]').click(function(){
        $.ajax({
            type: "GET",
            url: "/staffs",
            beforeSend: function(){
               $("#loader").show();
            },
            success: function(res) {
                $("#loader").hide();
                $('.main-box .grid_9').html($(res).find('.main-box .grid_9').html());
            }
        });
    });*/

    var opening = 8

    /*Date Performed DateTime Picker*/
    $('#dentistTools .dental-services-details input[name=date_performed]').datetimepicker({
    	//timeFormat: "hh:mm tt"
    	hourMin: opening,
        hourMax: 18,
    	stepMinute: 15
    });

    $('.box-content .form-details input[name=date_created]').datetimepicker({
        hourMin: opening,
        hourMax: 18,
        stepMinute: 15
    });

    /*Typeahead add appointment*/
    $.getJSON("/json/patients/all/names", function(data){
        $('#addAppointmentModal input[name=first_name]').typeahead({
            source: data,
            items: 8,
            highlighter: function(item){
                return '<div class="typeahead">'+item+'</div>'
            },
            updater: function(val) {
                return(val);
            }
        });
    });

    /*Typeahead add dental service*/
    $.getJSON("/json/dental_services/all/types", function(data){
        $('input[name=type]').typeahead({
            source: data,
            items: 8,
            highlighter: function(item){
                return '<div class="typeahead">'+item+'</div>'
            },
            updater: function(val) {
                return(val);
            }
        });
    });

    /*Typeahead add dental service*/
    $('input[type=text].specialization').on("keypress", function(e){
        $.getJSON("/json/specialization/all/names", function(data){
            $('input[type=text].specialization').typeahead({
                source: data,
                items: 8,
                highlighter: function(item){
                    return '<div class="typeahead">'+item+'</div>'
                },
                updater: function(val) {
                    return(val);
                }
            });
        });
    });

    /*add dental service change of tool type*/
    $('select[name=target]').on("change", function(){
        if($(this).val() === '2' || $(this).val() === '3'){
            $('select[name=image_template]').parent().show();
        } else {
            $('select[name=image_template]').val('')
            $('select[name=image_template]').parent().hide();
        };
    });

    /*contact number 6-12 digits only*/
    $('input[name=contact_no]#contact_no').on("keyup", function(e){
        var len = $(this).val().length;
        if($(this).val().match(/^[0-9]+$/) === null){
            $(this).attr("value","");
        }
    });

    /*contact number validation*/
    $('input[name=contact_no]#contact_no').on("blur", function(e){
        var len = $(this).val().length;
        if($(this).val().length < 6 || $(this).val().length > 12){
            var a = $(this).val().substr(0,12);
            $(this).attr("value",a);
            alert("Please enter a valid contact number.");
        }
    }).on('keydown', function(e) {
    if (e.keyCode==8)
        $(this).trigger('keypress');
    });

    /*date performed default value*/
    var date = new Date();
    var curr_date = date.getDate();
    var curr_month = date.getMonth() + 1; //Months are zero based
    var curr_year = date.getFullYear();
    $('#dentistTools').find('.dental-services input[name=date_performed]').val(curr_year+"-"+curr_month+"-"+curr_date+" "+opening+":00");


    /*tbl-treatment hide/show the service*/
    $('#tbl-treatments').find('input[type=checkbox]').click(function(){
        var dataCanvas = "#canvas"+$(this).attr("data-teeth-name")+"_"+$(this).attr("data-service-id")+"_"+$(this).attr("data-treat-id");
        var dataCanvas2 = "#canvas"+otherTooth($(this).attr("data-teeth-name"))+"_"+$(this).attr("data-service-id")+"_"+$(this).attr("data-treat-id");
        var array = [];
        var teethName = $(this).attr("data-teeth-name");
        var serviceId = $(this).attr("data-service-id");
        var treatId = $(this).attr("data-treat-id");
        var status = $(this).attr("checked");

        switch(teethName){
            case "UPA":
              array = UPA;
              break;
            case "LOWA":
              array = LOWA;
              break;
            case "UPC":
              array = UPA;
              break;
            case "LOWC":
              array = LOWA;
              break;
            case "ALLA":
              array = UPA.concat(LOWA);
              break;
            case "ALLC":
              array = UPA.concat(LOWA);
              break;
            default:
              console.log("teeth name not exists");
        }

        if( teethName === "UPA" || teethName === "LOWA" || teethName === "UPC" || teethName === "LOWC" || teethName === "ALLA" || teethName === "ALLC" ){
            $.each(array, function(key, value){
                var dataCanvas = "#canvas"+value+"_"+serviceId+"_"+treatId;
                var dataCanvas2 = "#canvas"+otherTooth(value)+"_"+serviceId+"_"+treatId;
                if(status === "checked"){
                    $(dataCanvas).fadeOut();
                    $(dataCanvas2).fadeOut();
                } else {
                    $(dataCanvas).fadeIn()
                    $(dataCanvas2).fadeIn()
                }
            })
        }


        if($(this).attr("checked") === "checked"){
            $(dataCanvas).fadeOut();
            $(dataCanvas2).fadeOut();
        } else {
            $(dataCanvas).fadeIn()
            $(dataCanvas2).fadeIn()
        }
    });

    /*name of each tooth in teeth chart*/
    $('.tooth_label').each(function(i, d){
        var toothId = $(d).parent().attr('id');
        $.getJSON("/json/dental_services/tooth/name/"+toothId, function(data){
            $(d).html(data);
        });
    });

    var origPrice;

    /*dental services button*/
    $('#dentistTools #dental_services button').click(function(){
        origPrice = parseFloat($(this).attr('data-price'));
    });

    /*discount on teeth chart*/
    $('select[class=discount]').change(function(){
       var multiplier = parseFloat($(this).val());
       var price = origPrice;
       discount = (price * multiplier).toFixed(2);
       price = price - discount;
       $('input[name=price]').val(price);
    });

    /*teeth naming*/
    $('.tooth_label').each(function(i, d){
        var toothId = $(d).parent().attr('id');
        $.getJSON("/json/dental_services/tooth/name/"+toothId, function(data){
            $(d).attr('value', data);
        });
    });

    /*$('.upper').hover(function(){
        $(this).css("background-color","red");
    });*/

});