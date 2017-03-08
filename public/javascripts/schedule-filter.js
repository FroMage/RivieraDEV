$(function() {

    window.toggleTheme = function (theme){
		filterSchedule('theme',theme, toggleThemeFilterClass)
	}

    window.toggleLevel = function (level){
		filterSchedule('level',level, toggleLevelFilterClass)
    }

    window.filterSchedule = function (filter, option, filterMethod){
		// Le filtre sélectionné
		let currentFilter = $('.js-talksFilter-' + filter + 's .js-talksFilter-' + filter + '-' + option)

		// Les éléments à filtrer
		let currentElems = $('.js-talksFilter-' + filter + '-' + option)

		if(currentFilter && currentFilter.attr('data-filter-' + filter)){
			// Si element courant est désactivé alors le réactiver
			currentElems.each(function(index, element) {
				filterMethod(element);
			}, this);
		}
		else{
			let otherFilters = $('.js-talksFilter-' + filter + 's .js-talksFilter-item').not('.js-talksFilter-' + filter + '-' + option)
			let otherHiddenFilters = otherFilters.not('[data-filter-' + filter + ']')
			// Si d'autres éléments sont déjà cachés alors on cache l'élément sélectionné
			if(otherHiddenFilters.length > 0 && otherHiddenFilters.length !== otherFilters.length){
				currentElems.each(function(index, element) {
					filterMethod(element);
				}, this);
				return;
			}

			// Si aucun éléments n'est cachés alors on cache tous les autres
			let otherElems = $('.js-talksFilter-toFilter .js-talksFilter-item').not('.js-talksFilter-' + filter + '-' + option)
			if(otherFilters.length > 0){
				otherFilters.each(function(index, element) {
					filterMethod(element);
				}, this);
				otherElems.each(function(index, element) {
					filterMethod(element);
				}, this);
			}
			else{
				filterMethod($('.js-talksFilter-' + filter + '-' + option));
			}
		}
    }

    var toggleThemeFilterClass = function (elem){
		toggleFilterClass(elem, 'data-filter-theme', 'hidden');
	}
	
	var toggleLevelFilterClass = function (elem){
		toggleFilterClass(elem, 'data-filter-level', 'hidden');
	}

	var toggleFilterClass = function(elem, attrName, className){
		if(elem && (!elem.getAttribute(attrName))){
			elem.setAttribute(attrName,className);
		}
		else{
			elem.removeAttribute(attrName);
		}
	}
});