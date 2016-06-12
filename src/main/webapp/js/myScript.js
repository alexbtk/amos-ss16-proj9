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

    function displayIndustryCompetitors(host,colapseB,industryName,companiesName){
	    colapseB.find(".box-title").text(industryName);
	    for(var i in companiesName)
	      	colapseB.find(".box-body").append('<input type="checkbox" name="'+companiesName[i]+'" value="'+companiesName[i]+'">'+companiesName[i]+'<br/>');    
	    host.append(colapseB);
	}

	var names = ['Apple','Samsung'];

	displayIndustryCompetitors(host,colapseB.clone(),"Smartphones",names);
	displayIndustryCompetitors(host,colapseB.clone(),"Hardware",names);


});

 function openSection(id){
 	$('.contentSection').hide();
 	$('#'+id+'Section').show();
 }

 function logoutf(){
 	alert(1);
 }