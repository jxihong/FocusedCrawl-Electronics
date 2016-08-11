/*
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

package org.apache.nutch.protocol.interactiveselenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that reads dynamically generated content when page is scrolled.
 * @author Joey Hong
 */
public class ScrollHandler implements InteractiveSeleniumHandler {
    public static final Logger LOG = LoggerFactory
	.getLogger(ScrollHandler.class);
    
    /** 
     * Waits for Javascript to execute.
     */
    public void waitforJavascript(WebDriver driver) {
	WebDriverWait wait = new WebDriverWait(driver, 30);
	
	/** wait for jQuery to load */
	ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		try {
		    return (Boolean) (((JavascriptExecutor) driver)
				          .executeScript("return JQuery.active == 0"));
		}
		catch (Exception e) {
		    return true;
		}
	    }
	};
	/** wait for Javascript to load */
	ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		try {
		    return (boolean) ((JavascriptExecutor) driver)
		                         .executeScript("return document.readyState").toString().equals("complete");
		}
		catch (Exception e) {
		    return true;
		}
	    }
	};
	
	wait.until(jQueryLoad);
	wait.until(jsLoad);
    }

    @Override
    public String processDriver(WebDriver driver) {
	StringBuffer buffer = new StringBuffer();
	long currentPos = (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;");
	
	int count = 0;
	while (true) {
	    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
	    waitforJavascript(driver);
	    if (currentPos == (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;")) {
		count++;
		if (count > 10)
		    break;
	    } else {
		currentPos = (Long) ((JavascriptExecutor) driver).executeScript("return window.scrollY;");
		count = 0;
	    }
	    // In case the page gets too large
	    if (currentPos >= 10000) {
		break;
	    }
	}
	String content = driver.findElement(By.tagName("body")).getAttribute("innerHTML");
	buffer.append(content).append("\n");
	return buffer.toString();
    }
    
    @Override
    public boolean shouldProcessURL(String URL) {
	if (URL.startsWith("https://plus.google.com") || URL.startsWith("http://twitter.com") ||
	    URL.startsWith("https://www.facebook.com")) {
	    return true;
	}
	return false;
    }

    
}