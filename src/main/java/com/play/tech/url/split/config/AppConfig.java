package com.play.tech.url.split.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.play.tech.url.split.aspect.MethodExecutionAspect;
import com.play.tech.url.split.service.SplitService;
import com.play.tech.url.split.service.SplitServiceImpl;
/**
 * This configuration class declares <code>@Bean</code> classes.
 * <code>@EnableAspectJAutoProxy</code> enables support for handling components 
 * marked with <code>@Aspect</code> annotation. 
 * 
 * @author feride
 *
 */
@Configuration
@ComponentScan(basePackages = "com.play.tech.url.split")
@EnableAspectJAutoProxy
public class AppConfig {
	
	@Bean
    public SplitService splitService() {
        return new SplitServiceImpl();
    }
	@Bean
	public MethodExecutionAspect methodExecutionProfiler(){
		return new MethodExecutionAspect();
	}

}
