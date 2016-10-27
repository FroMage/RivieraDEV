$(function() {
    var finalDate = '2017/05/11 08:30:00';
    var format = '<span class="countdown-day">%D</span>'
    + '<span class="countdown-hour">%H</span>'
    + '<span class="countdown-min">%M</span>'
    + '<span class="countdown-sec">%S</span>';

    $('#js-countdown').countdown(finalDate, function(event) {
        $(this).html(event.strftime(format));
    });
});