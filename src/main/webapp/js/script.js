/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
$(document).ready(function(){
	var tags = [];

	$( "#login" ).hide();
	$( "#welcome" ).hide();
	
	if(getCookie("apiKey") == ""){
		$( "#content" ).hide();
		$( "#welcome" ).hide();
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
			$( "#welcome" ).fadeIn(500);
		});
	});
	
	$( "#welcomeButton" ).on("click", function(){
		$( "#dashboardCompanyInput" ).val($( "#welcomeCompanyInput" ).val());
		$( "#welcome" ).fadeOut(500, function(){
			$( "#content" ).fadeIn(500);
		});
	});
	
	$( "#logoutButton" ).on("click", function(){
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
	
	
	
	/**
	 * Get companies from one industry
	 * 
	 * @param resource - industry resource
	 */
	function getIndustryCompanies(resource){
		sentRequest({'industryCompanies':'<'+decodeURI(resource)+'>'},"resource","dialog");
	
	}
	
	function showDialog(){
		$( "#dialog" ).dialog({
			autoOpen: false,
			width: 400,
			buttons: [
				{
					text: "Ok",
					click: function() {
						$( this ).dialog( "close" );
					}
				},
				{
					text: "Cancel",
					click: function() {
						$( this ).dialog( "close" );
					}
				}
			]
		});
	}
	showDialog();
	
	/**
	 * General request function that will get information from controller
	 * 
	 * @param params - the parameters that will be sent
	 * @param displayMode - how the got data will be display
	 * @param displayTarget - the id of the div in where the answer will be inserted
	 */
	function sentRequest(params,displayMode,displayTarget){
		$.post( "qeuryRequest",  params).done(function(data){
				//alert(data);
				var res = JSON.parse(data);
				obj = $('#'+displayTarget);
				if(displayMode == "accordion"){
					div = $("<div id = 'accordion'></div>");
					for(var i=0; i < res.length; ++i){
						div.append($("<h2></h2>").append(res[i]['title']));
						div.append($("<div></div>").append(res[i]['content']));
					}
					obj.html(div);
					$( "#accordion" ).accordion();
				}else if(displayMode == "tabs"){
					div = $("<div id = 'tabs'></div>");
					var ul = $("<ul></ul>");
					for(var i=0; i < res.length; ++i){
						if('values' in res[i]){
							ul.append($("<li></li>").append($("<a href='#tabs-"+i+"'></a>").append(res[i]['title'])));
							var values = "";
							var data = [];
							var i = 0;
							res[i]['values'].forEach(function(entry) {
							    values = values + entry + " ";
							    data.push({"time" : "now-"+(i*7)+"d", "value" : entry});
							    i++;
							});
							div.append($("<div id='tabs-"+i+"'></div>"));
							obj.html(div.prepend(ul));
							drawGraph(data, "#tabs-" + i);
						}
						else{
							ul.append($("<li></li>").append($("<a href='#tabs-"+i+"'></a>").append(res[i]['title'])));
							div.append($("<div id='tabs-"+i+"'></div>").append(res[i]['content']));
							obj.html(div.prepend(ul));
						}
					}
					$( "#tabs" ).tabs();
				}else if(displayMode == "simple"){
					div = $("<div id = 'simple'></div>");
					for(var i=0; i < res.length; ++i){
						div.append($("<h3></h3>").append(res[i]['title']));
						div.append($("<p></p>").append(res[i]['content']));
					}
					obj.html(div);
				}else if(displayMode == "resource"){
					div = $("<ul id = 'list'></ul>");
					for(var i=0; i < res.length; ++i){
						div.append('<li style="cursor: pointer;" class="'+encodeURI(res[i]['resource'])+'">'+res[i]['name']+"</li>");
					}
					obj.html(div);
					$( "#dialog" ).dialog( "open" );
					$("#dialog #list li").click(function(){
						var resu = $(this).attr("class");
						getIndustryCompanies(resu);
					});
				}else if(displayMode == "link"){
					div = $("<ul id = 'list'></ul>");
					for(var i=0; i < res[0]['content'].length; ++i){
						div.append('<li style="cursor: pointer;" class="'+res[0]['content'][i]['name']+'">'+res[0]['content'][i]['name']+"</li>");
					}
					obj.html(div);
					$( "#dialog" ).attr("title",res[0]['title']);
					$( "#dialog" ).dialog( "open" );
					$("#dialog #list li").click(function(){
						var productName = $(this).attr("class");
						getNewsSentimentByRegions(productName);
					});
				}else if(displayMode == "return"){
					return res;
				}else if(displayMode == "map"){
					var places = [];
					for(var i=0; i < res[0]['content'].length; ++i){
						var entry = {};
						entry['name'] = res[0]['content'][i]['name'];
						var point = res[0]['content'][i]['point'];
						var two = point.split("&");
						entry['long'] = parseFloat(two[0]);
						entry['lat'] = parseFloat(two[1]);
						places.push(entry);
					}
					console.log(places);
			
					$( "#dialog" ).dialog( "option", "width", 500 ).dialog( "open" );
					createMap('dialog',places);
				}
			});
	}
	
	$("#submitQuestion").click(function(){
		companyName = $( "#dashboardCompanyInput" ).val();
		if(companyName == ""){
			alert("Write company Name");
			return false;
		}
		params = {};
		var ok = 1;
		$( "#principalForm input[type=checkbox]" ).each(function(){
			if(this.checked){
				params[$(this).attr('name')] = companyName;
				
				if($(this).attr('name') == "avgNewsSentimentGraph")
					params["avgNewsSentimentGraphWeeks"] = "7";
				ok = 0;
			}
		});
		
		if($("#oneWeekButton").prop("checked")){
			params['timeframe'] = '1';
		}
		else if($("#twoWeekButton").prop("checked")){
			params['timeframe'] = '2';
		}
		else if($("#fourWeekButton").prop("checked")){
			params['timeframe'] = '4';
		}
		else{
			params['timeframe'] = '1';
		}
		
		if(ok == 1){
			alert("Check a question!");
			return false;
		}
		sentRequest(params,"tabs","displayAnswers");
		return false;
	});
	
	$("#submitAdvancedQuestion").click(function(){
		
		companyName = $( "#dashboardCompanyInput" ).val();
		if(companyName == ""){
			alert("Write company Name");
			return false;
		}
		params = {};
		var ok = 0;
		$( "#principalForm input[type=checkbox]" ).each(function(){
			if(this.checked){
				params[$(this).attr('name')] = companyName;
				++ok;				
			}
		});
		if(ok != 1){
			alert("Check one question!");
			return false;
		}
		if(typeof params['question3a'] !== 'undefined'){
			sentRequest({'industries':companyName},"resource","dialog");
		}
		if(typeof params['question5a'] !== 'undefined'){
			sentRequest({'products':companyName},"link","dialog");
		}
		if(typeof params['question7'] !== 'undefined'){
			sentRequest({'question7':companyName},"map","dialog");
		}
		if(typeof params['question8'] !== 'undefined'){
			getNewsRelation(companyName);
		}
		return false;
	});
	
	/**
	 * Collapse functionality for twitter posts
	 * 
	 */

	$('.row .btn').on('click', function(e) {
	    e.preventDefault();
	    var $this = $(this);
	    var $collapse = $this.closest('.collapse-group').find('.collapse');
	    $collapse.collapse('toggle');
	});
	
	/**
	 * Twitter sentiment slider
	 * 
	 */
	//Instantiate a slider
	var mySlider = new Slider("#twitterslider", {
	    id: "twitterslider"
	});
	
	$("#dummyGraphSubmit").click(function(){
		var values = [-1.2, -1.4, 0.2, 0.4, -0.5, 0.8, 0.4];
		var data = [];
		var i = 0;
		values.forEach(function(entry) {
		    data.push({"time" : "now-"+(i*7)+"d", "value" : entry});
		    i++;
		});
		drawGraph(data, "#avgSentimentGraph");
	});
		
});





