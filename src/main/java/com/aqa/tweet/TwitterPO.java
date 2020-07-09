package com.aqa.tweet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TwitterPO {
	
	WebDriver driver;
	By msj_container = By.className("errorpage-body-content");
	By msj = By.tagName("h1");
	
	public TwitterPO(WebDriver driver) {
		super();
		this.driver = driver;			
	}
	
	public String getMsj(){

        return driver.findElement(msj_container).findElement(msj).getText();

    }
	
}
