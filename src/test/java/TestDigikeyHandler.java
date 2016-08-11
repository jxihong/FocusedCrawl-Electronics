/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.nasa.jpl.nutch.phantomjs.test;

import java.io.UnsupportedEncodingException;

import org.junit.*;
import static org.junit.Assert.*;

import org.apache.nutch.protocol.interactiveselenium.DigikeyHandler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;

import gov.nasa.jpl.nutch.phantomjs.PhantomJSWebClient;

public class TestDigikeyHandler {
    private WebDriver driver;
    
    @Test
    public void testHandler() {
	DigikeyHandler handler = new DigikeyHandler();
	WebDriver driver = null;
	
	try {
	    driver = PhantomJSWebClient.getDriverForPage("http://www.digikey.com/product-search/en/integrated-circuits-ics/interface-analog-switches-multiplexers-demultiplexers/2556671");
	    System.out.println(new String(handler.processDriver(driver).getBytes("UTF-8")));
	}
	catch(Exception e) {
	    if(e instanceof TimeoutException) {
		System.out.println("Timeout Exception");
		   
		try {
		    System.out.println(new String(handler.processDriver(driver).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
	    }
	    if(driver != null) {
		PhantomJSWebClient.cleanUpDriver(driver);
		//e.printStackTrace();
	    }
	}
    }

}
