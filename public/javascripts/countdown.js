$(function() {
    var finalDate = '2017/05/11 08:30:00';
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

    $('#js-countdown').countdown(finalDate, function(event) {
        $(this).html(event.strftime(format));
    });

    //$('#js-countdown').countdown("pause");
});