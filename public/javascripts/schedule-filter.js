/**
 * It is possible to have simultaneously 1 conference and 2 snorkelings.
 * In this case, we wants the 2 snorkelings to have the same height, which half of the height of the conference.
 * It's impossible to do it in CSS because we use tables.
 * That's why we do it in JS.
 */
const resizeTalks = function() {
    const rows = document.getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const cells = row.getElementsByClassName('js-fullSchedule-talk-cell');
        if (cells.length > 0) {
            // First iteration to find the max height for each "row" of talks
            const maxHeightPerIndex = new Map();
            let heightParentCell = 0;
            for (let j = 0; j < cells.length; j++) {
                const cell = cells[j];
                heightParentCell = cell.offsetHeight;
                const talks = cell.getElementsByClassName(
                    'js-fullSchedule-talk-item'
                );
                if (talks.length > 1) {
                    for (let k = 0; k < talks.length; k++) {
                        const currentMaxHeight = maxHeightPerIndex.get(k)
                            ? maxHeightPerIndex.get(k)
                            : talks[k].offsetHeight;
                        maxHeightPerIndex.set(
                            k,
                            Math.max(currentMaxHeight, talks[k].offsetHeight)
                        );
                    }
                }
            }

            // Second iteration to apply the max height to all talks
            //if (maxHeightPerIndex.size > 0) {
            for (let j = 0; j < cells.length; j++) {
                const cell = cells[j];
                const talks = cell.getElementsByClassName(
                    'js-fullSchedule-talk-item'
                );
                if (talks.length === 1) {
                    // Apply the height of its parent (td)
                    talks[0].style.minHeight = `${heightParentCell}px`;
                } else if (talks.length > 1) {
                    for (let k = 0; k < talks.length; k++) {
                        talks[k].style.minHeight = `${maxHeightPerIndex.get(
                            k
                        )}px`;
                    }
                }
            }
            //}
        }
    }
};

const dataPanelOpened = 'data-panel-opened';
const dataPanelMaxHeight = 'data-max-height';

/**
 * Init the toggle filters panel.
 */
const initToggleFilters = function() {
    let filtersPanel = document.getElementsByClassName('js-talksFilters');
    if (filtersPanel && filtersPanel.length > 0) {
        filtersPanel[0].style.height = null;
        filtersPanel[0].removeAttribute(dataPanelMaxHeight);
        // Test if panel was previously opepend (in case of windows resize)
        let isFiltersPanelOpened = filtersPanel[0].getAttribute(
            dataPanelOpened
        );
        // Calculate max height of the filters panel to be able to do a CSS transition effect
        let maxHeight = filtersPanel[0].getBoundingClientRect().height + 'px';
        // Keep in data attribute the value to alternate between 0 and Max value when the user clicks on button
        filtersPanel[0].setAttribute(dataPanelMaxHeight, maxHeight);
        if (isFiltersPanelOpened) {
            // Window resize, re-init the height
            filtersPanel[0].style.height = maxHeight;
        } else {
            // Per default hide panel
            filtersPanel[0].style.height = 0;
        }
    }
};

const initSchedulePage = function() {
    resizeTalks();
    initToggleFilters();
};

$(function() {
    initSchedulePage();

    window.addEventListener('resize', initSchedulePage);

    window.toggleLanguage = function(language) {
        filterSchedule('language', language, toggleLanguageFilterClass);
    };

    window.toggleType = function(type) {
        filterSchedule('type', type, toggleTypeFilterClass);
    };

    window.toggleTheme = function(theme) {
        filterSchedule('theme', theme, toggleThemeFilterClass);
    };

    window.toggleLevel = function(level) {
        filterSchedule('level', level, toggleLevelFilterClass);
    };

    window.filterSchedule = function(filter, option, filterMethod) {
        // Le filtre sélectionné
        let currentFilter = $(
            '.js-talksFilter-' +
                filter +
                's .js-talksFilter-' +
                filter +
                '-' +
                option
        );

        // Les éléments à filtrer
        let currentElems = $('.js-talksFilter-' + filter + '-' + option);

        if (currentFilter && currentFilter.attr('data-filter-' + filter)) {
            // Si element courant est désactivé alors le réactiver
            currentElems.each(function(index, element) {
                filterMethod(element);
            }, this);
        } else {
            let otherFilters = $(
                '.js-talksFilter-' + filter + 's .js-talksFilter-item'
            ).not('.js-talksFilter-' + filter + '-' + option);
            let otherHiddenFilters = otherFilters.not(
                '[data-filter-' + filter + ']'
            );
            // Si d'autres éléments sont déjà cachés alors on cache l'élément sélectionné
            if (
                otherHiddenFilters.length > 0 &&
                otherHiddenFilters.length !== otherFilters.length
            ) {
                currentElems.each(function(index, element) {
                    filterMethod(element);
                }, this);
                return;
            }

            // Si aucun élément n'est caché alors on cache tous les autres
            let otherElems = $(
                '.js-talksFilter-toFilter .js-talksFilter-item'
            ).not('.js-talksFilter-' + filter + '-' + option);
            if (otherFilters.length > 0) {
                otherFilters.each(function(index, element) {
                    filterMethod(element);
                }, this);
                otherElems.each(function(index, element) {
                    filterMethod(element);
                }, this);
            } else {
                filterMethod($('.js-talksFilter-' + filter + '-' + option));
            }
        }
    };

    var toggleLanguageFilterClass = function(elem) {
        toggleFilterClass(elem, 'data-filter-language', 'hidden');
    };

    var toggleTypeFilterClass = function(elem) {
        toggleFilterClass(elem, 'data-filter-type', 'hidden');
    };

    var toggleThemeFilterClass = function(elem) {
        toggleFilterClass(elem, 'data-filter-theme', 'hidden');
    };

    var toggleLevelFilterClass = function(elem) {
        toggleFilterClass(elem, 'data-filter-level', 'hidden');
    };

    var toggleFilterClass = function(elem, attrName, className) {
        if (elem && !elem.getAttribute(attrName)) {
            elem.setAttribute(attrName, className);
        } else {
            elem.removeAttribute(attrName);
        }
    };

    /**
     * Reset all filters
     */
    window.resetFilters = function() {
        let filters = ['language', 'type', 'theme', 'level'];
        for (var i = 0; i < filters.length; i++) {
            resetFilter(filters[i]);
        }

        // Reset Filters Panel
        document.querySelectorAll('.js-talksFilters input').forEach(elem => {
            elem.checked = false;
        });
    };

    /**
     * Reset a filter
     * @param {string} filterName
     */
    window.resetFilter = function(filterName) {
        if (filterName) {
            $('[data-filter-' + filterName + ']').removeAttr(
                'data-filter-' + filterName
            );
        }
    };

    /**
     * Show/Hide filters pannel
     */
    window.toggleFiltersPannel = () => {
        let filtersPanel = document.getElementsByClassName('js-talksFilters');
        if (filtersPanel && filtersPanel.length > 0) {
            if (
                filtersPanel[0].style.height == '0px' ||
                filtersPanel[0].style.height == '0'
            ) {
                filtersPanel[0].style.height = filtersPanel[0].getAttribute(
                    dataPanelMaxHeight
                );
                filtersPanel[0].setAttribute(dataPanelOpened, true);
            } else {
                filtersPanel[0].style.height = 0;
                filtersPanel[0].removeAttribute(dataPanelOpened);
            }
        }
    };
});
