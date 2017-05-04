
// var now = new Date();
var now = Date.parse("Thu, 11 May 2017 10:21:00 +0200");

function showTalks(){
    var current = null;
    var currentOtherTracks = [];
    var next = null;
    var nextOtherTracks = [];
    for(var t in tracks){
        var track = tracks[t];
        for(var i=0;i<track.length;i++){
            var talk = track[i];
            if(talk.start <= now && talk.end >= now){
                if(talk.track == showTrack){
                    current = talk;
                } else {
                    currentOtherTracks.push(talk);
                }
            } else if(talk.start > now) {
                if(talk.track == showTrack){
                    if(next == null || next.start > talk.start){
                        next = talk;
                    }
                }else{
                    var addIt = true;
                    for(var n=0;n<nextOtherTracks.length;n++){
                        if(nextOtherTracks[n].track == talk.track){
                            if(nextOtherTracks[n].start > talk.start){
                                nextOtherTracks.splice(n, 1);
                            }else{
                                addIt = false;
                            }
                            break;
                        }
                    }
                    if(addIt)
                        nextOtherTracks.push(talk);
                }
            }
        }
    }

    // Display the current date
    $(".js-slides-currentDate").append(formatDate(new Date(now)) + " " + formatTime(new Date(now)));
    
    var t = jQuery("#target");
    t.empty();
    
    // Current track
    var markup = "<div class='row'>";
    // -- Current talk
    markup += "<div class='current col-md-6'>" + talkToString(current) + "</div>";
    // -- Next talk
    if(next){
        markup += "Coming next in this track:";
        markup += "<div class='next col-md-6'>" + talkToString(next) + "</div>";
    }
    markup += "</div>";

    // Other tracks

    // -- Current talks
    if (currentOtherTracks.length > 0) {
        markup += "<div class='current-other'>";
        markup += "Currently in other tracks:";
        markup += "<div class='row'>";
        for(var n = 0; n < currentOtherTracks.length; n++) {
            var talk = currentOtherTracks[n];
            markup += "<div class='col-md-3'>";
            markup +=     "<h2 class='schedule-day fullSchedule-day schedule-day2'>"+talk.track+"</h2>";
            markup +=     talkToString(talk);
            markup += "</div>";
        }
        markup += "</div>";
        markup += "</div>";
    }

    // -- Next talks
    if (nextOtherTracks.length > 0) {
        markup += "<div class='next-other'>";
        markup += "Next in other tracks:";
        markup += "<div class='row'>";
        for (var n = 0; n < nextOtherTracks.length; n++) {
            var talk = nextOtherTracks[n];
            markup += "<div class='col-md-3'>";
            markup +=     "<h2 class='schedule-day fullSchedule-day schedule-day2'>"+talk.track+"</h2>";
            markup +=     talkToString(talk);
            markup += "</div>";
        }
        markup += "</div>";
        markup += "</div>";
    }

    t.append(markup);
}
var months = [
"Janvier",
"Février",
"Mars",
"Avril",
"Mai",
"Juin",
"Juillet",
"Août",
"Septembre",
"Octobre",
"Novembre",
"Decembre",
];
function formatDate(date){
    return pad(date.getDate()) + " " + months[date.getMonth()];
}
function formatTime(date){
    return pad(date.getHours()) + ":" + pad(date.getMinutes());
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
    
    var ret="";
    if(weeks > 0)
        ret += weeks+" weeks ";
    if(daysMod > 0)
        ret += daysMod+" days ";
    if(hoursMod > 0)
        ret += hoursMod+" hours ";
    if(minutesMod > 0)
        ret += minutesMod+" minutes ";
    if(secondsMod > 0)
        ret += secondsMod+" seconds";
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
            + "    <i class='fa fa-clock-o talkDetails-icon' aria-hidden='true'></i>"
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
});
