/*  ###########################################################################
    Author:     Lunatech Labs
    date:       May 2011
    ########################################################################### */


    $(document).ready(function(){
    
        var s = window.location.pathname.split('/')[2];
        if (s==="en" || s==="fr" || s==="nl"){
            $.cookie('navlang',s);
            $(".home-link").attr('href',"/index.php/"+s);
            $('a[hreflang]').removeClass('active');
            $('a[hreflang='+s+']').addClass('active');
        }
    
        $("nav").delegate('a[hreflang]','click',function(){
            var lang = $(this).attr('hreflang');
            $.cookie('navlang',lang);
            $(this).attr('href',"/index.php/"+lang);
        });
        
        $("#photos").homeGallery('dutchcelt', '5151045243279797297', 4);
        
        $("#gallery").photoGallery('dutchcelt', '5304827252675935233', 28);
        
        if (!Modernizr.cssgradients && Modernizr.borderradius){
            $("button").css('border-radius','0');
        }

    });
    
    $(window).load(function(){
        var lang = $.cookie('navlang');
        if (lang!=null) { 
            $(".home-link").attr('href',"/index.php/"+lang); 
            $("html").attr('lang',lang); 
            $('a[hreflang='+lang+']').addClass('active');
        } else {
            $('a[hreflang='+ $("html").attr('lang') +']').addClass('active');
        }
    });


    
//  PICASA SETUP  #############################################################


    // HOMEPAGE PHOTOS ========================================================

    $.fn.homeGallery = function(user, album, num, callback) {
        var scope = $(this);
        $.picasa.images(user, album, function(images) {
          if (callback) {
            callback(images);
          } else {
            var picasaAlbum = "";
            $.each(images, function(i, element) {
                picasaAlbum += "<a rel='#overlay' data-photo='" + element.url + "' href='" + element.url + "'><img src='" + element.thumbs[2].url + "' alt='" + element.title + "' /></a>\n";
                if (i===(num-1)) return false;
            });
            scope.append(picasaAlbum).each(function(){
                $('.contentWrap:eq(0)').append('<img src="" alt="" />').append('<p></p>');
                $("#photos a").overlay({ mask: '#333' });
            }).delegate('a','mousedown',function(){
                $('.contentWrap:eq(0) img').attr("src",$(this).data('photo'));
                $('.contentWrap:eq(0) p').text($('img',this).attr('alt'));
                return false;
            });
          }
        });
    }
    
    // PHOTO GALLERY =========================================================
    
    $.fn.photoGallery = function(user, album, num, callback) {
        var scope = $(this);
        $.picasa.images(user, album, function(images) {
          if (callback) {
            callback(images);
          } else {
            var picasaAlbum = "";
            $.each(images, function(i, element) {
                picasaAlbum += "<a title='" + element.title + "' href='" + element.url + "'><img src='" + element.thumbs[1].url + "' alt='' /></a>\n";
                if (i===(num-1)) return false;
            });
            scope.append(picasaAlbum).each(function(){
                if ($('.viewer').attr('src')==null) {
                    $("a:first", this).addClass('active');
                    $('.viewer').attr('src', $("a:first", this).attr('href'));
                    $('h2').text($("a:first", this).attr('title'));
                }
            }).delegate('a','click',function(){
                $("#gallery a").removeClass('active');
                $(this).addClass('active');
                var h = $(this).attr('href');
                var t = $(this).attr('title');
                $('.viewer').attr('src',h);
                $('h2').text(t);
                return false;
            });
          }
        })
    }

    
    

/*      FLICKR SETUP ABANDONED ################################################
    	
        
    	$('#photos').jflickrfeed({
    		limit: 4,
    		qstrings: {
    			id: '70314018@N00'
    		},
    		itemTemplate: '<a rel="#overlay" data-photo="{{image}}"><img src="{{image}}" alt="{{title}}" /></a>'
    	},function(data){
    	   $('.contentWrap:eq(0)').append('<img src="" alt="" />').append('<p></p>');
    	   $("a",this).overlay({ mask: '#333' });
        }).delegate('a','mousedown',function(){
    	   $('.contentWrap:eq(0) img').attr("src",$(this).data('photo'));
    	   $('.contentWrap:eq(0) p').text($('img',this).attr('alt'));
    	   return false;
    	});
    	$('#gallery').jflickrfeed({
    		limit: 20,
    		qstrings: {
    			id: '70314018@N00'
    		},
    		useTemplate: false,
    		itemCallback: function(item){
        	   $(this).append('<a href="' + item.image + '" title="'+item.title+'" data-gallery-description="'+escape(item.description)+'"><img src="' + item.image_s + '" alt="" /></a>');
        	   if ($('.viewer').attr('src')==null) {
        	       $("a:first", this).addClass('active');
        	       $('.viewer').attr('src', $("a:first", this).attr('href'));
        	       $('h2').text(item.title);
        	       $('#gallery-description').append("<p>"+unescape($('a:first', this).data('galleryDescription'))+"</p>");
        	   }
    		}
    	}).delegate('a','click',function(){
            $("#gallery a").removeClass('active');
            $(this).addClass('active');
            var h = $(this).attr('href');
            var t = $(this).attr('title');
            var d = unescape($(this).data('galleryDescription'));
            $('.viewer').attr('src',h);
            $('h2').text(t);
            $('#gallery-description p').text("").append(d);
            return false;
        });
*/
