/**
 * Build a sunburst using D3 library
 */
function SunburstBuilder(elt) {

	var root;
	var height;
	var width;
	var radius;
	var dataURL;
	var onClickCallback;
	
	this.setRoot = function(elt) {
		this.root = elt;
		return this;
	}
	
	this.setHeight = function(h) {
		this.height = h;
		return this;
	};
	
	this.setWidth = function(w) {
		this.width = w;
		return this;
	}
	
	this.setRadius = function(r) {
		this.radius = r;
		return this;
	}
	
	this.setURL = function(url) {
		this.dataURL = url;
		return this;
	}
	
	this.onClick = function(f) {
		this.onClickCallback = f;
		return this;
	}
	
	this.build = function(data) {
		
		var radius = this.radius ? this.radius : Math.min(this.width, this.height) / 2;
		
		var color = d3.scale.category20c();
		var width = this.width;
		var height = this.height;
		var onClickCallback = this.onClickCallback;
		
		var svg = d3.select(this.root).append("svg")
		    .attr("width", this.width)
		    .attr("height", this.height)
		  .append("g")
		    .attr("transform", "translate(" + this.width / 2 + "," + this.height * .52 + ")")
		  .append("g")
		    .classed("inner", true);
	
		var partition = d3.layout.partition()
		    .sort(null)
		    .size([2 * Math.PI, radius * radius])
		    .value(function(d) { return 1; });
		
		var arc = d3.svg.arc()
		            .startAngle(function(d) { return d.x + Math.PI / 2; })
		            .endAngle(function(d) { return d.x + d.dx + Math.PI / 2; })
		            .innerRadius(function(d) { return Math.sqrt(d.y); })
		            .outerRadius(function(d) { return Math.sqrt(d.y + d.dy); });
	
		var build_from_json = function(root) {
			
			var path = svg.datum(root).selectAll("path")
		      .data(partition.nodes)
		    .enter().append("path")
		      .attr("display", function(d) { return d.depth ? null : "none"; }) // hide inner ring
		      .attr("d", arc)
		      .style("fill", function(d) { 
		    	  console.log(d); 
		    	  var v = d.children ? d : d.parent;
		    	  return color(v.name); 
		      })
		      .style("fill-rule", "evenodd");
		  
		  var textBox = d3.select("svg").selectAll("text")
		      .data([""])
		    .enter().append("text")
		      .attr("transform", "translate(" + (width/2) + ",0)")
		      .attr("text-anchor", "end")
		      .attr("dy", height/2)
		      .attr("text-anchor", "middle")
		      .style("font", "300 "+ (width/36) +"px verdana")
		      .text(String);
		
		  var innerG = d3.selectAll("g.inner");
		
		  d3.selectAll("path").on("click", function (d, i) {
			  
		    var newAngle = - (d.x + d.dx / 2);
		
		    innerG
		      	.transition()
		        .duration(1500)
		        .attr("transform", "rotate(" + (180 / Math.PI * newAngle) + ")");
		
		    path
		      .classed("selected", function (x) { return d.name == x.name; });
		    
		    if (onClickCallback) onClickCallback(d);
		    
		  });
		  
		  d3.selectAll("path").on("mouseover", function (d, i) {
		    		
		    textBox.data([d.name]).text(String);
		    
		  });
			
		};
		
		if (this.dataURL) {
			
			d3.json(this.dataURL, function(error, root) {	
				  build_from_json(root);
			});
			
		}else{
			
			build_from_json(data);
			
		}
		
		d3.select(self.frameElement).style("height", height + "px");
		
	}
	
	return this;
	
}