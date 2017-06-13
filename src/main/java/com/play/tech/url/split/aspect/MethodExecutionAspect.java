package com.play.tech.url.split.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.play.tech.url.split.model.Url;

/**
 * It represents the method execution time aspect class.
 * <code>aroundMethodExecution</code> method will be executed before and after
 * the original method. It calculates method execution times and calls this
 * method 10000 times.
 * 
 * <code>logAfterThrowingAllMethods</code> method will be executed if an
 * exception occurs.
 * 
 * @author feride
 *
 */
@Aspect
@Component
public class MethodExecutionAspect {

	private static final Logger logger = Logger.getLogger(MethodExecutionAspect.class);

	@Around("execution(* com.play.tech.url.split.service.SplitService.splitUrlByRegex (java.lang.String)) && args (url)"
			+ "|| execution(* com.play.tech.url.split.service.SplitService.splitUrlByStateMachine (java.lang.String)) && args (url)")
	public Object aroundMethodExecution(ProceedingJoinPoint proceedingJoinPoint, String url) throws Throwable {

		Url urlObject = null;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			urlObject = (Url) proceedingJoinPoint.proceed(new Object[] { url });
		}
		Long elapsedTime = System.currentTimeMillis() - start;
		urlObject.setExecutionTime(elapsedTime);
		return urlObject;
	}

	@AfterThrowing(pointcut = "execution(* com.play.tech.url.split.service.SplitService.*(..))", throwing = "ex")
	public void logAfterThrowingAllMethods(Exception ex) throws Throwable {
		logger.error("Exception occured while doing operation" + ex);

	}

}
