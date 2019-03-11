$(function() {
    // Mobile Menu -------
    window.initBurgerMenu = () => {
        var burgerMenu = $('.js-burgerMenu-input');
        let burgerMenuOpenClass = 'burgerMenu--open';

        if (burgerMenu && burgerMenu.length > 0)
            burgerMenu[0].addEventListener('change', event => {
                let body = document.getElementsByTagName('body')[0];
                if (event.target.checked) {
                    body.classList.add(burgerMenuOpenClass);
                } else {
                    body.classList.remove(burgerMenuOpenClass);
                }
            });
    };

    // Desktop Menu ------
    var navbar = $('.js-navbar-animated');

    var toggleAnimatedClass = function() {
        var windowWidth = $(window).width(),
            screenXsMax = 767;

        if (windowWidth <= screenXsMax) {
            navbar.removeClass('navbar-animated');
        } else {
            navbar.addClass('navbar-animated');
        }
    };

    var resizeNavbar = function() {
        var distanceY =
                window.pageYOffset || document.documentElement.scrollTop,
            shrinkOn = 300;

        if (distanceY > shrinkOn) {
            navbar.addClass('navbar-animated-small');
        } else {
            if (navbar.hasClass('navbar-animated-small')) {
                navbar.removeClass('navbar-animated-small');
            }
        }
    };

    window.addEventListener('scroll', function(e) {
        resizeNavbar();
    });

    window.addEventListener('resize', function(e) {
        toggleAnimatedClass();
    });

    toggleAnimatedClass();
    resizeNavbar();
});
