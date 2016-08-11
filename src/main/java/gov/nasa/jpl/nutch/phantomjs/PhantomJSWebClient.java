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
package gov.nasa.jpl.nutch.phantomjs;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.io.TemporaryFilesystem;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

public class PhantomJSWebClient {
    
    public static WebDriver getDriverForPage(String url) {
	long pageLoadTimout = 3;
	
	DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", false);
	
	WebDriver driver = null;
	try {
	    driver = new PhantomJSDriver(caps);
	    driver.manage().timeouts().pageLoadTimeout(pageLoadTimout, TimeUnit.SECONDS);
	    driver.get(url);
	} catch(Exception e) {
	    if(e instanceof TimeoutException) {
		System.out.println("PhantomJS WebDriver: Timeout Exception: Capturing whatever loaded so far...");
		return driver;
	    }
	    cleanUpDriver(driver);
	    throw new RuntimeException(e);
	}

	return driver;
    }
    
    public static String getHTML(String url) {
	WebDriver driver = getDriverForPage(url);
	
	return getHTMLContent(driver);
    }
    
    public static String getHTMLContent(WebDriver driver) {
	try {
	    String innerHtml = driver.findElement(By.tagName("body")).getAttribute("innerHTML");
	    return innerHtml;
	} catch(Exception e) {
	    TemporaryFilesystem.getDefaultTmpFS().deleteTemporaryFiles();
	    throw new RuntimeException(e);
	} finally {
	    cleanUpDriver(driver);
	}
    }

    public static void cleanUpDriver(WebDriver driver) {
	if (driver != null) {
	    try {
		driver.close();
		driver.quit();
		TemporaryFilesystem.getDefaultTmpFS().deleteTemporaryFiles();
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
    }
    


}