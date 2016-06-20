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
 			console.log(data);
 			host.empty();
 			host.append("<div id='existCompetitorsIndustry' class='"+companyName+"'></div>");
 			for(var i in data)
 				displayIndustryCompetitors(host, colapseB.clone(), data[i]['name'], data[i]['comp']);
 		});	
 	}
 	else if(id == "company"){
 		var companyName = $("#dashboardCompanyInput").val();
 		if(companyName == ""){
 			alert("No company!!");
 			return;
 		}
 		if($('#'+id+'Section').find("#existAvgNewsSentimentGraph").length > 0){
 			if($('#'+id+'Section').find("#existAvgNewsSentimentGraph").attr("class") == companyName)
 				return;
 		}
 		
 		var colapseB = $($("#template").html());
 	    var host = $('#companySection');  	
 	    host.empty();
 		
 		$.post( "qeuryRequest",  {"avgNewsSentimentGraph":companyName, "avgNewsSentimentGraphWeeks": "7"}).done(function(data){
 			res = JSON.parse(data);
 			host.append("<div id='existAvgNewsSentimentGraph' class='"+companyName+"'></div>");
 			
 			var values = "";
			var d = [];
			var d0 = {};
			d0["data"] = [];
			var i = 0;
			res[i]['values'].forEach(function(entry) {
			    values = values + entry + " ";
			    d0["data"].push({"x" : (-1*i), "y" : entry});
			    i++;
			});
			d0["label"] = 'Average News Sentiment'
			d0["strokeColor"] = '#F16220';
			d0["pointColor"] = '#F16220';
			d0["pointStrokeColor"] = '#fff';
			d.push(d0);
			
			var options = {};
			
			var avgNewsChart = new Chart($("#avgNewsSentimentGraphCanvas")[0].getContext('2d')).Scatter(d, options);
			
 		});	
 		
 		// Company locations on map
 		$.post( "qeuryRequest",  {"companyLocations":companyName}).done(function(data){
 			data = JSON.parse(data);
 			host.append("<div id=\"world-map\" style=\"width: 600px; height: 400px\"></div>");
			
 			var vecmap = $('#world-map').vectorMap({
 			    map: 'world_mill',
 			    scaleColors: ['#C8EEFF', '#0071A4'],
 			    normalizeFunction: 'polynomial',
 			    hoverOpacity: 0.7,
 			    hoverColor: false,
 			    markerStyle: {
 			      initial: {
 			        fill: '#F8E23B',
 			        stroke: '#383f47'
 			      }
 			    },
 			    backgroundColor: '#383f47',
 			    markers: data,
 			  });
 		});
 		
 		// Add Company Relations
 		getNewsRelation(companyName);
 	}else if(id == "products"){
 		var companyName = $("#dashboardCompanyInput").val();
 		if(companyName == ""){
 			alert("No company!!");
 			return;
 		}
 		if($('#productsSection #companyProducts').find("#existsProductCompany").length > 0){
 			if($('#productsSection #companyProducts').find("#existsProductCompany").attr("class") == companyName)
 				return;
 		}
 		
 		
 		var colapseB = $($("#template").html());
 	    var host = $('#productsSection #companyProducts');  

		host.html("loading company products..."); 		
 		
 		$.post( "qeuryRequest",  {"productsCompany":companyName}).done(function(data){
 			data = JSON.parse(data);
 			host.empty();
 			host.append("<div id='existsProductCompany' class='"+companyName+"'></div>");
 			var checkAll = '<input type="checkbox" name="checkAll" value="checkAll" />Check All<br/>'; 			
 			displayIndustryCompetitors(host, colapseB.clone(), data[0]['title'], data[0]['content']);
 			host.find(".box-body").append(checkAll);
 			
 			host.find(".box-body").find("input[name=checkAll]").change(function(){
 				if($(this).is(':checked')){
 					host.find(".box-body").find('input:checkbox').attr('checked','checked');
 				}else{
 					host.find(".box-body").find('input:checkbox').removeAttr('checked');
 				}
 			});
 			
 		});
 		
 	    var hostC = $('#productsSection #competitorsProducts');  	
 	    
 	    hostC.html("loading Competitors Products..."); 
 		
 	    $.post( "qeuryRequest",  {"productsCompetitors":companyName}).done(function(data){
 			data = JSON.parse(data);
 			hostC.empty();
 			var checkAll = '<input type="checkbox" name="checkAll" value="checkAll" />Check All<br/>';
 			displayIndustryCompetitors(hostC, colapseB.clone(), data[0]['title'], data[0]['content']);
 			hostC.find(".box-body").append(checkAll);
 			
 			hostC.find(".box-body").find("input[name=checkAll]").change(function(){
 				if($(this).is(':checked')){
 					hostC.find(".box-body").find('input:checkbox').attr('checked','checked');
 				}else{
 					hostC.find(".box-body").find('input:checkbox').removeAttr('checked');
 				}
 			});
 			
 		});
 	    // Graph
 	    var hostG = $('#productsSection #graphProducts');
 	    
 	    
 	    var button = $('<button type="button" class="btn btn-block btn-default">Go</button>');
 	    var timeTextbox = $("<input type='textbox' name='timeFrame'>");
 	    
 	    button.click(function(){
 	    	var avgCP = "0", avgCtP = "0", productsCP = [], productsCtP = [];
 	    	var timeFrame = 2;
 	    	
 	    	host.find(".box-body").find("input[type=checkbox]").each(function(){
 	    		if($(this).is(':checked')){
 	    			if($(this).attr("name") != "checkAll")
 	    				productsCP.push($(this).attr("name"));
 	    		}
 	    	});
 	    	
 	    	hostC.find(".box-body").find("input[type=checkbox]").each(function(){
 	    		if($(this).is(':checked')){
 	    			if($(this).attr("name") != "checkAll")
 	    				productsCtP.push($(this).attr("name"));
 	    		}
 	    	});
 	    	
 	    	if(hostC.find(".box-body").find("input[name=checkAll]").is(':checked'))
 	    		avgCtP = "1";
 	    	
 	    	if(host.find(".box-body").find("input[name=checkAll]").is(':checked'))
 	    		avgCP = "1";
 	    	 	    	
 	    	timeFrame = timeTextbox.val();
 	    	
 	    	if(isNaN(timeFrame))
 	    		timeFrame = 2; 	    	
 	    	 	    		    	
 	    	$.post( "qeuryRequest",  {
 	    		"avgCompanyProducts":avgCP, 
 	    		"avgCompetitorsProducts":avgCtP,
 	    		"timeFrame":timeFrame,
 	    		"productsCP":productsCP.join(","),
 	    		"productsCtP":productsCtP.join(",")}).done(function(data){
	 	 			var res = JSON.parse(data);
	 	 		
	 	 			var values = "";
	 				var d = [];
	 				for(var j = 0; j<res[0]["values"].length;++j){
		 				var d0 = {},i=0;
		 				d0["data"] = [];		 				
		 				res[0]["values"][j]['values'].forEach(function(entry) {
		 				    values = values + entry + " ";
		 				    d0["data"].push({"x" : (-1*i), "y" : entry});
		 				    i++;
		 				});
		 				d0["label"] = res[0]["values"][j]['title'];
		 				d0["strokeColor"] = '#F16220';
		 				d0["pointColor"] = '#F16220';
		 				d0["pointStrokeColor"] = '#fff';	 				
			 				
		 				d.push(d0);
	 				}
	 				
	 				console.log(res);
	 				
	 				var options = {};
	 				
	 				if($("#productNewsSentimentGraphCanvas").length == 0)
	 					hostG.append('<canvas style="width: 60%;height: 60%;" id="productNewsSentimentGraphCanvas"></canvas>');	 		 	    
	 		 	    
	 				var avgNewsChart = new Chart($("#productNewsSentimentGraphCanvas")[0].getContext('2d')).Scatter(d, options);
	 			 	    		
 	    		});
 	    });
 	   hostG.append($("<p>Weeks</p>").append(timeTextbox)).append(button);
 	}
 	
 }

