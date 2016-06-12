/*
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-06-11
*/

 $( document ).ready(function() {

 	$('.contentSection').hide();
 	$('#companySection').show();

    var colapseB = $($("#template").html());
    var host = $('#competitorsSection');
    

    /**
     * Display competitors into a popup.
     * 
     * @param host - the object where we add the popup
     * @param colapseB - the template of the popup
     * @param industryName - title of the popup
     * @param companiesName - array of string
     */
    function displayIndustryCompetitors(host,colapseB,industryName,companiesName){
	    colapseB.find(".box-title").text(industryName);
	    for(var i in companiesName)
	      	colapseB.find(".box-body").append('<input type="checkbox" name="'+companiesName[i]+'" value="'+companiesName[i]+'">'+companiesName[i]+'<br/>');    
	    host.append(colapseB);
	}

	
	//Todo: set the value from textbox
	var companyName = "Apple Inc.";
	
	$.post( "qeuryRequest",  {"industriesCompetitors":companyName}).done(function(data){
		data = JSON.parse(data);
		host.empty();
		for(var i in data)
			displayIndustryCompetitors(host, colapseB.clone(), data[i]['name'], data[i]['comp']);
	});	
	
});

 /**
  * Display the section when clicking on the tab.
  * 
  * @param id - section id
  */
 function openSection(id){
 	$('.contentSection').hide();
 	$('#'+id+'Section').show();
 }

 /**
  * Logout, show the first screen.
  */
 function logoutf(){
	//ToDo: go to first screen
 	alert("logout!");
 }