$(function() {
    window.likeTalk = function (elem, id){
		let like = $(elem).attr('data-like');
		let $icon = $(elem).find('svg');
		if(like) {
			// User want to no more like this talk
			$(elem).attr('data-like', null);
			$icon.attr('data-prefix', 'far');
			localStorage.removeItem('Talk-Like-' + id);
			//TODO : Ajax to remove like talk	
		}
		else {
			// Use click on Like
			$(elem).attr('data-like', true);
			$icon.attr('data-prefix', 'fas');
			localStorage.setItem('Talk-Like-' + id, true);
			//TODO : Ajax to like talk
		}
		console.log(id, elem);
		}
		
		// initialiez Liked Talk
		function initTalkLiked() {
			localStorage.getItem
			let $likes = $('[data-like=true] svg');
			likes.attr('data-prefix', 'fas');
		}

});