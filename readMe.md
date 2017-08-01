# Simple Url Split Technical Challenge

[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/fiskra/url-split/master/LICENSE)

*** This content edited. I found more elegant solution for state machine part. I shared code at the end of readMe. 

### How to run

Go to the location of jar file in your win/linux environment. 

``` java -jar urlSplit.jar "test url" ``` 



### Simple Url Split Application

This application aims to split a given link with two different algorithms to parts by user, compare their performance and print the link parts will be like below:

scheme
host
port(optional)
path
parameters(optional)
Regex: 123ms
State: 456ms

This application includes two algorithms:

* The first algorithm will use regular expressions
* The second algorithm will be based on a state machine.

The program will run 10000 iterations of each algorithm.

## Technical Infrastructure

### Language
Java 8

### Build
Maven is the preferential build manager.

### Framework
Spring AOP is used 

### Design Patterns
State Design Pattern

### Logging
log4j (the logging options are defined in `application.properties` in resources dir.)

## Implementation Details

Spring framework has many advantages. It supports all types of application development. In this program,
spring-core, spring-context and spring-aop are selected.

`Application.java` class is our main class to call required methods.
`Url.java` class is a model for url splitting operation.
`AppConfig.java` class is a spring configuration class to define beans, scan components and enable some configurations.
**@Configuration** annotation indicates that the class can be used by the Spring IoC container as a source of bean definitions. 
**@Bean** annotation shows the class will return an object that should be registered as a bean in the Spring application context. 
**@EnableAspectJAutoProxy** enable to handle components marked with **@Aspect** 

**@ComponentScan** annotation specifies the base packages to scan. Spring needs to know the packages to scan for annotated components.

`SplitService.java` is an interface for service layer methods. 
`SplitServiceImpl.java` is a class that annotated as a **@Service**. It includes business logic parts.

##### Regular Expression Algorithm

