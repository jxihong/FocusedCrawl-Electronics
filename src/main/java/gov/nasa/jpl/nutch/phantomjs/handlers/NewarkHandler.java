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

package org.apache.nutch.protocol.interactiveselenium;

import java.io.UnsupportedEncodingException;
import java.lang.NumberFormatException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for the site http://newark.com.
 * @author Joey Hong
 */
public class NewarkHandler implements InteractivePhantomJSHandler {
    public static final Logger LOG = LoggerFactory
	.getLogger(NewarkHandler.class);

    @Override
    public String processDriver(WebDriver driver) {
	StringBuffer buffer = new StringBuffer();
	
	// Extract content 
	String content = driver.findElement(By.tagName("body")).getText();
	buffer.append(content).append("\n");
	
	WebElement pages = driver.findElement(By.xpath("//span[@class='pages']"));
	if (pages == null) {
	    return bugger.toString();
	}
	
	int lastPageNum = 1;
	try {
	    lastPageNum = Integer.parseInt(
			      pages.findElement(By.xpath("./span[@class='paginNext pageIt']/a")).getAttribute("data-rel"));
	} catch (NumberFormatException e) {
	    // do nothing
	}
	
	for (int i = 2; i <= lastPageNum; i++) {
	    buffer.append("<a href=\"").append(driver.getCurrentUrl()).append("/prl/results/").append(i)
		.append("\" />\n");
	}
	
	return buffer.toString();
    }
    
    @Override
    public boolean shouldProcessURL(String URL) {
	if (URL.startsWith("http://www.newark.com/") && !URL.contains("/results/")) {
	    return true;
	}
	return false;
    }
}