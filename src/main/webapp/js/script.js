$(document).ready(function(){
	var tags = [];

	$( "#login" ).hide();
	
	if(getCookie("apiKey") == ""){
		$( "#content" ).hide();
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
			$( "#content" ).fadeIn(500);
		});
	});
	
	$( "#logoutButton" ).on("click", function(){
		$( "#content" ).fadeOut(500, function(){
			//Clear form
			$("#apiKey").val("");
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
			setCookie("twitterTokenSecret", "", 1);
			
			$( "#login" ).fadeIn(500);
		});
	});
	$( "#companyInput" ).autocomplete({
      	source: function(request, response) {
        			response(tags);
				}
	});

	$( "#companyInput" ).keyup(function() {
		var name = $( "#companyInput" ).val();
		if(name.length > 2){
  			var param = {};
  			param["Text"] = $( "#companyInput" ).val();
  			$.post( "getCompanies",  param).done(function(data){
  				var res = JSON.parse(data);
  				if(res["companies"].length > 0){
  					//alert(res["companies"][1]);
  					tags = res["companies"];
  					$('#companyInput').autocomplete("option", { source: tags });
  					$('#companyInput').autocomplete("search", name);
  				}else{
  					tags = [];
  					$('#companyInput').autocomplete("option", { source: tags });
  					$('#companyInput').autocomplete("search", name);
  				}
  			});
  		}
	});
	
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
						ul.append($("<li></li>").append($("<a href='#tabs-"+i+"'></a>").append(res[i]['title'])));
						div.append($("<div id='tabs-"+i+"'></div>").append(res[i]['content']));
					}
					obj.html(div.prepend(ul));
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
				}else if(displayMode == "return"){
					return res;
				}
			});
	}
	
	$("#submitQuestion").click(function(){
		companyName = $( "#companyInput" ).val();
		if(companyName == ""){
			alert("Write company Name");
			return false;
		}
		params = {};
		var ok = 1;
		$( "#principalForm input[type=checkbox]" ).each(function(){
			if(this.checked){
				params[$(this).attr('name')] = companyName;
				ok = 0;
				//$("#displayAnswers").append($(this).attr('name'));				
			}
		});
		if(ok == 1){
			alert("Check a question!");
			return false;
		}
		sentRequest(params,"tabs","displayAnswers");
		return false;
	});
	
	$("#submitAdvancedQuestion").click(function(){
		
		companyName = $( "#companyInput" ).val();
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
				//$("#displayAnswers").append($(this).attr('name'));				
			}
		});
		if(ok != 1){
			alert("Check one question!");
			return false;
		}
		if(typeof params['question3a'] !== 'undefined'){
			sentRequest({'industries':companyName},"resource","dialog");
		}		
		return false;
	});
		
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