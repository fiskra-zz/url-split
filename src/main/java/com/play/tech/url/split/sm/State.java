package com.play.tech.url.split.sm;

import java.net.URL;

import com.play.tech.url.split.model.Url;

/**
 * This class provides states and implements state changing methods.
 * 
 * @author feride
 *
 */
public enum State implements StateEventListener{
	START,SCHEME,HOST,PORT,PATH,PARAM;

	public void onEventParseScheme(URL aURL, Url urlState){
		urlState.setScheme(aURL.getProtocol());
	}

	public void onEventParseHost(URL aURL, Url urlState) {
		urlState.setHost(aURL.getHost());
	}

	public void onEventParsePort(URL aURL, Url urlState) {
		urlState.setPort(aURL.getPort()!= -1 ? Integer.toString(aURL.getPort()) : null);
	}

	public void onEventParsePath(URL aURL, Url urlState) throws Exception {
		urlState.setPath(aURL.getPath());
		
	}

	public void onEventParseParam(URL aURL, Url urlState) throws Exception {
		urlState.setParameters(aURL.getQuery());
		
	}
	

}