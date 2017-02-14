$(function() {

    window.toggleTheme = function (theme){
        toggleThemeFilterClass($('.fullSchedule-talk-theme-' + theme));
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
		}
	}
});