package com.appium.calc;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class BaseClass {

    public static AndroidDriver<MobileElement> driver;
    public static WebDriverWait                wait;

    public static void setup() throws MalformedURLException {
    	
		URL url = new URL("http://127.0.0.1:4723/wd/hub");

    	// Desired capability
    	DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platfromName", "Android");
		cap.setCapability("platformVersion", "11");
		cap.setCapability("appPackage", "com.google.android.calculator");
		cap.setCapability("appActivity","com.android.calculator2.Calculator");
		
		//  cap.setCapability("skipDeviceInitialization", true);
		cap.setCapability("skipServerInstallation", true);
		cap.setCapability("ignoreUnimportantViews", true);		

		//   {
		//      "platformName": "Android",
		//      "platformVersion": "11",
		//      "appPackage": "com.google.android.calculator",
		//      "appActivity": "com.android.calculator2.Calculator"
		//    }
		
		// Android Driver
		driver = new AndroidDriver<MobileElement>(url, cap);
        wait = new WebDriverWait(driver, 15);		
    	
    }
    
	public enum CalcPad {
		Simple, Advanced
	}
	
	public static void switchPad(CalcPad pad)  {

		String basicPadId = "com.google.android.calculator:id/pad_basic";
		String advPadId = "com.google.android.calculator:id/pad_advanced";
		String newId = "";
		Point tPoint = new Point(1,1);
		
		if ( pad == CalcPad.Simple ) {
			// switching to Advanced Pad
			newId = basicPadId;

			// We need to 'tap' the specific area to pull out the advanced pad
			tPoint.x = 310;
			tPoint.y = 1350;

			// check advPad exist?
			// if yes, close it
			
		}
		else {
			newId = advPadId;
			
			tPoint.x = 1024;
			tPoint.y = 1400;			
			
		}
		
		// System.out.println("DEBUG NewId is " + newId );
		
		try {
			
			// System.out.println("DEBUG Element size: " + driver.findElements(By.id(newId)).size() );

			if ( pad == CalcPad.Advanced ) {
				if ( driver.findElements(By.id(advPadId)).size() != 0 ) {
					System.out.println("DEBUG: Already in [Advanced] Pad.");	
				}
				else {
					// switch to advanced pad
					@SuppressWarnings("rawtypes")
					TouchAction taAdvPad = new TouchAction(driver)
					.tap(point(tPoint.x, tPoint.y))
					.waitAction(waitOptions(Duration.ofMillis(250)));
					taAdvPad.perform();
					// small sleep to ensure the action done.
					Thread.sleep(500);
				}
				return;
			}
			else if ( pad == CalcPad.Simple) {
			
				// Verify advanced pad open
				// Note: don't use "basicPadId" to check, because it is always there.
				if ( driver.findElements(By.id(advPadId)).size() == 0 ) {
					// ok, means we're in simple pad 
					System.out.println("DEBUG: Already in [Simple] Pad.");
					return;
				}
				else {

					// wait.until(ExpectedConditions.presenceOfElementLocated(By.id(newId))) ;
					@SuppressWarnings("rawtypes")
					TouchAction taAdvPad = new TouchAction(driver)
					.tap(point(tPoint.x, tPoint.y))
					.waitAction(waitOptions(Duration.ofMillis(250)));
					taAdvPad.perform();
					// small sleep to ensure the action done.
					Thread.sleep(500);
				}
			}
			System.out.println("Switched to [" + ( ( pad == CalcPad.Simple ) ? "Simple" : "Advanced") + "] Pad.");
			
		} catch (TimeoutException e) {
			System.out.println("Switching to " + ( ( pad == CalcPad.Simple ) ? "Simple" : "Advanced") + "Pad failed!");
		} catch (InterruptedException e) {
			// catch the Thread sleep
			e.printStackTrace();
		}		
	}

	
	public enum CalcButton {
		B1("1"), B2("2"), B3("3"), B4("4"), B5("5"), B6("6"), B7("7"), B8("8"), B9("9"), Zero("0"),
		Add("plus"), Minus("minus"), Divide("divide"), Multiply("multiply"), Equal("="), Power("power");
		
		public final String label;
		private CalcButton(String label) {
			this.label = label;
		}
	}
	
	

	/**
	 * Return the final result of the Calculator. The result_final element must appear first. This usually
	 * means user should clicked the equal button or performed 'enter'. 
	 * 
	 * @return String result of calculation
	 */
	public static String getFinalResult() {

		String finResultId = "com.google.android.calculator:id/result_final";
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(finResultId))) ;	
			return driver.findElementById(finResultId).getText();
		} catch (TimeoutException e) {
			System.out.println("ERROR! Final Result Element not exists!");
		}
		
		return null;
	
	}
	
	
	public static void ManualClearCalc() {
		ManualClearCalc(10);
	}
	
	/**
	 *  Manually press the backspace button for reasonably many times to clear the calculator.
	 *  This is essential following a div by zero operation. The calculator apps GUI don't have 
	 *  "C" clear button available after error of div by zero. So, this is a workaround to reset
	 *  the calculator.
	 */
	public static void ManualClearCalc(int times) {
		
		for (int i = 0 ; i < times ; i++ ) {
			System.out.println("Formula: [" + driver.findElementById("com.google.android.calculator:id/formula").getText() + "]" );
			if ( driver.findElementById("com.google.android.calculator:id/formula").getText() == "" )
				break;
			driver.findElementById("com.google.android.calculator:id/del").click();
			
		}
			
	}
	
	
	/**
	 * Adds two single digit value, Op1 & Op2. [Simple assignment for 26Sep21]
	 * 
	 * @param Op1 a string contain single digit value
	 * @param Op2 a string contain single digit value
	 * @return result of the addition (String) 
	 */
	public static String Add(String Op1, String Op2) {
		return Add(Integer.parseInt(Op1), Integer.parseInt(Op2));
	}
	
	/**
	 * Adds two single digit value, Op1 & Op2. [Simple assignment for 26Sep21]
	 * 
	 * @param Op1 a single digit integer value
	 * @param Op2 a single digit integer value
	 * @return result of the division (String) 
	 */
	public static String Add(int Op1, int Op2) {
		return SimpleOperand( Op1, Op2, CalcOp.Add);
	}

	/**
	 * Multiply Op1 by Op2. [Simple assignment for 26Sep21]
	 * 
	 * @param Op1 a single digit integer value
	 * @param Op2 a single digit integer value
	 * @return result of the Multiply (String) 
	 */
	public static String Multiply(int Op1, int Op2) {
		return SimpleOperand( Op1, Op2, CalcOp.Multiply);
	}	

	/**
	 * Minus Op1 by Op2. [Simple assignment for 26Sep21]
	 * 
	 * @param Op1 a single digit integer value
	 * @param Op2 a single digit integer value
	 * @return result of the Minus (String) 
	 */
	public static String Minus(int Op1, int Op2) {
		return SimpleOperand( Op1, Op2, CalcOp.Minus);
	}	

	
	/**
	 * Divide Op1 by Op2. [Simple assignment for 26Sep21]
	 * 
	 * @param Op1 a single digit integer value
	 * @param Op2 a single digit integer value
	 * @return result of the addition (String), error message from calculator apps if div by zero 
	 */
	public static String Divide(int Op1, int Op2) {
		return SimpleOperand( Op1, Op2, CalcOp.Divide);
	}	
	
	
	public enum CalcOp {
			Add, Multiply, Minus, Divide
	}
	
	public static String SimpleOperand(int Op1, int Op2, CalcOp Operand) {

		if ( Op1 > 9 || Op1 < 0 || Op2 > 9 || Op2 < 0) {
			System.out.println("ERROR: Add() - Op1 & Op2 must be 0 ~ 9");
			return "ERR";
		}
		
		String opButton = "";
		
		driver.findElementByXPath(new String("//android.widget.Button[@text='" + Op1 + "']")).click();

		switch (Operand) {
		case Add:
			opButton = "plus"; break;
		case Minus:
			opButton = "minus"; break;
		case Multiply:			
			opButton = "multiply"; break;
		case Divide:			
			opButton = "divide"; break;
		}

		driver.findElementByXPath(new String("//android.widget.Button[@content-desc='" + opButton + "']")).click();
		
		driver.findElementByXPath(new String("//android.widget.Button[@text='" + Op2 + "']")).click();
		driver.findElementByXPath(new String("//android.widget.Button[@text='=']")).click();

		// Divide by Zero
		if (Operand == CalcOp.Divide && Op2 == 0) {
			// If it is divide, the calculator Apps will show "Can't divide by 0" at result_preview
			String previewResultId = "com.google.android.calculator:id/result_preview";			
			
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(previewResultId))) ;	
				return driver.findElementById(previewResultId).getText();
			} catch (TimeoutException e) {
				System.out.println("ERROR! Preview Result Element not exists!");
				return "ERR" ;
			}
			
		}
		else {

			String finResultId = "com.google.android.calculator:id/result_final";
			
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(finResultId))) ;	
				return driver.findElementById(finResultId).getText();
			} catch (TimeoutException e) {
				System.out.println("ERROR! Final Result Element not exists!");
				return "ERR" ;
			}

		}
		

		
	}
	

	/**
	 * Press the Button of the Calculator. It will switch to the correct Pad Layout automatically.
	 * 
	 * @param button Button to press. It's an enum type of CalcButton
	 */
	public static void pressButton(CalcButton button) {
		
		switch (button) {
			case B1:
			case B2:			
			case B3:			
			case B4:			
			case B5:			
			case B6:			
			case B7:			
			case B8:			
			case B9:			
			case Zero:	
			case Equal:
				System.out.println("DEBUG: To press button [" + button.label + "]");
				try {
					String Xpath = new String("//android.widget.Button[@text='" + button.label + "']");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath))) ;	
					driver.findElementByXPath(Xpath).click();
				} catch (TimeoutException e) {
					System.out.println("ERROR! Operation timeout.. ");
				}
				break;
			case Add:
			case Minus:
			case Divide:
			case Multiply:				
				System.out.println("DEBUG: To press button [" + button.label + "]");
				driver.findElementByXPath(new String("//android.widget.Button[@content-desc='" + button.label + "']")).click();
				break;
			case Power:
				// For Button's appear at Advanced Pad,  will do the switching here
				System.out.println("DEBUG: To press button [" + button.label + "]");
				switchPad(CalcPad.Advanced);
				driver.findElementByXPath(new String("//android.widget.Button[@content-desc='" + button.label + "']")).click();
				System.out.println("DEBUG: Pressed button [" + button.label + "]");
				switchPad(CalcPad.Simple);
				
				break;
		}
		
	}
    
    
	
}
