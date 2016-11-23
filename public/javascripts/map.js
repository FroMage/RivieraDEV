$(function () {

    function initMap(){
        // if HTML DOM Element that contains the map is found...
        var element = document.getElementById('location-map');
        if (element){
        
            // Coordinates to center the map        
            var myLatlng = {lat: 43.613063, lng: 7.056227};
        
            // Other options for the map, pretty much selfexplanatory
            var mapOptions = {
                zoom: 16,
                center: myLatlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
        
            // Attach a map to the DOM Element, with the defined settings
            var map = new google.maps.Map(element, mapOptions);

            var marker = new google.maps.Marker({
                position: myLatlng,
                map: map
            });
        
        }
    }

    initMap();

});