
var now = new Date();
var nowOffset = 0;

function showTalks(){
    var currentTalks = [];
    var nextTalks = [];

    for(var t in tracks){
        var track = tracks[t];
        for(var i=0;i<track.length;i++){
            var talk = track[i];
            if(talk.start <= now && talk.end >= now){
                currentTalks.push(talk);
            } else if(talk.start > now) {
                var addIt = true;
                for(var n = 0; n < nextTalks.length; n++){
                	// only keep the closest talks
                    if(nextTalks[n].start > talk.start){
                        nextTalks.splice(n, 1);
                    }else if(nextTalks[n].start < talk.start){
                        addIt = false;
                        break;
                    }
                }
                if(addIt) {
                    nextTalks.push(talk);
                }
            } 
        }
    }

    // Display the current date
    $(".js-live-currentDate").text(formatDate(new Date(now)) + " " + formatTimeSeconds(new Date(now)));
    
    var t = jQuery("#target");
    t.empty();
    
    var markup = "<h2 class='schedule-day fullSchedule-day schedule-day1'>Currently</h2>";

    // -- Current talks
    if (currentTalks.length > 0) {
        markup += "<div class='current-other'>";
        //markup += "Currently in other tracks:";
        markup += "<div class='row'>";
        for(var n = 0; n < currentTalks.length; n++) {
            var talk = currentTalks[n];
            markup += "<div class='col-md-3'>";
            markup +=     "<div class='live-talk" + (talk.track == showTrack? " live-currentTrack" : "") + "'>";
            markup +=         "<h2 class='schedule-day fullSchedule-day schedule-day2'>"+talk.track+"</h2>";
            markup +=         talkToString(talk);
            markup +=     "</div>";
            markup += "</div>";
        }
        markup += "</div>";
        markup += "</div>";
    }else{
    	markup += "<div class='nothing'>Oh noes!! We haven’ts gots any talks righter now!! :(</div>";
    }

    markup += "<h2 class='schedule-day fullSchedule-day schedule-day1'>Next</h2>";

    // -- Next talks
    if (nextTalks.length > 0) {
        markup += "<div class='next-other'>";
        //markup += "Next in other tracks:";
        markup += "<div class='row'>";
        for (var n = 0; n < nextTalks.length; n++) {
            var talk = nextTalks[n];
            markup += "<div class='col-md-3'>";
            markup +=     "<div class='live-talk" + (talk.track == showTrack? " live-currentTrack" : "") + "'>";
            markup +=         "<h2 class='schedule-day fullSchedule-day schedule-day2'>"+talk.track+"</h2>";
            markup +=         talkToString(talk);
            markup +=     "</div>";
            markup += "</div>";
        }
        markup += "</div>";
        markup += "</div>";
    }else{
    	markup += "<div class='nothing'>Oh noes!! Its a doner, see youse nexts years!! :)</div>";
    }

    t.append(markup);
}
var months = [
"January",
"February",
"March",
"April",
"May",
"June",
"July",
"August",
"September",
"October",
"November",
"December",
];
function formatDate(date){
    return pad(date.getDate()) + " " + months[date.getMonth()];
}
function formatTime(date){
    return pad(date.getHours()) + ":" + pad(date.getMinutes());
}
function formatTimeSeconds(date){
    return formatTime(date) + ":" + pad(date.getSeconds());
}
function pad(num) {
    var s = num+"";
    while (s.length < 2) s = "0" + s;
    return s;
}
function duration(time){
    var seconds = Math.floor(time / 1000);
    var minutes = Math.floor(seconds / 60);
    var secondsMod = seconds % 60;
    var hours = Math.floor(minutes / 60);
    var minutesMod = minutes % 60;
    var days = Math.floor(hours / 24);
    var hoursMod = hours % 24;
    var weeks = Math.floor(days / 7);
    var daysMod = days % 7;
    
    var ret=" ";
    if(weeks > 0)
        ret += weeks+" week"+(weeks>1?"s":"")+" ";
    if(daysMod > 0)
        ret += daysMod+" day"+(days>1?"s":"")+" ";
    ret += pad(hoursMod)+":"+pad(minutesMod)+":"+pad(secondsMod);
    if(ret.endsWith(" "))
    	ret = ret.substring(0, ret.length-1);
    if(ret.startsWith(" "))
    	ret = ret.substring(1);
    return ret;
}

function talkToString(talk){
    if(talk == null)
        return "";
    var start = new Date(talk.start);
    var end = new Date(talk.end);
    var durationString;
    if(start.getTime() > now){
        durationString = " (in " + duration(start.getTime() - now)+")";
    }else{
        durationString = " (" + duration(end.getTime() - now)+" remaining)";
    }
    var markup = "<div class='talk'>";

    // Title
    markup += "<div class='talkDetails-title'>" + talk.title + "</div>";

    // Slot
    markup += "<div class='talkDetails-slot-hour'>"
            + "    <i class='far fa-clock talkDetails-icon'></i>"
            +      formatTime(start) + " - " + formatTime(end) + durationString
            + "</div>";

    // Theme 
    if (talk.theme) {
        markup += "<span class='talk-theme talk-theme-" + talk.themeColor + "'>" + talk.theme + "</span>";
    }

    // Level
    if (talk.level) {
        markup += "<span class='talk-level'>" + talk.level + "</span>";
    }

    //markup += "<div class='description'>"+talk.description+"</div>";

    // Speakers
    markup += "<div class='talkDetails-speakers'>";
    for(var n = 0; n < talk.speakers.length; n++) {
        var speaker = talk.speakers[n];
        markup += "<div class='talkDetails-speaker'>";
            markup += "<div class='talkDetails-speaker-photo' style='background-image: url(" + speaker.photo + ")'></div>";
            markup += "<div class='talkDetails-speaker-name'>" + speaker.name + "</div>";
        markup += "</div>";
    }
    markup += "</div>";

    // End of the div .talk
    markup += "</div>";
    return markup;
}

jQuery(function(){
	showTalks();
	window.setInterval(function(){
		now = new Date().getTime() + nowOffset;
		showTalks();
	}, 1000);
	window.setInterval(function(){
		jQuery.get("http://rivieradev.fr");
	}, 1000 *60*30 );
});
