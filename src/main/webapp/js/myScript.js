/*
 *
 * AMOS-SS16-PROJ9
 *
 * Released under the AGPL license
 * Date: 2016-06-11
 */

var colorsGraph= ['#F16220', 'black', 'blue', 'fuchsia', 'gray', 'green', 
			'lime', 'maroon', 'navy', 'olive', 'orange', 'purple', 'red', 
			'silver', 'teal',  'yellow'];

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
					$("#twitterUsername").val(getCookie("twitterUsername"));
					$("#twitterPassword").val(getCookie("twitterPassword"));
					/*$("#toneAnalyzerUsername").val(
							getCookie("toneAnalyzerUsername"));
					$("#toneAnalyzerPassword").val(
							getCookie("toneAnalyzerPassword"));
					$("#twitterConsumerKey").val(
							getCookie("twitterConsumerKey"));
					$("#twitterConsumerSecret").val(
							getCookie("twitterConsumerSecret"));
					$("#twitterToken").val(getCookie("twitterToken"));
					$("#twitterTokenSecret").val(
							getCookie("twitterTokenSecret"));*/

					$("#loginButton").on(
							"click",
							function() {
								// set cookies
								setCookie("apiKey", $("#apiKey").val(), 1);
								setCookie("twitterUsername", $("#twitterUsername").val(), 1);
								setCookie("twitterPassword", $("#twitterPassword").val(), 1);
								/*setCookie("toneAnalyzerUsername", $(
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
										"#twitterTokenSecret").val(), 1);*/

								// hide login show container
								$("#login").fadeOut(500, function() {
									$("#logoutButton").fadeIn(500);
									$("#welcome").fadeIn(500);
								});
								
								//reload content after logout
								if($('#productsSection #companyProducts').find("#existsProductCompany").length > 0)
									$('#productsSection #companyProducts').find("#existsProductCompany").remove();
								if($('#competitorsSection').find("#existCompetitorsIndustry").length > 0)
									$('#competitorsSection').find("#existCompetitorsIndustry").remove();
								if($('#companySection').find("#existCompanyQuery").length>0)
									$('#companySection').find("#existCompanyQuery").remove();
								$("#alchelmystatusprogress").addClass("progress-bar-green").removeClass("progress-bar-red");
								$("#twitterstatusprogress").addClass("progress-bar-green").removeClass("progress-bar-red");
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
								//$("#companyMoreitem").click();
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
							 */
							 //Remove credentials 
							 setCookie("apiKey", " ", 1);
							 setCookie("twitterUsername", " ", 1);
							 setCookie("twitterPassword", " ", 1);
							 /*setCookie("toneAnalyzerUsername", "", 1);
							 setCookie("toneAnalyzerPassword", "", 1);
							 setCookie("twitterConsumerKey", "", 1);
							 setCookie("twitterConsumerSecret", "", 1);
							 setCookie("twitterToken", "", 1);
							 setCookie("twitterTokenSecret", "", 1);*/
							 

							$("#login").fadeIn(500);
						});
					});
					$(".companyInput").autocomplete({
						source : function(request, response) {
							response(tags);
						}
					}).autocomplete( "widget" ).css("z-index","10000000000 !important");//.addClass( "autocompletecostumclass" );

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
						$("#companyMoreitem").click();
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
				
					// hide the content
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
			if ($('#competitorsSection').find("#existCompetitorsIndustry").attr(
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
									}).fail( function(xhr, textStatus, errorThrown) {
								    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
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
			if ($('#companySection').find("#existCompanyQuery")
					.attr("class") == companyName)
				return;
		}
		
		// news graph sentiment> company vs competitors
		
		$.post("qeuryRequest", {
				"productEmployeesCompetitors" : companyName,
		}).done(
					function(data) {
						res = JSON.parse(data);						
						var toCompare = [companyName];
						for(var ii = 0;ii<2 && ii < res[0].length;++ii)
							if(res[0][ii] != "" && res[0][ii] != '' && res[0][ii] != null)
								toCompare.push(res[0][ii]);
						var d = [];
						console.log("top");
						console.log(toCompare);
						for(var ii in toCompare){
							$.post("qeuryRequest", {
								"avgNewsSentimentGraph" : toCompare[ii],
								"avgNewsSentimentGraphWeeks" : $("#advancedOptions div input").val()
							}).done(
									function(data) {
										res = JSON.parse(data);
										var values = "";
										
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
										var idColor = d.length;
										if(d.length >= colorsGraph.length) 
											idColor = 0;
										d0["label"] = toCompare[d.length];									
										d0["strokeColor"] = colorsGraph[idColor];
										d0["pointColor"] = colorsGraph[idColor];
										d0["pointStrokeColor"] = colorsGraph[idColor];
										d.push(d0);				
										
										if(d.length == toCompare.length){
											var options = {};
											var avgNewsChartC = new Chart(
													$("#avgNewsSentimentGraphCanvasComparationC")[0]
															.getContext('2d')).Scatter(d, options);
										}
									}).fail( function(xhr, textStatus, errorThrown) {
								    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
								    });
						};
				}).fail( function(xhr, textStatus, errorThrown) {
			    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
			    });
		
		
		
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
		    	var twitter = parseFloat($("#sentimentQuery").find("#twiterSlider").val());
		    	var news = parseFloat($("#sentimentQuery").find("#newsSlider").val());
							
				var valT = parseFloat($("#sentimentQuery #twitterValues").text());
				var valN = parseFloat($("#sentimentQuery #newsValues").text());
				var reSe = 2.;
				reSe = ((valT*twitter)/100.) + 4. + ((valN*news)/100.) + 4.;	
				
				reSe /= 2.0;
				$('#sentimentResult').slider('setValue', (reSe-4.));
			
		    });		    
		    
		}
		
		$.post("qeuryRequest", {
			"question6" : companyName,
		}).done(
					function(data) {
						var res = JSON.parse(data);		
						console.log(res);
						var valN = res[0]['news'];
						$("#sentimentQuery #newsValues").text(valN);
					
	    }).fail( function(xhr, textStatus, errorThrown) {
	    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
	    });
		
		$.post("qeuryRequest", {
			"question6h" : companyName,
		}).done(
					function(data) {
						console.log(data);
						var res = JSON.parse(data);	
						
						console.log(res);
						var valT = res[0]['twiter'];
						$("#sentimentQuery #twitterValues").text(valT);
					
	    }).fail( function(xhr, textStatus, errorThrown) {
	    	 $("#twitterstatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
	    });
		
		$("#sentimentQuery").find("input[type=checkbox]").each(function(){
		      $(this).attr("checked","checked");
		});
		
		$("#boxSentimentReviewmakeQuery").click();
		
		return;
		
		// Company locations on map
		$
				.post("qeuryRequest", {
					"companyLocations" : companyName
				})
				.done(
						function(data) {
							data = JSON.parse(data);
							console.log(data);
							$("#mapforcompany").empty().append("<div id=\"world-map\" style=\"width: 600px; height: 400px\"></div>");

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
						}).fail( function(xhr, textStatus, errorThrown) {
					    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
					    });

		// Add Company Relations
		//getNewsRelation(companyName);	
		$.post("qeuryRequest", {
			"recentDev" : companyName,
		}).done(
					function(data) {
						var res = JSON.parse(data);		
						console.log("recent devs");
						console.log(res);
						var ul = $("<ul></ul>");
						for(var i=0;i<res[0]['content'].length;++i){
							ul.append($("<li></li>").append($("<a href='"+res[0]['content'][i]['url']+"' target='_blank'></a>").text(res[0]['content'][i]['sentence'])));
						}
						var colapseBa = $($("#template").html());
				 	    var hostDe = $('#companySection #recentDevelopments');  
				 	    colapseBa.find(".box-title").text("Recent Developments");
				 	    colapseBa.find(".box-body").append(ul);
				 	    hostDe.empty().append(colapseBa);
					
	    }).fail( function(xhr, textStatus, errorThrown) {
	    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
	    });
		
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

									}).fail( function(xhr, textStatus, errorThrown) {
								    	 $("#alchelmystatusprogress").removeClass("progress-bar-green").addClass("progress-bar-red");
								    });
				});
		hostG.empty().append(slider).append(button);
	} else if (id == "twitter") {
		var companyName = $("#dashboardCompanyInput").val();
		if (companyName == "") {
			alert("No company!!");
			return;
		}
		//document.addEventListener("DOMContentLoaded", domReady);
		//function domReady(){
			
			
			/*
			var twitterChart = document.getElementById("newsChart");
			var twitterChartdata = {
				    labels: [
				        "Red",
				        "Blue",
				        "Yellow"
				    ],
				    datasets: [
				        {
				            data: [300, 50, 100],
				            backgroundColor: [
				                "#FF6384",
				                "#36A2EB",
				                "#FFCE56"
				            ],
				            hoverBackgroundColor: [
				                "#FF6384",
				                "#36A2EB",
				                "#FFCE56"
				            ]
				        }]
				};
			var twitterPieChart = new Chart(twitterChart,{
			    type: 'pie',
			    data: twitterChartdata
			});
			*/
		//};
		
		

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
			
			var negRetweetOver100 = $('#negOver100');
			var negRetweetUnder100 = $('#negUnder100');
			var negRetweetUnder10 = $('#negUnder10');
			
			var posRetweetOver100 =  $('#posOver100');
			var posRetweetUnder100 = $('#posUnder100');
			var posRetweetUnder10 = $('#posUnder10');
			
			var neutRetweetOver100 =  $('#neutOver100');
			var neutRetweetUnder100 = $('#neutUnder100');
			var neutRetweetUnder10 = $('#neutUnder10');
			
			negRetweetOver100.empty();
			negRetweetUnder100.empty();
			negRetweetUnder10.empty();
			
			posRetweetOver100.empty();
			posRetweetUnder100.empty();
			posRetweetUnder10.empty();
			
			neutRetweetOver100.empty();
			neutRetweetUnder100.empty();
			neutRetweetUnder10.empty();
			
			for ( var i in negPostsArray)
				var retweetCount = negPostsArray[i]["postRetweeted"];
				if (Number(retweetCount) > 100){
					negRetweetOver100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + negPostsArray[i]["postId"] + "\">+</button>Tweet from User " + negPostsArray[i]["postUser"] +"</p><div id=\"" + negPostsArray[i]["postId"] + "\" class=\"collapse\">" + negPostsArray[i]["postText"] + "</div>");
				} else if (Number(retweetCount) <= 100 && Number(retweetCount) > 10){
					negRetweetUnder100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + negPostsArray[i]["postId"] + "\">+</button>Tweet from User " + negPostsArray[i]["postUser"] +"</p><div id=\"" + negPostsArray[i]["postId"] + "\" class=\"collapse\">" + negPostsArray[i]["postText"] + "</div>");
				}else {
					negRetweetUnder10.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + negPostsArray[i]["postId"] + "\">+</button>Tweet from User " + negPostsArray[i]["postUser"] +"</p><div id=\"" + negPostsArray[i]["postId"] + "\" class=\"collapse\">" + negPostsArray[i]["postText"] + "</div>");
				}
				//negtab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + negPostsArray[i]["postId"] + "\">+</button>Tweet from User " + negPostsArray[i]["postUser"] +"</p><div id=\"" + negPostsArray[i]["postId"] + "\" class=\"collapse\">" + negPostsArray[i]["postText"] + "</div>");	
				
			for ( var i in neutPostsArray)
				var retweetCount = neutPostsArray[i]["postRetweeted"];
				if (Number(retweetCount) > 100){
					neutRetweetOver100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + neutPostsArray[i]["postId"] + "\">+</button>Tweet from User " + neutPostsArray[i]["postUser"] +"</p><div id=\"" + neutPostsArray[i]["postId"] + "\" class=\"collapse\">" + neutPostsArray[i]["postText"] + "</div>");
				} else if (Number(retweetCount) <= 100 && Number(retweetCount) > 10){
					neutRetweetUnder100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + neutPostsArray[i]["postId"] + "\">+</button>Tweet from User " + neutPostsArray[i]["postUser"] +"</p><div id=\"" + neutPostsArray[i]["postId"] + "\" class=\"collapse\">" + neutPostsArray[i]["postText"] + "</div>");
				}else {
					neutRetweetUnder10.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + neutPostsArray[i]["postId"] + "\">+</button>Tweet from User " + neutPostsArray[i]["postUser"] +"</p><div id=\"" + neutPostsArray[i]["postId"] + "\" class=\"collapse\">" + neutPostsArray[i]["postText"] + "</div>");
				}
				//neuttab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + neutPostsArray[i]["postId"] + "\">+</button>Tweet from User " + neutPostsArray[i]["postUser"] +"</p><div id=\"" + neutPostsArray[i]["postId"] + "\" class=\"collapse\">" + neutPostsArray[i]["postText"] + "</div>");	
				
			for ( var i in posPostsArray)
				var retweetCount = posPostsArray[i]["postRetweeted"];
				if (Number(retweetCount) > 100){
					posRetweetOver100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + posPostsArray[i]["postId"] + "\">+</button>Tweet from User " + posPostsArray[i]["postUser"] +"</p><div id=\"" + posPostsArray[i]["postId"] + "\" class=\"collapse\">" + posPostsArray[i]["postText"] + "</div>");
				} else if (Number(retweetCount) <= 100 && Number(retweetCount) > 10){
					posRetweetUnder100.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + posPostsArray[i]["postId"] + "\">+</button>Tweet from User " + posPostsArray[i]["postUser"] +"</p><div id=\"" + posPostsArray[i]["postId"] + "\" class=\"collapse\">" + posPostsArray[i]["postText"] + "</div>");
				}else {
					posRetweetUnder10.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + posPostsArray[i]["postId"] + "\">+</button>Tweet from User " + posPostsArray[i]["postUser"] +"</p><div id=\"" + posPostsArray[i]["postId"] + "\" class=\"collapse\">" + posPostsArray[i]["postText"] + "</div>");
				}
				//postab.append("<p><button type=\"button\" class=\"btn btn-xs\" data-toggle=\"collapse\" data-target=\"#" + posPostsArray[i]["postId"] + "\">+</button>Tweet from User " + posPostsArray[i]["postUser"] +"</p><div id=\"" + posPostsArray[i]["postId"] + "\" class=\"collapse\">" + posPostsArray[i]["postText"] + "</div>");
			
			$.jqplot.config.enablePlugins = true;
			var plot1 = $.jqplot('twitterChart', [[['negative sentiments',negPostsArray.length],['neutral sentiments',neutPostsArray.length],['positive sentiments',posPostsArray.length]]], {
				grid: {
		            drawBorder: false, 
		            drawGridlines: false,
		            background: '#ffffff',
		            shadow:false
		        },
				gridPadding: {top:0, bottom:38, left:0, right:0},
				
		        seriesDefaults:{
		            renderer:$.jqplot.PieRenderer,
		            seriesColors: ["#e50202", "#eefd00", "#04ff08"],
		            trendline:{ show:false }, 
		            rendererOptions: { padding: 8, showDataLabels: true }
		        },
		        legend:{
		            show:true, 
		            placement: 'outside', 
		            rendererOptions: {
		                numberRows: 2
		            }, 
		            location:'s',
		            marginTop: '15px'
		        }       
		    });
			
		});
		
		$.post("qeuryRequest", {
			"question4" : companyName,
			"timeframe" : 2
		}).done(
					function(data) {
						var res = JSON.parse(data);		
						var newsSentimentCountString = res[0]['content'];
						var numberPattern = /\d+/g;
						var numbers = newsSentimentCountString.match(numberPattern);
						//alert(numbers[0]);
						//alert(numbers[1]);
						var plot2 = $.jqplot('newsChart', [[['negative news',numbers[0]],['positive news',numbers[1]]]], {
							grid: {
					            drawBorder: false, 
					            drawGridlines: false,
					            background: '#ffffff',
					            shadow:false
					        },
							gridPadding: {top:0, bottom:38, left:0, right:0},
							
					        seriesDefaults:{
					            renderer:$.jqplot.PieRenderer,
					            seriesColors: ["#e50202", "#04ff08"],
					            trendline:{ show:false }, 
					            rendererOptions: { padding: 8, showDataLabels: true }
					        },
					        legend:{
					            show:true, 
					            placement: 'outside', 
					            rendererOptions: {
					                numberRows: 2
					            }, 
					            location:'s',
					            marginTop: '15px'
					        }       
					    });
					
	    });
				
		
		};

	}


/*
 * Checks if object exists
 */
function isDefined(x) {
	var undefined;
	return x !== undefined;
}
