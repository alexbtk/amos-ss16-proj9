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
	function AlchemyNewsQuery(params, functionCallback,name) {
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
			functionCallback(result,name);
	    }});
	}

	/**
	 * Get the regions and their sentiment 
	 * 
	 * @param result - json with documents from Alchemy
	 * @returns
	 */
	function processNewsSentimentByRegions(result,_p) {
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
	
	function processNewsRelation(result,name){
		if(result['status'] == "OK"){
	    	var actions = {};
			var docs = result['result']['docs'];
			var rel = $("<ul><ul/>");
			for(var i in docs){
				var relations = docs[i]['source']['enriched']['url']['enrichedTitle']['relations'];
				for(var j in relations){
					if((name.toLowerCase()).indexOf((relations[j]['subject']['text'].toLowerCase()))!=-1){
						if(typeof actions[relations[j]['action']['verb']['text']] == 'undefined')
							actions[relations[j]['action']['verb']['text']] = [{
								"action":relations[j]['action']['text'],
								"object":relations[j]['object']['text'],
								"url" : docs[i]['source']['enriched']['url']['url']
							}];
						else
						actions[relations[j]['action']['verb']['text']].push({
								"action":relations[j]['action']['text'],
								"object":relations[j]['object']['text'],
								"url" : docs[i]['source']['enriched']['url']['url']
							});
					}
				}
			}
			$.each(actions,function(key,val){
				var ul = $("<ul></ul>");
				for(var i in val)
					ul.append($("<li></li>").append($("<a href='"+val[i]['url']+"' target='_blank'></a>").text(val[i]['action']+" "+val[i]['object'])));
				rel.append($("<li></li>").html(ul));
			});
			console.log(actions);
			//$( "#dialog" ).html(rel).dialog( "open" );
			var colapseB = $($("#template").html());
	 	    var host = $('#companySection');  
	 	    colapseB.find(".box-title").text("Recent Developments");
	 	    colapseB.find(".box-body").append(rel);
	 	    host.append(colapseB);
		}
	}

    function getNewsRelation(name){
		var params = {
			'name':name,
			'return':'q.enriched.url.enrichedTitle.relations,q.enriched.url.relations,q.enriched.url.url',
			'start':'now-3d',
			'end':'now',
			'criteria':[
			//	'q.enriched.url.entities.entity.type=Country',
				'q.enriched.url.enrichedTitle.relations.relation.subject.entities.entity.text=O['+name.trim()+"^"+name.trim().replace(" ", "^")+"]",
				'q.enriched.url.enrichedTitle.relations.relation.subject.entities.entity.type=Company'],
			'apikey': getCookie("apiKey")
		};

		AlchemyNewsQuery(params, processNewsRelation, name);
	}
    
	

