$(function() {
    window.likeTalk = function(elem, id) {
        let $elem = $(elem);
        let like = $elem.attr('data-like');
        if (like) {
            // The user doesn't want to like this talk anymore
            _unlikeIcon($elem);
            _removeLike(id);
        } else {
            // The user likes the talk
            _likeIcon($elem);
            _addLike(id);
        }
    };

    // Initialize Liked Talks
    window.initLikedTalks = function() {
        let likes = _getLikes();
        for (var i = 0; i < likes.length; i++) {
            _likeIcon(
                $('.js-talksFilter-like-icon[data-talk=' + likes[i] + ']')
            );
        }
    };

    function _likeIcon($elem) {
        $elem.attr('data-like', true);
    }

    function _unlikeIcon($elem) {
        $elem.attr('data-like', null);
    }

    /**
     * Add a talk in liked talks
     * @param {int} id ID of the talk
     */
    function _addLike(id) {
        let likes = _getLikes();
        likes.push(id);
        _setLikes(likes);

        $.ajax({
            url: '/like-talk/' + id,
            type: 'POST',
            success: function(result) {
                $('.js-talksFilter-like[data-talk=' + id + ']').html(result);
            },
            error: function(result) {},
        });
    }

    /**
     * Remove a talk from liked talks
     * @param {int} id ID of the talk
     */
    function _removeLike(id) {
        let likes = _getLikes();
        let index = likes.indexOf(id);
        if (index > -1) {
            likes.splice(index, 1);
        }
        _setLikes(likes);

        $.ajax({
            url: '/unlike-talk/' + id,
            type: 'POST',
            success: function(result) {
                $('.js-talksFilter-like[data-talk=' + id + ']').html(result);
            },
            error: function(result) {},
        });
    }

    /**
     * Return array of ID talks liked by the user.
     * (The information is saved in the local storage of the user)
     */
    function _getLikes() {
        if (window.localStorage) {
            let likes = JSON.parse(
                localStorage.getItem('RivieraDEV-2023-Likes')
            );
            if (!likes) {
                // Initialize value
                likes = [];
                _setLikes(likes);
            }
            return likes;
        }
        return [];
    }

    /**
     * Save in the local storage an array of ID talks liked by the user
     * @param {array} likes
     */
    function _setLikes(likes) {
        if (window.localStorage) {
            localStorage.setItem(
                'RivieraDEV-2023-Likes',
                JSON.stringify(likes)
            );
        }
    }
});
