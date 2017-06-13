package com.play.tech.url.split;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.play.tech.url.split.config.AppConfig;
import com.play.tech.url.split.model.Url;
import com.play.tech.url.split.service.SplitService;

/**
 * This is the main class to call all functionalities
 * 
 * @author feride
 *
 */
public class Application {
	
	public static void main(String[] args) throws Exception {
		try {
			BasicConfigurator.configure(); // log4j configuration
			AbstractApplicationContext  context = new AnnotationConfigApplicationContext(AppConfig.class);
			SplitService service = (SplitService) context.getBean("splitService");
			Url regex = service.splitUrlByRegex(args[0]);
			Url state = service.splitUrlByStateMachine(args[0]);
			System.out.println(state);
			print(regex, 'r');
			print(state, 's');
		} catch (Exception e) {
			System.out.println("Unexpected situation occured plase check the link you demand...");
		}
	}
	
	
	public static void print(Url url, char type){
		System.out.print(type == 'r' ? "Regex:" : "State:");
		System.out.println(url.getExecutionTime() + "msec");
	}
    
    
    
}
