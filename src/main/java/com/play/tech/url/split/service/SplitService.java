package com.play.tech.url.split.service;

import com.play.tech.url.split.model.Url;

public interface SplitService {

	public Url splitUrlByRegex(String url) throws Exception;

	public Url splitUrlByStateMachine(String url) throws Exception;

}
