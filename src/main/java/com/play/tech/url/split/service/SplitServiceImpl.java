package com.play.tech.url.split.service;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.play.tech.url.split.model.Url;
import com.play.tech.url.split.sm.State;
import com.play.tech.url.split.sm.StateMachine;
/**
 * It indicates business layer methods. 
 * 
 * @author feride
 *
 */

@Service("splitService")
public class SplitServiceImpl implements SplitService {
	
	private final static String urlPattern = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*)?)?(#(.*))?";

	public Url splitUrlByRegex(String url) throws Exception{
		return splitOperationByRegex(url);
	}
	
	public Url splitUrlByStateMachine(String url) throws Exception{
		return splitOperationBySM(url);
	}
	
	public Url splitOperationByRegex(String url) throws Exception{
		Url regexUrl = null;
		
		try {	
			Pattern pattern = Pattern.compile(urlPattern);
			Matcher matcher = pattern.matcher(url);
			matcher.find();
			String scheme = matcher.group(2);
			String authority = matcher.group(4);
			String host =  authority.contains(":") ? authority.split(":")[0] : authority;
			String port = authority.contains(":") ? authority.split(":")[1] : null;
			String path = matcher.group(5);
			String params = matcher.group(7);
			regexUrl = new Url(scheme, host, port, path, params);

		} catch (Exception e) {
			throw new Exception("Exception occured while regex parsing operation..."+ e);
		}
		
		
		return regexUrl;
	}

	public Url splitOperationBySM(String url) throws Exception {
		Url urlState = new Url();
		try {

			StateMachine machine = new StateMachine();
			machine.setCurrentState(State.START); // start state
			URL aURL = new URL(url);
			machine.onEventParseScheme(aURL, urlState); // parse scheme operation
			machine.setCurrentState(State.SCHEME);
			machine.onEventParseHost(aURL, urlState); // parse host operation
			machine.setCurrentState(State.HOST);
			machine.onEventParsePort(aURL, urlState); // parse port operation
			machine.setCurrentState(State.PORT);
			machine.onEventParsePath(aURL, urlState);// parse path operation
			machine.setCurrentState(State.PATH);
			machine.onEventParseParam(aURL, urlState); // parse param operation
			
		} catch (Exception e) {
			throw new Exception("Exception occured while state machine parsing operation..."+ e);
		}
		return urlState;
	}
}
