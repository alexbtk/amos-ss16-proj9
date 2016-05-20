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
				}else if(displayMode == "simple"){
					div = $("<div id = 'accordion'></div>");
					for(var i=0; i < res.length; ++i){
						div.append($("<h2></h2>").append(res[i]['title']));
						div.append($("<div></div>").append(res[i]['content']));
					}
					obj.html(div);
				}
			});
	}
	
	$("#submitQuestion").click(function(){
		companyName = $( "#companyInput" ).val();
		params = {};
		$( "#principalForm input[type=checkbox]" ).each(function(){
			if(this.checked){
				params[$(this).attr('name')] = companyName;
				//$("#displayAnswers").append($(this).attr('name'));				
			}
		});
		sentRequest(params,"simple","displayAnswers");
		return false;
	});
	
});