package com.appium.calc;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class PracClac {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		System.out.println("This is main of PracCalc");
	
		// IP 127.0.0.0
		// Port 4723

		// "http://127.0.0.1:4723/wd/hub"

		URL url = new URL("http://127.0.0.1:4723/wd/hub");
		// Desired capability

		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability("platfromName", "Android");
		cap.setCapability("platformVersion", "11");
		cap.setCapability("appPackage", "com.google.android.calculator");
		cap.setCapability("appActivity","com.android.calculator2.Calculator");
		
		//      {
		//      "platformName": "Android",
		//      "platformVersion": "11",
		//      "appPackage": "com.google.android.calculator",
		//      "appActivity": "com.android.calculator2.Calculator"
		//    }

		// Android Driver
		AndroidDriver<MobileElement> driver = new AndroidDriver<MobileElement>(url, cap);

	
		System.out.println("driver:" + driver);
		
		
		WebDriverWait wait = new WebDriverWait(driver,  10);
		
		// Verify we're in Simple Pad
	    try {
	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.calculator:id/pad_basic")));
			System.out.println("Basic Pad Presence");
	    } catch (TimeoutException e) {
			System.out.println("Not in Basic Pad");
			return;
	    }		
		
		// Close Advanced Pad
		// com.google.android.calculator:id/pad_basic
	    
		
		// MobileElement me_advpad = driver.findElementById("com.google.android.calculator:id/pad_advanced");
		// System.out.println("Adv_Panel" + me_advpad.isEnabled());
		
		
		// Mobile Element
		MobileElement me_9 = driver.findElementByXPath("//android.widget.Button[@text='9']");
		me_9.click();
		//android.widget.Button[@content-desc="minus"]
		// MobileElement me_add = driver.findElementByXPath("//android.widget.Button[@text='+']");
		MobileElement me_add = driver.findElementByXPath("//android.widget.Button[@content-desc='minus']");
		me_add.click();

		MobileElement me_5 = driver.findElementByXPath("//android.widget.Button[@text='5']");
		me_5.click();

		MobileElement me_eq = driver.findElementByXPath("//android.widget.Button[@text='=']");
		me_eq.click();
		
		System.out.println("Wait for sleep..");
		
		Thread.sleep(5000);
		
		// check the screen of calculator
		MobileElement me_finalresult = driver.findElementById("com.google.android.calculator:id/result_final");
		System.out.println("me_finalresult: [" + me_finalresult.getText() + "]");
		
		System.out.println("Execution Over");
		
		
		
		
	}

}
