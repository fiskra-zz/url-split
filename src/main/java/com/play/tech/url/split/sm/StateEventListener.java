package com.play.tech.url.split.sm;

import java.net.URL;

import com.play.tech.url.split.model.Url;

/**
 * This interface is a listener class to provide events for parsing operations.
 * 
 * @author feride
 *
 */
public interface StateEventListener {

	void onEventParseScheme(URL aURL, Url urlState) throws Exception;
    void onEventParseHost(URL aURL, Url urlState) throws Exception;
    void onEventParsePort(URL aURL, Url urlState) throws Exception;
    void onEventParsePath(URL aURL, Url urlState) throws Exception;
    void onEventParseParam(URL aURL, Url urlState) throws Exception;
    
}
