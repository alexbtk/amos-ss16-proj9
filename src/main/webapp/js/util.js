/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/

function setCookie(cname, cvalue, exdays) {
	    var d = new Date();
	    d.setTime(d.getTime() + (exdays*24*60*60*1000));
	    var expires = "expires="+d.toUTCString();
	    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i = 0; i < ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
}

function createMap(targetDiv, places){  
	
	var mapContainer = $('<div id="mapContainer" style="height:400px;width:500px;"></div>');
	$("#dialog").html(mapContainer).css("width","600px");
	
    var platform = new H.service.Platform({
      'app_id': 'yYBoMh2Svi76MblKinwE',
      'app_code': '-t0xRquwdpgj53W13QMmCg'
    });
    var defaultLayers = platform.createDefaultLayers();

    // Instantiate (and display) a map object:
    var map = new H.Map(
      document.getElementById('dialog').childNodes[0],
      defaultLayers.normal.map,
        {
          zoom: 1,
          center: { lat: 0, lng: 0 }
        });

    var ui = H.ui.UI.createDefault(map, defaultLayers);

    for(var i in places){
      var circle = new H.map.Circle({lat: places[i]['lat'], lng: places[i]['long']}, 118000);
      map.addObject(circle);

      var bubble = new H.ui.InfoBubble({ lng: places[i]['lat'], lat: places[i]['long'] }, {
        content: places[i]['name']
      });

      // Add info bubble to the UI:
      //ui.addBubble(bubble);
    }
    
    
        
  }
