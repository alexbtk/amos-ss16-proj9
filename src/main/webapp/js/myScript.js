/*
 *
 * AMOS-SS16-PROJ9
 *
 * Released under the AGPL license
 * Date: 2016-06-11
 */



$(document)
		.ready(
				function() {
					var tags = [];

					$("#login").hide();
					$("#welcome").hide();

					if (getCookie("apiKey") == "") {
						$("#content").hide();
						$("#welcome").hide();
						$("#logoutButton").hide();
						$("#searchMenu").hide();
						$("#login").show();
					}

					// Set credetials in login field
					$("#apiKey").val(getCookie("apiKey"));
					$("#toneAnalyzerUsername").val(
							getCookie("toneAnalyzerUsername"));
					$("#toneAnalyzerPassword").val(
							getCookie("toneAnalyzerPassword"));
					$("#twitterConsumerKey").val(
							getCookie("twitterConsumerKey"));
					$("#twitterConsumerSecret").val(
							getCookie("twitterConsumerSecret"));
					$("#twitterToken").val(getCookie("twitterToken"));
					$("#twitterTokenSecret").val(
							getCookie("twitterTokenSecret"));

					$("#loginButton").on(
							"click",
							function() {
								// set cookies
								setCookie("apiKey", $("#apiKey").val(), 1);
								setCookie("toneAnalyzerUsername", $(
										"#toneAnalyzerUsername").val(), 1);
								setCookie("toneAnalyzerPassword", $(
										"#toneAnalyzerPassword").val(), 1);
								setCookie("twitterConsumerKey", $(
										"#twitterConsumerKey").val(), 1);
								setCookie("twitterConsumerSecret", $(
										"#twitterConsumerSecret").val(), 1);
								setCookie("twitterToken", $("#twitterToken")
										.val(), 1);
								setCookie("twitterTokenSecret", $(
										"#twitterTokenSecret").val(), 1);

								// hide login show container
								$("#login").fadeOut(500, function() {
									$("#logoutButton").fadeIn(500);
									$("#welcome").fadeIn(500);
								});
							});

					$("#welcomeButton").on(
							"click",
							function() {
								$("#dashboardCompanyInput").val(
										$("#welcomeCompanyInput").val());
								$("#avgNewsSentimentGraphSlider input").val($("#advancedOptions div input").val());
								$("#avgNewsSentimentGraphSlider input").change();
								$("#welcome").fadeOut(500, function() {
									$('.contentSection').hide();
									$('#companySection').show();
									$('#searchMenu').fadeIn(500);
									$("#content").fadeIn(500);
								});
							});

					$("#logoutButton").on("click", function() {
						$("#logoutButton").fadeOut(500);
						$("#searchMenu").fadeOut(500);
						$("#content").fadeOut(500, function() {
							// Clear form
							/*
							 * $("#apiKey").val("");
							 * $("#toneAnalyzerUsername").val("");
							 * $("#toneAnalyzerPassword").val("");
							 * $("#twitterConsumerKey").val("");
							 * $("#twitterConsumerSecret").val("");
							 * $("#twitterToken").val("");
							 * $("#twitterTokenSecret").val("");
							 * 
							 * //Remove credentials setCookie("apiKey", "", 1);
							 * setCookie("toneAnalyzerUsername", "", 1);
							 * setCookie("toneAnalyzerPassword", "", 1);
							 * setCookie("twitterConsumerKey", "", 1);
							 * setCookie("twitterConsumerSecret", "", 1);
							 * setCookie("twitterToken", "", 1);
							 * setCookie("twitterTokenSecret", "", 1);
							 */

							$("#login").fadeIn(500);
						});
					});
					$(".companyInput").autocomplete({
						source : function(request, response) {
							response(tags);
						}
					});

					$(".companyInput").keyup(function() {
						var id = "#" + $(this).attr('id');
						var name = $(id).val();
						if (name.length > 2) {

							var param = {};
							param["Text"] = $(id).val();
							$.post("getCompanies", param).done(function(data) {
								var res = JSON.parse(data);
								if (res["companies"].length > 0) {
									// alert(res["companies"][1]);
									tags = res["companies"];
									$(id).autocomplete("option", {
										source : tags
									});
									$(id).autocomplete("search", name);
								} else {
									tags = [];
									$(id).autocomplete("option", {
										source : tags
									});
									$(id).autocomplete("search", name);
								}
							});
						}
					});

					$("#searchButton").click(function() {
						alert("TODO: reload content!");
					});
					
					$("#avgNewsSentimentGraphSlider input").change(function(){
						$("#avgNewsSentimentGraphSlider p").html(
								"Weeks for News Sentiment Graph: " + 
								$("#avgNewsSentimentGraphSlider input").val() +
								" Weeks");
					});
					
					$("#advancedOptions a").click(function(){
						$("#advancedOptions div").slideToggle(300);
					});
					
					$("#advancedOptions div input").change(function(){
						$("#advancedOptions div p").html(
								"Default Timeframe: " + 
								$("#advancedOptions div input").val() +
								" Weeks");
					});
					
					$("#avgNewsSentimentGraphSlider button").click(function(){
						$.post("qeuryRequest", {
							"avgNewsSentimentGraph" : $("#dashboardCompanyInput").val(),
							"avgNewsSentimentGraphWeeks" : $("#avgNewsSentimentGraphSlider input").val()
						}).done(
								function(data) {
									res = JSON.parse(data);
									
									var values = "";
									var d = [];
									var d0 = {};
									d0["data"] = [];
									var i = 0;
									res[i]['values'].forEach(function(entry) {
										values = values + entry + " ";
										d0["data"].push({
											"x" : (-1 * i),
											"y" : entry
										});
										i++;
									});
									d0["label"] = 'Average News Sentiment';
									d0["strokeColor"] = '#F16220';
									d0["pointColor"] = '#F16220';
									d0["pointStrokeColor"] = '#fff';
									d.push(d0);

									var options = {};

									var avgNewsChart = new Chart(
											$("#avgNewsSentimentGraphCanvas")[0]
													.getContext('2d')).Scatter(d, options);

								});
					});
					
					$("#productTabs").tabs();
					$(".contentSection").hide();
				});

