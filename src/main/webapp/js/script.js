$(document).ready(function(){
	var tags = [];

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