There is a well-defined regular expression written by Berners-Lee for parsing url 
[regular exp ref](https://tools.ietf.org/html/rfc3986#appendix-B)

```
^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
 12            3  4          5       6  7        8 9
 
 $1 = http:
 $2 = http
 $3 = //www.ics.uci.edu
 $4 = www.ics.uci.edu
 $5 = /pub/ietf/uri/
 $6 = <undefined>
 $7 = <undefined>
 $8 = #Related
 $9 = Related 
 	       
```

##### State Machine Algorithm

Parsing the url to many fields by implementing a state machine might be a challenging. State design pattern is one of the behavioral design pattern. So basically, we have states and action to change object's state.

These are the actions changes the states.  
```
start -> parse scheme -> parse host -> parse port -> parse path -> parse parameters - > end
```
States for parsing opertions: 

```
START - SCHEME - HOST - PORT - PATH - PARAM
```
In State design pattern generally two states(on, off or more) and one event(like doAction()). But in our situation we have many states and dispatching events, subscribing each state as an active, and unsubscribing it as an inactive is an unfavourable for me, I tried to get rid off redundancy.

So I enhanced the code to create states and corresponding events and delegate them by following above path. Because I already know the routes to reach at the end.

Just check the state in right action and if state is correct do the parsing operation. 
```
public void onEventParseScheme(URL aURL, Url urlState) throws Exception {
		if(currentState == State.START)
			currentState.onEventParseScheme(aURL,urlState);		
}
```
This is how to call state machine: 
```
	StateMachine machine = new StateMachine();
	machine.setCurrentState(State.START); // first set the state START
	URL aURL = new URL(url); // Create a URL object(java.net library)
	machine.onEventParseScheme(aURL, urlState); // call the first action
	machine.setCurrentState(State.SCHEME); // then change the state
	machine.onEventParseHost(aURL, urlState); // call the second action
```
These `onEventX()` operations basically sets parsed url object's fields to Url objects' parameters. If something is wrong on my link,
```
URL aURL = new URL(url);
```
these code part throw an exception that given url is not convenient for the real url. I just tried to implement state machine part, parsing url is a java library' business.

##### AOP 

Run 10000 iterations of each algorithm and  method time calculation are common for two algorithms so what I did is here, apply aspect oriented concept. These two functions can be considered as a cross-cutting concerns and cross-cutting concerns are conceptually separate from the application's business logic.  
`MethodExecutionAspect.java` class is an aspect and Spring AOP concept has some advices like "around," "before" and "after" and **around** is a good fit for supplying our requirements. If we want to throw specific exception we can use **@AfterThrowing** advice.

## Test
`SplitServiceTest.java` class is a unit test class to test parsing methods on service layer in two ways.
```
@Test()
public void schemeShouldReturnHttpWithRegex() throws Exception {
	Url url = splitService.splitOperationByRegex("http://google.com");
	assertEquals("http", url.getScheme());
	}
```

These test methods can be extended in many ways. I just wrote some basic ones. 

## EDIT 

I changed the state machine part. I prefered to use one common event `onEventChange` instead of writing all events like `onEventParseScheme`, `onEventParseHost`...  and implement this common method in event' enum. Therefore `StateMachine.java` class is just for changing states.

`StateEventListener.java` class 

```
public interface StateEventListener {

	void onEventChange(URL aURL, Url urlState) throws Exception;
}

```
`State.java` class 

```

public enum State implements StateEventListener{
	
	SCHEME{
		public void onEventChange(URL aURL, Url urlState) throws Exception {
			urlState.setScheme(aURL.getProtocol());
						
		}
	},HOST{
		public void onEventChange(URL aURL, Url urlState) throws Exception {
			urlState.setHost(aURL.getHost());
			
		}
	},PORT{
		public void onEventChange(URL aURL, Url urlState) throws Exception {
			urlState.setPort(Integer.toString(aURL.getPort()));
			
		}
	},PATH{
		public void onEventChange(URL aURL, Url urlState) throws Exception {
			urlState.setPath(aURL.getPath());
			
		}
	},PARAM{
		public void onEventChange(URL aURL, Url urlState) throws Exception {
			urlState.setParameters(aURL.getQuery());
			
		}
	};

}

```

`StateMachine.java` class

```
public class StateMachine implements StateEventListener {
	
	private State currentState;
	
	private LinkedList<State> transitions;
	
	public StateMachine(){
		this.transitions = new LinkedList<State>(Arrays.asList(State.SCHEME,State.HOST, State.PORT, State.PATH,State.PARAM));
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	
	public LinkedList<State> getTransitions() {
		return transitions;
	}

	public void onEventChange(URL aURL, Url urlState) throws Exception {
		currentState.onEventChange(aURL, urlState);
	}

}
```
This is service part

```
public Url splitOperationBySM(String url) throws Exception {
	Url urlState = new Url();
	try {
		StateMachine machine = new StateMachine();
		machine.setCurrentState(State.SCHEME);
		URL aURL = new URL(url);
		for (int i = 1; i < machine.getTransitions().size(); i++) {
			machine.getCurrentState().onEventChange(aURL, urlState);
			machine.setCurrentState(machine.getTransitions().get(i));
		}
	} catch (Exception e) {
		throw new Exception("Exception occured while state machine parsing operation..."+ e);
	}
	return urlState;
	}	
```

I think this approach is more closer to state machine design pattern.
![State Machine Design Pattern](https://github.com/fiskra/url-split/blob/master/statemachine.png)

In state machine design pattern we have State astract class which includes the methods may change object' states. And Event state classes which implement State class and do method implementations. And context class to run methods and change states. 

What I change in these design is I create enum class instead of abstract class and event classes to extends abstract class. Enum class implements EventListener class for behaviour change' methods. I could get rid of writing bulky code(creating a new class for every state is kind of bulky code for me.)   

One good example of sm can be jsf framework entire web request response lifecycle:

1. RestoreViewPhase
2. ApplyRequestValuePhase
3. ProcessValidationPhase
4. UpdateModelvaluesPhase
5. InvokeApplicationPhase
6. renderResponsePhase

[state-machine-example](http://efectivejava.blogspot.com.tr/2013/09/java-state-design-patten-oops-state.html?utm_source=BP_recent)
