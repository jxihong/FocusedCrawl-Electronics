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

import java.util.List;;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import static org.junit.Assert.*;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestGhostDriverInteract {
    private WebDriver driver;
    protected static DesiredCapabilities caps;
   
    public void waitforJS(WebDriver driver) {
	while (true) {
	    Boolean jsIsComplete = (Boolean) ((JavascriptExecutor) driver)
		.executeScript("return document.readyState").toString().equals("complete");
	    
	    if (jsIsComplete) {
		break;
	    }
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
    
    @Before
    public void initialize() throws Exception {
	caps = new DesiredCapabilities();
	caps.setJavascriptEnabled(true);
	caps.setCapability("takesScreenshot", false);
        // caps.setCapability(
	//    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
        //    "/usr/local/bin/phantomjs");
	
	driver = new PhantomJSDriver(caps);
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    
    @Test
    public void scroll() throws Exception {
	driver.get("http://www.jncmanufacturing.com/"); // site with infinite scroll to load content
	    
	Long currentPos = (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;");
	
	//Scroll to bottom of page	
	while (true) {
	    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
	    waitforJS(driver);
	    
	    int count = 0;
	    if (currentPos == (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;")) {
		count++;
		if (count > 10) {
		    break;
		}
	    } else {
		currentPos = (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;");
		if (currentPos > 10000) {
		    break;
		}
		count = 0;
	    }
	}
	
	String content = driver.findElement(By.tagName("body")).getText();
	System.out.println(content);
	System.out.printf("%n");
    }
    
    @After
    public void quit() {
	if (driver != null) {
	    driver.quit();
	    driver = null;
	}
    }
    
}