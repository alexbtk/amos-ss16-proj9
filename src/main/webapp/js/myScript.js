/*
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-06-11
*/

 $( document ).ready(function() {
	 var tags = [];

		$( "#login" ).hide();
		$( "#welcome" ).hide();
		
		if(getCookie("apiKey") == ""){
			$( "#content" ).hide();
			$( "#welcome" ).hide();
			$( "#logoutButton" ).hide();
			$( "#searchMenu" ).hide();
			$( "#login" ).show();
		}
			
		
		//Set credetials in login field
		$("#apiKey").val(getCookie("apiKey"));
		$("#toneAnalyzerUsername").val(getCookie("toneAnalyzerUsername"));
		$("#toneAnalyzerPassword").val(getCookie("toneAnalyzerPassword"));
		$("#twitterConsumerKey").val(getCookie("twitterConsumerKey"));
		$("#twitterConsumerSecret").val(getCookie("twitterConsumerSecret"));
		$("#twitterToken").val(getCookie("twitterToken"));
		$("#twitterTokenSecret").val(getCookie("twitterTokenSecret"));
		
		$( "#loginButton" ).on("click", function(){
			//set cookies
			setCookie("apiKey", $("#apiKey").val(), 1);
			setCookie("toneAnalyzerUsername", $("#toneAnalyzerUsername").val(), 1);
			setCookie("toneAnalyzerPassword", $("#toneAnalyzerPassword").val(), 1);
			setCookie("twitterConsumerKey", $("#twitterConsumerKey").val(), 1);
			setCookie("twitterConsumerSecret", $("#twitterConsumerSecret").val(), 1);
			setCookie("twitterToken", $("#twitterToken").val(), 1);
			setCookie("twitterTokenSecret", $("#twitterTokenSecret").val(), 1);
			
			//hide login show container
			$( "#login" ).fadeOut(500, function(){
				$( "#logoutButton" ).fadeIn(500);
				$( "#welcome" ).fadeIn(500);
			});
		});
		
		$( "#welcomeButton" ).on("click", function(){
			$( "#dashboardCompanyInput" ).val($( "#welcomeCompanyInput" ).val());
			$( "#welcome" ).fadeOut(500, function(){
				$('.contentSection').hide();
			 	$('#companySection').show();
			 	$('#searchMenu').fadeIn(500);
				$( "#content" ).fadeIn(500);
			});
		});
		
		$( "#logoutButton" ).on("click", function(){
			$("#logoutButton").fadeOut(500);
			$("#searchMenu").fadeOut(500);
			$( "#content" ).fadeOut(500, function(){
				//Clear form
				/*$("#apiKey").val("");
				$("#toneAnalyzerUsername").val("");
				$("#toneAnalyzerPassword").val("");
				$("#twitterConsumerKey").val("");
				$("#twitterConsumerSecret").val("");
				$("#twitterToken").val("");
				$("#twitterTokenSecret").val("");
				
				//Remove credentials
				setCookie("apiKey", "", 1);
				setCookie("toneAnalyzerUsername", "", 1);
				setCookie("toneAnalyzerPassword", "", 1);
				setCookie("twitterConsumerKey", "", 1);
				setCookie("twitterConsumerSecret", "", 1);
				setCookie("twitterToken", "", 1);
				setCookie("twitterTokenSecret", "", 1);*/
				
				$( "#login" ).fadeIn(500);
			});
		});
		$( ".companyInput" ).autocomplete({
	      	source: function(request, response) {
	        			response(tags);
					}
		});

		$( ".companyInput" ).keyup(function() {
			var id = "#" + $(this).attr('id');
			var name = $(id).val();
			if(name.length > 2){
				
	  			var param = {};
	  			param["Text"] = $(id).val();
	  			$.post( "getCompanies",  param).done(function(data){
	  				var res = JSON.parse(data);
	  				if(res["companies"].length > 0){
	  					//alert(res["companies"][1]);
	  					tags = res["companies"];
	  					$(id).autocomplete("option", { source: tags });
	  					$(id).autocomplete("search", name);
	  				}else{
	  					tags = [];
	  					$(id).autocomplete("option", { source: tags });
	  					$(id).autocomplete("search", name);
	  				}
	  			});
	  		}
		});
		
		$( "#searchButton" ).click(function(){
			alert("TODO: reload content!");
		});

    
	
});
 
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

 /**
  * Display the section when clicking on the tab.
  * 
  * @param id - section id
  */
 function openSection(id){
 	$('.contentSection').hide();
 	$('#'+id+'Section').show();
 	
 	if(id == "competitors"){

 		var companyName = $("#dashboardCompanyInput").val();
 		if(companyName == ""){
 			alert("No company!!");
 			return;
 		}
 		if($('#'+id+'Section').find("#existCompetitorsIndustry").length > 0){
 			if($('#'+id+'Section').find("#existCompetitorsIndustry").attr("class") == companyName)
 				return;
 		}
 		var colapseB = $($("#template").html());
 	    var host = $('#competitorsSection');  

		host.html("loading..."); 		
 		
 		$.post( "qeuryRequest",  {"industriesCompetitors":companyName}).done(function(data){
 			data = JSON.parse(data);
 			host.empty();
 			host.append("<div id='existCompetitorsIndustry' class='"+companyName+"'></div>");
 			for(var i in data)
 				displayIndustryCompetitors(host, colapseB.clone(), data[i]['name'], data[i]['comp']);
 		});	
 	}
 	
 }

