package com.play.tech.url.split;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.play.tech.url.split.model.Url;
import com.play.tech.url.split.service.SplitService;
import com.play.tech.url.split.service.SplitServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SplitServiceTest {
	
	@Configuration
    static class SplitServiceTestContextConfiguration {

        @Bean
        public SplitService accountService() {
            return new SplitServiceImpl();
        }
    }

	@Autowired
	private SplitServiceImpl splitService;

	@Test()
	public void schemeShouldReturnHttpWithRegex() throws Exception {
		Url url = splitService.splitOperationByRegex("http://google.com");
		assertEquals("http", url.getScheme());
	}
	
	@Test()
	public void schemeShouldReturnHttpWithState() throws Exception {
		Url url = splitService.splitOperationBySM("http://google.com");
		assertEquals("http", url.getScheme());
	}
	
	@Test()
	public void hostShouldReturnGoogleWithRegex() throws Exception {
		Url url = splitService.splitOperationByRegex("http://google.com");
		assertEquals("google.com", url.getHost());
	}
	
	@Test()
	public void hostShouldReturnGoogleWithState() throws Exception {
		Url url = splitService.splitOperationBySM("http://google.com");
		assertEquals("google.com", url.getHost());
	}
	
	@Test()
	public void portShouldReturnNullWithRegex() throws Exception {
		Url url = splitService.splitOperationByRegex("http://google.com");
		assertEquals(null, url.getPort());
	}
	
	@Test()
	public void portShouldReturnNullWithState() throws Exception {
		Url url = splitService.splitOperationBySM("http://google.com");
		assertEquals(null, url.getPort());
	}

}
