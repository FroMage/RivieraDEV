$(function() {

    window.toggleTheme = function (theme){
        toggleThemeFilterClass($('.fullSchedule-talk-theme-' + theme));
	}

    window.toggleTheme2 = function (theme){
		// Le filtre sélectionné
		let currentFilter = $('.fullSchedule-filter-themes .fullSchedule-talk-theme-' + theme)
		console.log('Filtre courant:',currentFilter)

		// Les éléments à filtrer
		let currentElems = $('.fullSchedule-talk-theme-' + theme)
		console.log('Elms:',currentElems)

		if(currentFilter && currentFilter.attr('data-filter-theme')){
			// Si element courant est désactivé alors le réactiver
			console.log(theme + " doit être visible");
			currentElems.each(function(index, element) {
				toggleThemeFilterClass(element);
			}, this);
		}
		else{
			console.log(theme + " doit être caché");

			let otherHiddenFilters = $('.fullSchedule-filter-themes .fullSchedule-filter-item[data-filter-theme]').not('.fullSchedule-talk-theme-' + theme)
			let otherFilters = $('.fullSchedule-filter-themes .fullSchedule-filter-item').not('.fullSchedule-talk-theme-' + theme)
			// Si d'autres éléments sont déjà cachés alors on cache l'élément sélectionné
			if(otherHiddenFilters.length > 0 && otherHiddenFilters.length !== otherFilters.length){
				currentElems.each(function(index, element) {
					toggleThemeFilterClass(element);
				}, this);
				return;
			}

			// Si aucun éléments n'est cachés alors on cache tous les autres

			let otherElems = $('.fullSchedule-talk .fullSchedule-filter-item').not('.fullSchedule-talk-theme-' + theme)
			if(otherFilters.length > 0){
				otherFilters.each(function(index, element) {
					toggleThemeFilterClass(element);
				}, this);
				otherElems.each(function(index, element) {
					toggleThemeFilterClass(element);
				}, this);
			}
			else{
				toggleThemeFilterClass($('.fullSchedule-talk-theme-' + theme));
			}
			console.log('Elms:',currentElems)
		}


		// Sinon {
			// Si aucun élément désactivé on désactive tous les autres
			// Sinon on désactive l'élément courant
		//}

    }

    window.toggleLevel = function (level){
        toggleLevelFilterClass($('.fullSchedule-talk-level-' + level));
    }

    var toggleThemeFilterClass = function (elem){
		toggleFilterClass(elem, 'data-filter-theme', 'hidden');
	}
	
	var toggleLevelFilterClass = function (elem){
		toggleFilterClass(elem, 'data-filter-level', 'hidden');
	}
	
    var toggleFilterClass = function(elem, attrName, className){
        if(elem && (!elem.attr(attrName))){
            elem.attr(attrName,className);
		}
        else{
            elem.removeAttr(attrName,className);
            elem.removeAttribute(attrName);
        }
    }

	var toggleFilterClass2 = function(elem, attrName, className){
		if(elem && (!elem.getAttribute(attrName))){
			elem.setAttribute(attrName,className);
		}
		else{
			elem.removeAttribute(attrName);
		}
	}
});