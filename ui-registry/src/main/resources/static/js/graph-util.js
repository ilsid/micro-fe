GraphUtil = {
	
	// This is a slight adaptation of this code: 
	// https://gist.github.com/fancellu/2c782394602a93921faff74e594d1bb1
	drawGraph: function(graph) {
		var colors = d3.scaleOrdinal(d3.schemeCategory10);
		var svg = d3.select('#graphView');
		
		var width = +svg.attr('width');
	    var height = +svg.attr('height');
	    
	    svg.selectAll('*').remove();
	    
	    svg.append('defs').append('marker')
	    .attrs({'id':'arrowhead',
	        'viewBox':'-0 -5 10 10',
	        'refX':13,
	        'refY':0,
	        'orient':'auto',
	        'markerWidth':13,
	        'markerHeight':13,
	        'xoverflow':'visible'})
	    .append('svg:path')
	    .attr('d', 'M 0,-4 L 10,0 L 0,4')
	    .attr('fill', '#C0C0C0')
	    .style('stroke','none');
	    
	    var simulation = d3.forceSimulation()
		    .force('link', d3.forceLink().id(function (d) {return d.id;}).distance(200).strength(1))
		    .force('charge', d3.forceManyBody())
		    .force('center', d3.forceCenter(width / 2, height / 2));
	    
	    var graphCopy = StdUtil.deepCopy(graph);
	    
	    buildGraph(graphCopy.links, graphCopy.nodes);
	    
//	    d3.json('sample_graph.json').then(function(graph) {
//		    buildGraph(graph.links, graph.nodes);
//		});
		
		function buildGraph(links, nodes) {
	        link = svg.selectAll('.link')
		        .data(links)
		        .enter()
		        .append('path')
		        .attr('class', 'link')
		        .attr('stroke', '#C0C0C0')
		        .attr('stroke-width', '1')
		        .attr('fill', 'none')
		        .attr('marker-end','url(#arrowhead)')
		        .attr('id', function (d, i) {return 'linkpath' + i;});

	        linkLabels = svg.selectAll('.linkLabel')
	            .data(links)
	            .enter()
	            .append('text')
	            .style('pointer-events', 'none')
	            .attrs({
	                'class': 'linkLabel',
	                'id': function (d, i) {return 'linkLabel' + i},
	                'font-size': 12
	            });

	        linkLabels.append('textPath')
	            .attr('href', function (d, i) {return '#linkpath' + i})
	            .style('text-anchor', 'middle')
	            .style('pointer-events', 'none')
	            .attr('startOffset', '50%')
	            .text(function (d) {return d.message});

	        node = svg.selectAll('.node')
	            .data(nodes)
	            .enter()
	            .append('g')
	            .attr('class', 'node')
	            .call(d3.drag()
	                    .on('start', onDragStart)
	                    .on('drag', onDrag)
	            );

	        node.append('circle')
	            .attr('r', 7)
	            .style('fill', function (d, i) {return colors(i);})

	        node.append('title')
	            .text(function (d) {return d.id;});

	        node.append('text')
	            .attr('dy', -8)
	            .text(function (d) {return d.name;});

	        simulation
	            .nodes(nodes)
	            .on('tick', onTick);

	        simulation.force('link')
	            .links(links);
	    }
	    
	    function onTick() {
	        link
	        	.attr('d', function (d) {
	        			var x1 = d.source.x, y1 = d.source.y, x2 = d.target.x, y2 = d.target.y;
	        			
	        			var angleRad = (x2 != x1) ? 
			        					Math.atan(Math.abs((y2 - y1)) / Math.abs((x2 - x1))) : 
			        					Math.PI / 2;
			        	var angleDeg = angleRad * 180 / Math.PI				
						var length = Math.abs(y2 - y1) / Math.sin(angleRad);			
	        			var xOffset = Math.min(length / 2, 100) + (50 - Math.abs(angleDeg - 45));
	        			var yOffset = Math.min(length / 2, 100) - (50 - Math.abs(angleDeg - 45));
	        			var qx = (x2 >= x1) ? (x2 - xOffset) : (x2 + xOffset);
	        			var qy = (y2 >= y1) ? (y2 - yOffset) : (y2 + yOffset);
	        			
	        			return 'M' + x1 + ',' + y1 + ' ' + 
	        				   'Q' + qx + ',' + qy + ' ' +
	        				   x2 + ',' + y2;
	        		});

	        node
	            .attr('transform', function (d) {return 'translate(' + d.x + ', ' + d.y + ')';});

	        linkLabels.attr('transform', function (d) {
	            if (d.target.x < d.source.x) {
	                var bbox = this.getBBox();

	                rx = bbox.x + bbox.width / 2;
	                ry = bbox.y + bbox.height / 2;
	                return 'rotate(180 ' + rx + ' ' + ry + ')';
	            }
	            else {
	                return 'rotate(0)';
	            }
	        });
	    }

	    function onDragStart(d) {
	        if (!d3.event.active) simulation.alphaTarget(0.3).restart()
	        d.fx = d.x;
	        d.fy = d.y;
	    }

	    function onDrag(d) {
	        d.fx = d3.event.x;
	        d.fy = d3.event.y;
	    }

	}, 	

	
	removeNodeAndLinks: function(graph, name) {
		function hasEqualName(element, index, array) {
			return element.name == name;
		}
		
		var nodeId = graph.nodes.find(hasEqualName).id;
		StdUtil.removeByPropertyValue(graph.nodes, 'id', nodeId);
		StdUtil.removeByPropertyValue(graph.links, 'source', nodeId);
		StdUtil.removeByPropertyValue(graph.links, 'target', nodeId);
		this.drawGraph(StdUtil.deepCopy(graph));
	},
	
	
	emptyGraph: function() {
		return { nodes: [],	links: [] };
	}
}


