StdUtil = {
	removeByPropertyValue: function(array, property, value) {
		array.forEach(function(result, index) {
		    if(result[property] === value) {
		      array.splice(index, 1);
		    }    
		    
		  });
	},
	
	deepCopy: function(obj) {
		return JSON.parse(JSON.stringify(obj));
	},
	
	trimTrailingSlash(str) {
		if (str.substr(str.length - 1) === '/') {
			return str.substr(0, str.length - 1);
	    }
	    
		return str;
	},
	
	trimSlashes(str) {
		var res = str;
		
		if (res.substr(res.length - 1) === '/') {
			res = res.substr(0, res.length - 1);
	    }
		
		if (res[0] === '/') {
			res = res.substr(1);
		}
	    
		return res;
	}

}