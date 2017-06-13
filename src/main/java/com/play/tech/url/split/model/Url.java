package com.play.tech.url.split.model;
/**
 * It represents the url model. 
 * scheme, host, port, path and parameters
 * 
 * @author feride
 *
 */
public class Url {
	
	private String scheme;

	private String host;

	private String port;

	private String path;

	private String parameters;

	private long executionTime;
	
	public Url() {
		// TODO Auto-generated constructor stub
	}
	
	public Url(String scheme, String host, String port, String path, String parameters) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.parameters = parameters;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.scheme + "\n" + this.host + "\n" + (this.port!=null ? this.port +"\n" : "") + this.path + "\n" + (this.parameters!=null ? this.parameters + "\n" : "" );
	}

}
