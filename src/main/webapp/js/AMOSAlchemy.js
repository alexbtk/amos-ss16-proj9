/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/

	
	/**
	 * Make a query to Alchemy News via http
	 * 
	 * @param params - object with query parameters
	 * @param functionCallback - function that will be called when the ajax is finished
	 * @returns
	 */
	function AlchemyNewsQuery(params, functionCallback) {
	    var apiKey = params.apikey;
		var query = "https://gateway-a.watsonplatform.net/calls/data/GetNews?"+ 	
		"apikey="+apiKey+
		"&outputMode=json"+
		"&start="+params.start+  
		"&end="+params.end+
		"&"+ params.criteria.join('&') +
		"&return="+params.return;
	    $.ajax({url: query, success: function(result){
			console.log(result);
			functionCallback(result);
	    }});
	}

	/**
	 * Get the regions and their sentiment 
	 * 
	 * @param result - json with documents from Alchemy
	 * @returns
	 */
	function processNewsSentimentByRegions(result) {
	    if(result['status'] == "OK"){
	    	var regions = {};
			var docs = result['result']['docs'];
			for(var i in docs){
				var entities = docs[i]['source']['enriched']['url']['entities'];
				for(var j in entities){
					if(entities[j]['type'] == "Country"){
						if(typeof regions[entities[j]['text']] == 'undefined')
							regions[entities[j]['text']] = 0;
						regions[entities[j]['text']] += entities[j]['sentiment']['score'];
					}
				}
			}
			console.log(regions);
			var ul = $("<ul></ul>");
			$.each(regions, function(index, value)  {
			      ul.append($("<li></li>").append(index+":"+value));
			});
			$( "#dialog" ).html(ul).attr("title","Product Region Analysis");
			$( "#dialog" ).dialog( "open" );
		}
	}
	
	/**
	 * Create the query parameters, and set callback function for AlchemyNewsQuery
	 * @returns
	 */
	function getNewsSentimentByRegions(name){
		var params = {
			'return':'q.enriched.url.title,q.enriched.url.entities',
			'start':'now-3d',
			'end':'now',
			'criteria':['q.enriched.url.entities.entity.type=O[Product^Technology^OperatingSystem^Facility^FieldTerminology]'
			            ,'q.enriched.url.entities.entity.text=['+name+']'],
			'apikey': getCookie("apiKey")
		};

		AlchemyNewsQuery(params, processNewsSentimentByRegions);
	}
	

