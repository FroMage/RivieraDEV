$(function() {
    var finalDate = '2017/05/11 08:30:00';
    var format = 
      '<span class="countdown-day">'
    + '    <span class="countdown-number">%D</span>'
    + '    <span class="countdown-desc">Jours</span>'
    + '</span>'
    + '<span class="countdown-hour">'
    + '    <span class="countdown-number">%H</span>'
    + '    <span class="countdown-desc">Heures</span>'
    + '</span>'
    + '<span class="countdown-min">'
    + '    <span class="countdown-number">%M</span>'
    + '    <span class="countdown-desc">Minutes</span>'
    + '</span>'
    + '<span class="countdown-sec">'
    + '    <span class="countdown-number">%S</span>'
    + '    <span class="countdown-desc">Secondes</span>'
    + '</span>';

    $('#js-countdown').countdown(finalDate, function(event) {
        $(this).html(event.strftime(format));
    });
});