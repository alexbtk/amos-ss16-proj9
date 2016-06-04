function drawGraph(data, targetDiv){
	var margin = {top: 20, right: 20, bottom: 30, left: 40},
	width = 760 - margin.left - margin.right,
	height = 400 - margin.top - margin.bottom;

	/* 
	 * value accessor - returns the value to encode for a given data object.
	 * scale - maps value to a visual display encoding, such as a pixel position.
	 * map function - maps from data value to display value
	 * axis - sets up axis
	 */ 

//	setup x 
	var xValue = function(d, i) { return i;}, // data -> value
	xScale = d3.scale.linear().range([0, width]), // value -> display
	xMap = function(d, i) { return xScale(xValue(d, i));}, // data -> display
	//xMap = function(d) {return xScale()};
	xAxis = d3.svg.axis().scale(xScale).orient("bottom");

//	setup y
	var yValue = function(d) { return d["value"];}, // data -> value
	yScale = d3.scale.linear().range([height, 0]), // value -> display
	yMap = function(d) { return yScale(yValue(d));}, // data -> display
	yAxis = d3.svg.axis().scale(yScale).orient("left");

	alert("appending svg to " + targetDiv);
//	add the graph canvas to the body of the webpage
	var svg = d3.select(targetDiv).append("svg")
	.attr("width", width + margin.left + margin.right)
	.attr("height", height + margin.top + margin.bottom)
	.append("g")
	.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

//	add the tooltip area to the webpage
	var tooltip = d3.select(targetDiv).append("div")
	.attr("class", "tooltip")
	.style("opacity", 0);


	// don't want dots overlapping axis, so add in buffer to data domain
	xScale.domain([d3.min(data, xValue), d3.max(data, xValue)+1]);
	yScale.domain([d3.min(data, yValue)-1, d3.max(data, yValue)+1]);

	// x-axis
	svg.append("g")
		.attr("class", "x axis")
		.attr("transform", "translate(0," + height + ")")
		.call(xAxis)
		.append("text")
		.attr("class", "label")
		.attr("x", width)
		.attr("y", -6)
		.style("text-anchor", "end")
		.text("Weeks in past");

	// y-axis
	svg.append("g")
		.style("stroke-width", "1")
		.attr("class", "y axis")
		.call(yAxis)
		.append("text")
		.attr("class", "label")
		.attr("transform", "rotate(-90)")
		.attr("y", 12)
		.style("text-anchor", "end")
		.text("Sentiment");
	
	d3.selectAll(".domain")
		.style({'stroke': 'Black', 'fill': 'none', 'stroke-width': '2px'});

		var i = 0;
		data.forEach(function(entry){
			svg.append("circle").attr("class", "dot")
			.attr("r", 4.0)
			.attr("cx", xMap(entry, i))
			.attr("cy", yMap(entry))
			.style("fill", "black") 
			.on("mouseover", function(d) {
				tooltip.transition()
				.duration(200)
				.style("opacity", .9);
				tooltip.html(entry["value"])
						.style("left", (d3.event.pageX + 5) + "px")
						.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				tooltip.transition()
				.duration(500)
				.style("opacity", 0);
			});
			
			if(i > 0){
				svg.append("line")
					.attr("x1", xMap(data[i-1], i-1))
					.attr("y1", yMap(data[i-1]))
					.attr("x2", xMap(entry, i))
					.attr("y2", yMap(entry))
					.style("stroke", "black");
			}
			
			i++;
		});
	
		
}