package com.play.tech.url.split.sm;

import java.net.URL;

import com.play.tech.url.split.model.Url;

/**
 * It represents implementation of events on state machine. Basically it checks the state and does
 * corresponding operations.
 *  
 * @author feride
 *
 */
public class StateMachine implements StateEventListener {
	
	public State currentState;

	public void onEventParseScheme(URL aURL, Url urlState) throws Exception {
		if(currentState == State.START)
			currentState.onEventParseScheme(aURL,urlState);		
	}

	public void onEventParseHost(URL aURL, Url urlState) throws Exception {
		if(currentState == State.SCHEME)
			currentState.onEventParseHost(aURL,urlState);

	}

	public void onEventParsePort(URL aURL, Url urlState) throws Exception{
		if(currentState == State.HOST)
			currentState.onEventParsePort(aURL,urlState);

	}

	public void onEventParsePath(URL aURL, Url urlState) throws Exception {
		if(currentState == State.PORT)
			currentState.onEventParsePath(aURL, urlState);
		
	}

	public void onEventParseParam(URL aURL, Url urlState) throws Exception {
		if(currentState == State.PATH)
			currentState.onEventParseParam(aURL, urlState);
		
	}
	
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

}
