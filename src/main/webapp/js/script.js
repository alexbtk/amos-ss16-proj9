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
	
});