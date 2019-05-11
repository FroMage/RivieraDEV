let now = new Date();

const nowOffset = 0;
// Debugging by adding 2 days and 18 hours and 40 minutes
/* const nowOffset =
    1000 * 60 * 60 * 24 * 2 + 1000 * 60 * 60 * 18 + 1000 * 60 * 40; */

function showTalks() {
    const currentTalks = [];
    const nextTalks = [];

    for (let t in tracks) {
        const track = tracks[t];
        for (let i = 0; i < track.length; i++) {
            const talk = track[i];
            if (talk.start <= now && talk.end >= now) {
                currentTalks.push(talk);
            } else if (talk.start > now) {
                let addIt = true;
                for (let n = 0; n < nextTalks.length; n++) {
                    // only keep the closest talks
                    if (nextTalks[n].start > talk.start) {
                        nextTalks.splice(n, 1);
                    } else if (nextTalks[n].start < talk.start) {
                        addIt = false;
                        break;
                    }
                }
                if (addIt) {
                    nextTalks.push(talk);
                }
            }
        }
    }

    // Display the current date
    document.getElementById('js-live-currentDate').innerHTML =
        formatDate(new Date(now)) + ' ' + formatTimeSeconds(new Date(now));

    const target = document.getElementById('js-target');
    target.innerHTML = '';
    if (currentTalks.length + nextTalks.length > 8) {
        target.classList.add('live__talks--smaller');
    } else {
        target.classList.remove('live__talks--smaller');
    }

    let markup = "<h1 class='live__when'>Currently</h1>";

    // -- Current talks
    if (currentTalks.length > 0) {
        markup += "<div class='live__talks'>";
        for (let n = 0; n < currentTalks.length; n++) {
            const talk = currentTalks[n];
            markup += talkToString(talk, showTrack);
        }
        markup += '</div>'; // .live__talks
    } else {
        markup += "<div class='live__nothing'>";
        markup +=
            "<img src='/public/images/Sal1.png' class='nothing-img col-xs-12'/>";
        markup +=
            '<span>Oh noes!! We haven’ts gots any talks righter now!! :(</span>';
        markup += '</div>'; // .live__nothing
    }

    markup += "<h1 class='live__when'>Next</h1>";

    // -- Next talks
    if (nextTalks.length > 0) {
        markup += "<div class='live__talks'>";
        for (let n = 0; n < nextTalks.length; n++) {
            const talk = nextTalks[n];
            markup += talkToString(talk, showTrack);
        }
        markup += '</div>'; // .live__talks
    } else {
        markup += "<div class='live__nothing'>";
        markup +=
            "<img src='/public/images/Sal2.png' class='nothing-img col-xs-12'/>";
        markup +=
            '<span>Oh noes!! Its a doner, see youse nexts years!! :)</span>';
        markup += '</div>'; // .live__nothing
    }

    target.innerHTML = markup;
}
const months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
];
function formatDate(date) {
    return pad(date.getDate()) + ' ' + months[date.getMonth()];
}
function formatTime(date) {
    return pad(date.getHours()) + ':' + pad(date.getMinutes());
}
function formatTimeSeconds(date) {
    return formatTime(date) + ':' + pad(date.getSeconds());
}
function pad(num) {
    let s = num + '';
    while (s.length < 2) s = '0' + s;
    return s;
}
function duration(time) {
    const seconds = Math.floor(time / 1000);
    const minutes = Math.floor(seconds / 60);
    const secondsMod = seconds % 60;
    const hours = Math.floor(minutes / 60);
    const minutesMod = minutes % 60;
    const days = Math.floor(hours / 24);
    const hoursMod = hours % 24;
    const weeks = Math.floor(days / 7);
    const daysMod = days % 7;

    let ret = ' ';
    if (weeks > 0) ret += weeks + ' week' + (weeks > 1 ? 's' : '') + ' ';
    if (daysMod > 0) ret += daysMod + ' day' + (days > 1 ? 's' : '') + ' ';
    ret += pad(hoursMod) + ':' + pad(minutesMod) + ':' + pad(secondsMod);
    if (ret.endsWith(' ')) ret = ret.substring(0, ret.length - 1);
    if (ret.startsWith(' ')) ret = ret.substring(1);
    return ret;
}

function talkToString(talk) {
    if (talk == null) return '';
    const start = new Date(talk.start);
    const end = new Date(talk.end);
    let durationString;
    if (start.getTime() > now) {
        durationString = ' (in ' + duration(start.getTime() - now) + ')';
    } else {
        durationString = ' (' + duration(end.getTime() - now) + ' remaining)';
    }

    let markup = "<div class='liveTalk__container'>";
    markup += "<h2 class='liveTalk__track'>" + talk.track + '</h2>';
    markup +=
        "<div class='liveTalk" +
        (talk.track == showTrack ? ' liveTalk--currentTrack' : '') +
        "'>";

    markup += "<div class='liveTalk__part1'>";

    // Title
    markup += "<div class='liveTalk__title'>" + talk.title + '</div>';

    // Slot
    markup +=
        "<div class='liveTalk__slot'>" +
        formatTime(start) +
        ' - ' +
        formatTime(end) +
        durationString +
        '</div>'; // .liveTalk__slot

    // Theme
    if (talk.theme) {
        markup += "<span class='liveTalk__theme'>" + talk.theme + '</span>';
    }

    // Type
    if (talk.type) {
        markup += "<span class='liveTalk__type'>" + talk.type + '</span>';
    }

    // Level
    if (talk.level) {
        markup += "<span class='liveTalk__level'>" + talk.level + '</span>';
    }

    // Language
    if (talk.level) {
        markup +=
            "<span class='liveTalk__language'>" + talk.language + '</span>';
    }

    markup += '</div>'; // .liveTalk__part1
    markup += "<div class='liveTalk__part2 ";
    if (talk.theme) {
        markup += 'liveTalk__part2--' + talk.themeColor;
    }
    markup += "'>";

    // Speakers
    markup += "<div class='liveTalk__speakers'>";
    for (let n = 0; n < talk.speakers.length; n++) {
        const speaker = talk.speakers[n];
        markup += "<div class='liveTalk__speaker'>";
        markup +=
            "<div class='liveTalk__speakerPhoto' style='background-image: url(" +
            speaker.photo +
            ")'></div>";
        markup +=
            "<div class='liveTalk__speakerName'>" + speaker.name + '</div>';
        markup += '</div>';
    }
    markup += '</div>'; // .liveTalk__speaker
    markup += '</div>'; // .liveTalk__speakers

    markup += '</div>'; // .liveTalk__part2
    markup += '</div>'; // .liveTalk
    markup += '</div>'; // .liveTalk__container
    return markup;
}

window.onload = function() {
    // now = new Date().getTime() + nowOffset;
    showTalks();
    window.setInterval(function() {
        now = new Date().getTime() + nowOffset;
        showTalks();
    }, 1000);
    window.setInterval(function() {
        jQuery.get('http://rivieradev.fr');
    }, 1000 * 60 * 30);
};
