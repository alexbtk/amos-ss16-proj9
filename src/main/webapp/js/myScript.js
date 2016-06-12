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
	    	colapseB.find(".box-body").append("checkbox1:"+companiesName[i]+'<br/>');    
	    host.append(colapseB);
	}

	var names = ['ion','balon'];

	displayIndustryCompetitors(host,colapseB.clone(),"industry name",names);
	displayIndustryCompetitors(host,colapseB.clone(),"industry name1",names);


});

 function openSection(id){
 	$('.contentSection').hide();
 	$('#'+id+'Section').show();
 }

 function logoutf(){
 	alert(1);
 }