/**
 * Display competitors into a popup.
 * 
 * @param host -
 *            the object where we add the popup
 * @param colapseB -
 *            the template of the popup
 * @param industryName -
 *            title of the popup
 * @param companiesName -
 *            array of string
 */
function displayIndustryCompetitors(host, colapseB, industryName, companiesName) {
	colapseB.find(".box-title").text(industryName);
	for ( var i in companiesName)
		colapseB.find(".box-body").append(
				'<input class="' + host.attr("id") + '_checkbox' + '" type="checkbox" name="' + companiesName[i]
						+ '" value="' + companiesName[i] + '">'
						+ companiesName[i] + '</input>' + '<br/>');
	host.append(colapseB);
}

/**
 * Display the section when clicking on the tab.
 * 
 * @param id -
 *            section id
 */
function openSection(id) {
	$('.contentSection').hide();
	$('#' + id + 'Section').show();

	if (id == "competitors") {

		var companyName = $("#dashboardCompanyInput").val();
		if (companyName == "") {
			alert("No company!!");
			return;
		}
		if ($('#' + id + 'Section').find("#existCompetitorsIndustry").length > 0) {
			if ($('#' + id + 'Section').find("#existCompetitorsIndustry").attr(
					"class") == companyName)
				return;
		}
		var colapseB = $($("#template").html());
		var host = $('#competitorsSection');

		host.html("loading...");

		$.post("qeuryRequest", {
			"industriesCompetitors" : companyName
		}).done(
				function(data) {
					data = JSON.parse(data);
					console.log(data);
					host.empty();
					host.append("<div id='existCompetitorsIndustry' class='"
							+ companyName + "'></div>");
					host.append("<div id='competitorBoxes'></div>");
					var marckersC = [];
					for ( var i in data){
						displayIndustryCompetitors($("#competitorBoxes"), colapseB.clone(),
								data[i]['name'], data[i]['comp']);
						for(var j in data[i]['markers'])
							marckersC.push({latLng: data[i]['markers'][j]['latLng'], name: data[i]['markers'][j]['cName'] });
					}
					console.log(marckersC);
					host.append('<div id="world-map-markers-competitors" style="height: 325px;"></div>');
					$('#world-map-markers-competitors').vectorMap({
					    map: 'world_mill_en',
					    normalizeFunction: 'polynomial',
					    hoverOpacity: 0.7,
					    hoverColor: false,
					    backgroundColor: 'transparent',
					    regionStyle: {
					      initial: {
					        fill: 'rgba(210, 214, 222, 1)',
					        "fill-opacity": 1,
					        stroke: 'none',
					        "stroke-width": 0,
					        "stroke-opacity": 1
					      },
					      hover: {
					        "fill-opacity": 0.7,
					        cursor: 'pointer'
					      },
					      selected: {
					        fill: 'yellow'
					      },
					      selectedHover: {}
					    },
					    markerStyle: {
					      initial: {
					        fill: '#00a65a',
					        stroke: '#111'
					      }
					    },
					    markers: marckersC
					  });
					
					host.append(
							"<div id='competitorsSentimentGraphSlider'>" +
								"<p>Weeks for Competitors Sentiment Graph: 7 Weeks</p>" +
								"<input type='range' value='7' min='2' max='52'/><br />" +
							"</div> <div id='competitorsSentimentGraph'> <button>Draw Graph!</button>" +
							"<canvas id='competitorsSentimentGraphCanvas'></canvas></div>");
					
					$("#competitorsSentimentGraphSlider input").change(function(){
						$("#competitorsSentimentGraphSlider p").html(
								"Weeks for News Sentiment Graph: " + 
								$("#competitorsSentimentGraphSlider input").val() +
								" Weeks");
					});
					
					$("#competitorsSentimentGraphSlider input").val($("#advancedOptions div input").val());
					
					$("#competitorsSentimentGraph button").click(function(){
						var colors = ["#fff275", "#ff8c42", "#ff3c38", "#a23e48", "#6c8ead"];
						var colorIndex = 0;
						var checkedCompetitors = [];
						
						//competitorBoxes_checkbox
						$( ".competitorBoxes_checkbox" ).each(function( index ) {
							  if($(this).prop("checked"))
								  checkedCompetitors.push($(this).val());
						});
						
						var d = [];
						
						checkedCompetitors.forEach(function(entry) {
							$.post("qeuryRequest", {
								"avgNewsSentimentGraph" : entry,
								"avgNewsSentimentGraphWeeks" : $("#competitorsSentimentGraphSlider input").val()
							}).done(
									function(data) {
										res = JSON.parse(data);

										var d0 = {};
										d0["data"] = [];
										var i = 0;
										res[i]['values'].forEach(function(e) {
											d0["data"].push({
												"x" : (-1 * i),
												"y" : e
											});
											i++;
										});
										d0["label"] = entry;
										d0["strokeColor"] = colors[colorIndex];
										d0["pointColor"] = colors[colorIndex];
										d0["pointStrokeColor"] = '#fff';
										d.push(d0);
										
										if(d.length == checkedCompetitors.length){
											//Draw graph
											var options = {};

											var avgNewsChart = new Chart(
													$("#competitorsSentimentGraphCanvas")[0]
															.getContext('2d')).Scatter(d, options);
										}
									});
							
							colorIndex = (colorIndex+1) % 5;
						});
					});
				});
	} else if (id == "company") {
		var companyName = $("#dashboardCompanyInput").val();
		if (companyName == "") {
			alert("No company!!");
			return;
		}
		if ($('#' + id + 'Section').find("#existCompanyQuery").length > 0) {
			if ($('#' + id + 'Section').find("#existCompanyQuery")
					.attr("class") == companyName)
				return;
		}
		var colapseB = $($("#template").html());
		var host = $('#companySection');
		if($('#' + id + 'Section').find("#existCompanyQuery").length == 0)
			host.append("<div id='existCompanyQuery' class='"+companyName+"'></div>");
		else $('#' + id + 'Section').find("#existCompanyQuery").attr("class",companyName);
		
		//boxSentimentReview
		var sentimentReview = $("#boxSentimentReview").html();
		
		if($("#sentimentQuery").length == 0){
			host.append(sentimentReview);
			$('.slider').slider();
		    
		    $("#sentimentQuery").find("input[type=checkbox]").each(function(){
		      $(this).change(function(){
		        if($(this).is(":checked")){
		          $(this).parent().next().next().find("input").slider('setValue', 100);
		         }else{
		          $(this).parent().next().next().find("input").slider('setValue', 0);
		         }
		      })
		    }) ;
		    $("#boxSentimentReviewmakeQuery").click(function(){
		    	var twitter = parseInt($("#sentimentQuery").find("#twiterSlider").val());
		    	var news = parseInt($("#sentimentQuery").find("#newsSlider").val());
		    	$.post("qeuryRequest", {
					"question6" : companyName,
				}).done(
						function(data) {
							var res = JSON.parse(data);						
							console.log(res);
							var valT = res[0]['twiter'];
							var valN = res[0]['news'];
							var reSe = 2.;
							reSe = (valT*twitter)/100. + (valN*news)/100;
							if(valT != 0 && valN != 0)
								reSe /= 2.0;
							$('#sentimentResult').slider('setValue', reSe);
						});
		    });
		}
		

		// Company locations on map
		$
				.post("qeuryRequest", {
					"companyLocations" : companyName
				})
				.done(
						function(data) {
							data = JSON.parse(data);
							console.log(data);
							if($('#world-map').length == 0)
							host
									.append("<div id=\"world-map\" style=\"width: 600px; height: 400px\"></div>");

							var vecmap = $('#world-map').vectorMap({
								map : 'world_mill',
								scaleColors : [ '#C8EEFF', '#0071A4' ],
								normalizeFunction : 'polynomial',
								hoverOpacity : 0.7,
								hoverColor : false,
								markerStyle : {
									initial : {
										fill : '#F8E23B',
										stroke : '#383f47'
									}
								},
								backgroundColor : '#383f47',
								markers : data[0]["markers"],
							});
						});

		// Add Company Relations
		getNewsRelation(companyName);
	} else if (id == "products") {
		var companyName = $("#dashboardCompanyInput").val();
		if (companyName == "") {
			alert("No company!!");
			return;
		}
		if ($('#productsSection #companyProducts')
				.find("#existsProductCompany").length > 0) {
			if ($('#productsSection #companyProducts').find(
					"#existsProductCompany").attr("class") == companyName)
				return;
		}

		var colapseB = $($("#template").html());
		var host = $('#productsSection #companyProducts');

		host.html("loading company products...");

		$
				.post("qeuryRequest", {
					"productsCompany" : companyName
				})
				.done(
						function(data) {
							data = JSON.parse(data);
							host.empty();
							host
									.append("<div id='existsProductCompany' class='"
											+ companyName + "'></div>");
							var checkAll = '<input type="checkbox" name="checkAll" value="checkAll" />Check All<br/>';
							displayIndustryCompetitors(host, colapseB.clone(),
									data[0]['title'], data[0]['content']);
							host.find(".box-body").append(checkAll);

							host
									.find(".box-body")
									.find("input[name=checkAll]")
									.change(
											function() {
												if ($(this).is(':checked')) {
													host
															.find(".box-body")
															.find(
																	'input:checkbox')
															.attr('checked',
																	'checked');
												} else {
													host
															.find(".box-body")
															.find(
																	'input:checkbox')
															.removeAttr(
																	'checked');
												}
											});

						});

		var hostC = $('#productsSection #competitorsProducts');

		hostC.html("loading Competitors Products...");

		$
				.post("qeuryRequest", {
					"productsCompetitors" : companyName
				})
				.done(
						function(data) {
							data = JSON.parse(data);
							hostC.empty();
							var checkAll = '<input type="checkbox" name="checkAll" value="checkAll" />Check All<br/>';
							displayIndustryCompetitors(hostC, colapseB.clone(),
									data[0]['title'], data[0]['content']);
							hostC.find(".box-body").append(checkAll);

							hostC
									.find(".box-body")
									.find("input[name=checkAll]")
									.change(
											function() {
												if ($(this).is(':checked')) {
													hostC
															.find(".box-body")
															.find(
																	'input:checkbox')
															.attr('checked',
																	'checked');
												} else {
													hostC
															.find(".box-body")
															.find(
																	'input:checkbox')
															.removeAttr(
																	'checked');
												}
											});

						});
		// Graph
		var hostG = $('#productsSection #graphProducts');

		var button = $('<button type="button" class="btn btn-block btn-default">Go</button>');
		var slider = $('<div id="avgNewsSentimentGraphSliderProduct"><p>Weeks for News Sentiment Graph: 7 Weeks</p><input type="range" value="7" min="2" max="52"/><br /></div>');
		slider.find("input").val($("#advancedOptions div input").val());
		slider.find("input").change(function(){
			$(this).parent().find('p').html("Weeks for News Sentiment Graph: "+
					$(this).val()+" weeks");
		});
		slider.find("input").change();
		
		button
				.click(function() {
					var avgCP = "0", avgCtP = "0", productsCP = [], productsCtP = [];
					var timeFrame = 2;

					host.find(".box-body").find("input[type=checkbox]").each(
							function() {
								if ($(this).is(':checked')) {
									if ($(this).attr("name") != "checkAll")
										productsCP.push($(this).attr("name"));
								}
							});

					hostC.find(".box-body").find("input[type=checkbox]").each(
							function() {
								if ($(this).is(':checked')) {
									if ($(this).attr("name") != "checkAll")
										productsCtP.push($(this).attr("name"));
								}
							});

					if (hostC.find(".box-body").find("input[name=checkAll]")
							.is(':checked'))
						avgCtP = "1";

					if (host.find(".box-body").find("input[name=checkAll]").is(
							':checked'))
						avgCP = "1";

					timeFrame = slider.find("input").val();
					

					if (isNaN(timeFrame))
						timeFrame = 2;

					$
							.post("qeuryRequest", {
								"avgCompanyProducts" : avgCP,
								"avgCompetitorsProducts" : avgCtP,
								"timeFrame" : timeFrame,
								"productsCP" : productsCP.join(","),
								"productsCtP" : productsCtP.join(",")
							})
							.done(
									function(data) {
										var res = JSON.parse(data);

										var values = "";
										var d = [];
										for (var j = 0; j < res[0]["values"].length; ++j) {
											var d0 = {}, i = 0;
											d0["data"] = [];
											res[0]["values"][j]['values']
													.forEach(function(entry) {
														values = values + entry
																+ " ";
														d0["data"].push({
															"x" : (-1 * i),
															"y" : entry
														});
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

										if ($("#productNewsSentimentGraphCanvas").length == 0)
											hostG.append('<canvas style="width: 60%;height: 60%;" id="productNewsSentimentGraphCanvas"></canvas>');

										var avgNewsChart = new Chart(
												$("#productNewsSentimentGraphCanvas")[0]
														.getContext('2d'))
												.Scatter(d, options);

									});
				});
		hostG.empty().append(slider).append(button);
	} else if (id == "twitter") {
		var companyName = $("#dashboardCompanyInput").val();
		if (companyName == "") {
			alert("No company!!");
			return;
		}

		// Company average twitter sentiment
		$.post("qeuryRequest", {
			"avgTwitterBluemixSentimentPosts" : companyName
		}).done(function(data) {
			data = JSON.parse(data);
			var sentValue = data[0]["avgSentiment"];
			var negPostsArray = data[0]["negPosts"];
			var neutPostsArray = data[0]["neutPosts"];
			var posPostsArray = data[0]["posPosts"];

			//destroy old slider
			var oldSlider = $('#twitterAnswer .slider');
			oldSlider.remove();
			
			// Instantiate a slider
			var mySlider;
			if (!isDefined(mySlider)) {
				mySlider = new Slider("#twitterslider", {
					precision : 2
				});
			}
			mySlider.setValue(Number(sentValue));
			
			var negtab = $('#negsent');
			var neuttab = $('#neutsent');
			var postab = $('#possent');
			
			negtab.empty();
			neuttab.empty();
			postab.empty();
			
			for ( var i in negPostsArray)
				negtab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + negPostsArray[i]["postId"] + "\">+</button>Tweet from User " + negPostsArray[i]["postUser"] +"</p><div id=\"" + negPostsArray[i]["postId"] + "\" class=\"collapse\">" + negPostsArray[i]["postText"] + "</div>");	
				
			for ( var i in neutPostsArray)
				neuttab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + neutPostsArray[i]["postId"] + "\">+</button>Tweet from User " + neutPostsArray[i]["postUser"] +"</p><div id=\"" + neutPostsArray[i]["postId"] + "\" class=\"collapse\">" + neutPostsArray[i]["postText"] + "</div>");	
				
			for ( var i in posPostsArray)
				postab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + posPostsArray[i]["postId"] + "\">+</button>Tweet from User " + posPostsArray[i]["postUser"] +"</p><div id=\"" + posPostsArray[i]["postId"] + "\" class=\"collapse\">" + posPostsArray[i]["postText"] + "</div>");	
				
		});

	}

}
/*
 * Checks if object exists
 */
function isDefined(x) {
	var undefined;
	return x !== undefined;
}
