// See https://developers.google.com/maps/documentation/javascript/adding-a-google-map
function initMap() {
    // if HTML DOM Element that contains the map is found...
    var element = document.getElementById('location-map');
    if (element) {
        // Coordinates to center the map
        var myLatlng = { lat: 43.613063, lng: 7.056227 };

        // Other options for the map, pretty much selfexplanatory
        var mapOptions = {
            scrollwheel: false,
            zoom: 5, // 16,
            center: myLatlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            mapTypeControlOptions: {
                position: google.maps.ControlPosition.TOP_RIGHT,
            },
        };

        // Attach a map to the DOM Element, with the defined settings
        var map = new google.maps.Map(element, mapOptions);

        var contentString =
            '<div id="content">' +
            '<div id="siteNotice">' +
            '</div>' +
            '<span class="location__mapMarker__addressTitle">SKEMA Business School - Sophia Antipolis</span>' +
            '<span class="location__mapMarker__addressLine">Sophia Antipolis</span>' +
            '<span class="location__mapMarker__addressLine">60 Rue Fedor Dostoïevski</span>' +
            '<span class="location__mapMarker__addressLine">06902 Valbonne</span>' +
            '<div class="view-link"><a target="_blank" href="https://goo.gl/maps/ruuNcjcupC82"> <span>Voir sur Google&nbsp;Maps</span> </a> </div>' +
            '</div>';

        var infowindow = new google.maps.InfoWindow({
            content: contentString,
        });

        var marker = new google.maps.Marker({
            position: myLatlng,
            map: map,
        });
        marker.addListener('click', function() {
            infowindow.open(map, marker);
        });
    }
}

