# Simple Url Split Technical Challenge

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


