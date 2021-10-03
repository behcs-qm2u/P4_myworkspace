package com.appium.tboader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class TBBaseClass {

	public static AndroidDriver<MobileElement> driver;
	public static WebDriverWait                wait;

	public static void setup() throws MalformedURLException {

		// URL url = new URL("http://127.0.0.1:4723/wd/hub");
		// URL url = new URL("http://192.168.100.185:4723/wd/hub");
		URL url = new URL("http://192.168.100.41:4723/wd/hub");

		// Desired capability
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platfromName", "Android");
		cap.setCapability("platformVersion", "11");
		cap.setCapability("appPackage", "com.touchboarder.android.api.demos");
		cap.setCapability("appActivity","com.touchboarder.androidapidemos.MainActivity");

		//  cap.setCapability("skipDeviceInitialization", true);
		//cap.setCapability("skipServerInstallation", true);
		// use this with care!
		//cap.setCapability("ignoreUnimportantViews", true);		

		//		{
		//			  "platformName": "Android",
		//			  "platformVersion": "11",
		//			  "Package": "com.touchboarder.android.api.demos",
		//			  "Activity": "com.touchboarder.androidapidemos.MainActivity"
		//		}

		// Android Driver
		driver = new AndroidDriver<MobileElement>(url, cap);
		
		// Set Implicit wait
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		
		wait = new WebDriverWait(driver, 8);		

	}

}
