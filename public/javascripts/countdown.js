const beforeConf = (
    eventStartDate,
    {
        countDownDaysLabel,
        countDownHoursLabel,
        countDownMinutesLabel,
        countDownSecondsLabel,
    }
) => {
    const format = `<div class="countdown">
        <div class="countdown__item countdown__day">
            <span class="countdown__number">%D</span>
            <span class="countdown__desc">${countDownDaysLabel}</span>
        </div>
        <div class="countdown__item countdown__hour">
            <span class="countdown__number">%H</span>
            <span class="countdown__desc">${countDownHoursLabel}</span>
        </div>
        <div class="countdown__item countdown__min">
            <span class="countdown__number">%M</span>
            <span class="countdown__desc">${countDownMinutesLabel}</span>
        </div>
        <div class="countdown__item countdown__sec">
            <span class="countdown__number">%S</span>
            <span class="countdown__desc">${countDownSecondsLabel}</span>
        </div>
    </div>`;

    $('#js-countdown')
        .countdown(new Date(eventStartDate), function(event) {
            $(this).html(event.strftime(format));
        })
        .on('finish.countdown', function(event) {
            whileConf();
        });

    // DEBUG
    // $('#js-countdown').countdown('pause');
};

const whileConf = () => {
    $('#js-countdown').empty();
    // TODO mettre un message stylé genre "C'est maintenant !"
    updateControlsAfterCountdown();
};

const afterConf = () => {
    $('#js-countdown').empty();
    // TODO mettre un message stylé genre "On vous retrouve l'année prochaine"
    updateControlsAfterCountdown();
};

// TODO dégager ce code ailleurs
const updateControlsAfterCountdown = () => {
    // Cache les boutons qui se trouvent au dessus du compteur
    $('.js-registration-buttons').empty();
    // Ne met plus le menu "Billeterie" en avant
    $('.js-navbar-link-primary .label')
        .removeClass('label')
        .removeClass('label-primary');
};

const initCountdown = (
    { eventStartDate, eventEndDate },
    {
        countDownDaysLabel,
        countDownHoursLabel,
        countDownMinutesLabel,
        countDownSecondsLabel,
    }
) => {
    if (!eventStartDate || !eventEndDate) {
        return;
    }

    const now = new Date();

    if (now < new Date(eventStartDate)) {
        beforeConf(eventStartDate, {
            countDownDaysLabel,
            countDownHoursLabel,
            countDownMinutesLabel,
            countDownSecondsLabel,
        });
    } else if (now < new Date(eventEndDate)) {
        whileConf();
    } else {
        afterConf();
    }
};

export default initCountdown;
