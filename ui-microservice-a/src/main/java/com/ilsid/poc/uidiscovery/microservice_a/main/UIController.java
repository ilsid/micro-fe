package com.ilsid.poc.uidiscovery.microservice_a.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Controller
public class UIController {
	
	@RequestMapping("/")
    public String page(){
        return "index.html";
}
	
}
