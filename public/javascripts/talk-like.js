$(function() {
    window.likeTalk = function (elem, id){
		let $elem = $(elem);
		let like = $elem.attr('data-like');
		if(like) {
			// User want to no more like this talk
			_unlikeIcon($elem);
			_removeLike(id);
		}
		else {
			// User click on Like
			_likeIcon($elem);
			_addLike(id);
		}
		console.log(id, elem);
	}
		
	// initialiez Liked Talk
	window.initLikedTalks = function () {
		let likes = _getLikes();
		for (var i = 0; i < likes.length; i++) {
			_likeIcon($('[data-talk=' + likes[i] + ']'))
		}
	}

	function _likeIcon($elem){
		let $icon = $elem.find('svg');
		$elem.attr('data-like', true);
		$icon.attr('data-prefix', 'fas');
	}

	function _unlikeIcon($elem){
		let $icon = $elem.find('svg');
		$elem.attr('data-like', null);
		$icon.attr('data-prefix', 'far');
	}

	/**
	 * Add a talk in liked talks
	 * @param {int} id ID of the talk
	 */
	function _addLike(id){
		let likes = _getLikes();
		likes.push(id);
		_setLikes(likes);

		//TODO : Ajax to add like talk
	}

	/**
	 * Remove a talk from liked talks
	 * @param {int} id ID of the talk
	 */
	function _removeLike(id){
		let likes = _getLikes();
		let index = likes.indexOf(id);
		if (index > -1) {
			likes.splice(index, 1);
		}
		_setLikes(likes);

		//TODO : Ajax to remove like talk
	}

	/**
	 * Return array of ID talks liked by the user.
	 * (The information is saved in the local storage of the user)
	 */
	function _getLikes(){
		let likes = JSON.parse(localStorage.getItem('RivieraDEV-2018-Likes'));
		if(!likes){
			// Initialize value
			likes = [];
			_setLikes(likes);
		}
		return likes;
	}

	/**
	 * Save in the local storage an array of ID talks liked by the user
	 * @param {array} likes 
	 */
	function _setLikes(likes){
		localStorage.setItem('RivieraDEV-2018-Likes', JSON.stringify(likes));
	}

});