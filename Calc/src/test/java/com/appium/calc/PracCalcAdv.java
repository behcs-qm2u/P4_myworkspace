package com.appium.calc;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;


/**
 * @author behcs
 *
 */
public class PracCalcAdv extends BaseClass {


	public static void main(String[] args) throws MalformedURLException, InterruptedException {

		System.out.println("This is main() of " + (new Object(){}.getClass().getEnclosingClass().getSimpleName()));

		// Call the BaseClass's setup. Later can change to Unit Test annotation
		setup();

		Thread.sleep(2000);
		
		
		  System.out.println("Running 8 + 6 using Add() : Result is " + Add(8,6));
		  
		  System.out.println("Running 9 + 3 using Add() : Result is " + Add("9","3"));
		  
		  System.out.println("Running 9 / 3 using Divide() : Result is " +
		  Divide(9,3));
		  
		  System.out.println("Running 9 / 0 using Divide() : Result is " +
		  Divide(9,0));
		  
		  // after divide by zero error, we need to manually click "backspace" button
		  // many times to clear the calculator 
		  // there isn't "C" button appear in the apps 
		  
		  ManualClearCalc(10);
		  
		  
		  System.out.println("Running 8 / 4 using Minus() : Result is [" + Minus(8,4) +
		  "]");
		  
		  System.out.println("Running 8 / 4 using Minus() : Result is " + Minus(8,2));
		  
		  System.out.println("Running 3 / 5 using Minus() : Result is " + Minus(3,5));
		  
		  System.out.println("Running 6 / 7 using Multiply() : Result is " +
		  Multiply(6,7));
		  
		 	
		System.out.println("Advanced :");
		
		// Formula : 2 ^ 5 = 
		
		  System.out.println("Formula: 2 ^ 5 = "); pressButton(CalcButton.B2);
		  pressButton(CalcButton.Power); pressButton(CalcButton.B5);
		  pressButton(CalcButton.Equal);
		  
		  
		  System.out.println("Wait for sleep.."); Thread.sleep(1000);
		  System.out.println("me_finalresult: [" + getFinalResult() + "]");
		  System.out.println("Execution Over");
		 

	}

}
