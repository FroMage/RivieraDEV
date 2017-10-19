$(function() {
    // TODO aller chercher ces dates dans la BD plutôt qu'en dur dans le JS
    var begin = '2018/05/02 08:20:00';
    var end = '2018/05/04 18:00:00';
    var now = new Date();

    if (now < new Date(begin)) {
        beforeConf(begin);
    } else if (now < new Date(end)) {
        whileConf();
    } else {
        afterConf();
    }

});

function beforeConf(begin) {
    var format = 
      '<span class="countdown-day">'
    + '    <span class="countdown-number">%D</span>'
    + '    <span class="countdown-desc">' + countDownDaysLabel + '</span>'
    + '</span>'
    + '<span class="countdown-hour">'
    + '    <span class="countdown-number">%H</span>'
    + '    <span class="countdown-desc">' + countDownHoursLabel + '</span>'
    + '</span>'
    + '<span class="countdown-min">'
    + '    <span class="countdown-number">%M</span>'
    + '    <span class="countdown-desc">' + countDownMinutesLabel + '</span>'
    + '</span>'
    + '<span class="countdown-sec">'
    + '    <span class="countdown-number">%S</span>'
    + '    <span class="countdown-desc">' + countDownSecondsLabel + '</span>'
    + '</span>';

    $('#js-countdown').countdown(begin, function(event) {
        $(this).html(event.strftime(format));
    }).on('finish.countdown', function(event) {
        whileConf();
    });

    // DEBUG
    //$('#js-countdown').countdown("pause");
}

function whileConf() {
    $('#js-countdown').empty();
    // TODO mettre un message stylé genre "C'est maintenant !"
    updateControlsAfterCountdown();
}

function afterConf() {
    $('#js-countdown').empty();
    // TODO mettre un message stylé genre "On vous retrouve l'année prochaine"
    updateControlsAfterCountdown();
}

// TODO dégager ce code ailleurs
function updateControlsAfterCountdown() {
    // Cache les boutons qui se trouvent au dessus du compteur
    $('.js-registration-buttons').empty();
    // Ne met plus le menu "Billeterie" en avant
    $(".js-navbar-link-primary .label").removeClass('label').removeClass('label-primary');
}