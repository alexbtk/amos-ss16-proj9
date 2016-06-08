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
	
	
	 
    var markers = "";
    for(var i in places){
    	
      markers += places[i]['lat'].toString() + "," +  places[i]['long'].toString();    
      if(i<places.length-1)
    	  markers += "|";
    }
    
    var img_url = "https://maps.googleapis.com/maps/api/staticmap?center=0,0&zoom=1&size=400x500&sensor=false&"+
	 "markers=color:blue|label:S|"+ markers +
	 "&key=AIzaSyBGCB5TrhPy-jvQvVgAcDtBaXP7E_BGd90";
    
    $("#dialog").html(mapContainer.html("<img src='"+img_url+"'>")); 
    
    
        
  }
