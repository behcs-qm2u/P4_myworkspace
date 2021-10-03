package com.appium.tboader;

import static org.junit.Assert.*;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class TestControls {

	public static AndroidDriver<MobileElement> driver;
	public static WebDriverWait                wait;	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		

		
		
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
		
		SessionId sessionId = driver.getSessionId();
		System.out.println("INFO: sessionId: " + sessionId + " created on " + driver.getRemoteAddress());
		Thread.sleep(1000);
		
		// Check in first page? Click continue for the permissions
		String prechkLoc = "//android.widget.TextView[@resource-id='com.android.permissioncontroller:id/permissions_message']";
		String clickLoc = "//android.widget.Button[@text='Continue']";
		valAndClick(prechkLoc, clickLoc);
		
		// We need to bypass the app update reminder screen
		// API Demos .. This app was built... bla bla bla
		prechkLoc = "//android.widget.TextView[contains(@text, \"This app\")]";
		clickLoc = "//android.widget.Button[@text='OK']";
		valAndClick(prechkLoc, clickLoc);
		
		// We also need to bypass the Change Log reminder screen
		prechkLoc = "//android.widget.TextView[contains(@text, \"Change Log\")]";
		clickLoc = "//android.widget.TextView[@text='OK']";
		valAndClick(prechkLoc, clickLoc);		

		System.out.println("INFO: Entering API DEMOS screen...");
		clickView("API Demos");
        clickView("Views");

        // scroll up 2 times once we reached views to pick seekbar
        scrollUp(2);
        clickView("Seek Bar");
        
        
        //clickView("Controls");
        //clickView("01. Light Theme");		
		
        driver.hideKeyboard();
	}
	
	
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() {
		fail("Not yet implemented");
	}

	
	//@Test
	public void testFirstName() {
		driver.findElement(By.id("com.touchboarder.android.api.demos:id/edit")).sendKeys("Beh");
		String expValue = driver.findElement(By.id("com.touchboarder.android.api.demos:id/edit")).getText();
		System.out.println(expValue);
		assertTrue(expValue.equals("Beh"));
		driver.hideKeyboard();
	}
	
	//@Test
	public void testCB2() {
		MobileElement cb2 = driver.findElement(By.id("com.touchboarder.android.api.demos:id/check2))"));
		cb2.click();
		if ( !cb2.getAttribute("checked").equals("false")) {
			cb2.click();
		}

	}
	
	//@Test
	public void testRadioGroup() {
		
		List<MobileElement> rButtons = null;
		String xpath = "//android.widget.RadioGroup";
		String selectedButton = "RadioButton 1";
		
		MobileElement rGroup = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));		
		
		if ( rGroup != null ) {
			try {
				rButtons = rGroup.findElements(By.xpath("//android.widget.RadioButton"));
				
				System.out.println("DEBUG:clickView():: Nos of choices in this RadioGroup: " + rButtons.size());
				for (MobileElement rBtn : rButtons) {
					System.out.println(" -> " + rBtn.getAttribute("text"));
					if (rBtn.getAttribute("text").equals(selectedButton)) {
						System.out.println("     ^-- CLICK!");
						rBtn.click();
					
						// Do assert
						assertTrue(rBtn.getAttribute("checked").equals("true"));						
						// we want to list all, so don't break / return
						// break;
						// return ;
					}
				}	
			
			}
			catch (StaleElementReferenceException e) {
				System.out.println("DEBUG:selectRadioButton():: Caught [StaleElementReferenceException] retrying.." );
			}
			catch (Exception e) {
				System.out.println("DEBUG:selectRadioButton():: Caught [something else?!] retrying.." );
				e.printStackTrace();
			}

		}
		
	}
	

	//@Test
	public void testSpinner() {
		
		String spinnerId = "com.touchboarder.android.api.demos:id/spinner1";

		MobileElement spinner = driver.findElement(By.id(spinnerId));
		MobileElement defSpinValue = spinner.findElement(By.id("android:id/text1"));

		System.out.println("testSpinner()::Current selected: " + defSpinValue.getAttribute("text"));
		spinner.click();
		
		List<MobileElement> cTxtList = driver.findElements(By.xpath("//android.widget.CheckedTextView"));
		for (MobileElement cTxt : cTxtList) {
			if (cTxt.getAttribute("text").equals("Saturn")) {
				System.out.println("Before click:" + cTxt.getAttribute("text") + " " + cTxt.getAttribute("checked"));
				cTxt.click();
				break;
			}
		}
		
		// have to get the element again as we changed the focus when the spinner popped 
		MobileElement selectedSpinValue = spinner.findElement(By.id("android:id/text1"));
		
		System.out.println("New selected:" + selectedSpinValue.getAttribute("text"));
		assertTrue(selectedSpinValue.getAttribute("text").equals("Saturn"));
		
	}
	// String selSpinnerLoc = "//android.widget.Spinner[@resource-id='com.touchboarder.android.api.demos:id/spinner1']/android.widget.TextView[@resource-id='android:id/text1']";	

	// @Test
	public void testScroll() {
		System.out.println("INFO: Testing scroll up.. 2 times");
		scrollUp();
		scrollUp();
	}
	
	@Test 
	public void testSeek() {
		
		int targetPct = 80;
		
		System.out.println("INFO:testSeek():start");
		
		String seekBarId = "com.touchboarder.android.api.demos:id/seek";
		MobileElement seekBar = driver.findElement(By.id(seekBarId));
		
		String sbBounds =  seekBar.getAttribute("bounds");
		System.out.println("Bounds of seekBar" + seekBar.getAttribute("bounds"));

		int xAxisStartPoint = seekBar.getLocation().getX();
		int xAxisEndPoint = xAxisStartPoint + seekBar.getSize().getWidth();
		int yAxis = seekBar.getLocation().getY();
		
		System.out.println("xAxisStart: " + xAxisStartPoint);
		System.out.println("xAxisEndPoint: " + xAxisEndPoint);
		System.out.println("yAxis: " + yAxis);
		
		// [0,66][1080,116]
		String coordinate[] = sbBounds.replaceAll("\\]\\[", ",").replaceAll("\\[|\\]", "").split(","); 
		Point topLeft = new Point( Integer.parseInt(coordinate[0]), 
				Integer.parseInt(coordinate[1]) );
		Point bottomRight = new Point( Integer.parseInt(coordinate[2]), 
				Integer.parseInt(coordinate[3]) );

		
		/* Actions 
		 * - drag from about 20% to about 80%
		 */
		Point start = new Point(0,0);
		start = topLeft.moveBy((int) ((bottomRight.x - topLeft.x) * 0.2 ),
				(int) ((bottomRight.y - topLeft.y) * 0.5 ) ); 
				
		// Take Note: topLeft "moved"
		Point stop = new Point(0,0);

		stop = topLeft.moveBy((int) ((bottomRight.x - topLeft.x) * ((float)(targetPct * 0.95)/100) ), 
				(int) ((bottomRight.y - topLeft.y) * 0.5 ) );

		
		System.out.println("DEBUG:SeekBar edge points - TopLeft, BottomRight " + topLeft + " " + bottomRight);
		System.out.println("DEBUG:SeekBar touch points - begin, stop" + start + " " + stop);
		
		// this will animate the slide action
		scrollLeft(start, stop);
		// make it stop at exact
		seekBar.sendKeys(Integer.toString(targetPct));
		
		//Do assertion
		MobileElement chkBox = driver.findElement(By.id("com.touchboarder.android.api.demos:id/progress"));
		System.out.println("DEBUG:progress text box : " + chkBox.getText());
		String verifyText = Integer.toString(targetPct).concat(" from touch=true");
		assertTrue(verifyText.contentEquals(chkBox.getText()));
		
		
	}

	public void scrollLeft(Point start, Point end) {

		TouchAction action = new TouchAction(driver);
		
		action
		.press(PointOption.point(start.x, start.y))
		.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
		.moveTo(PointOption.point(end.x, end.y))
		.release()
		.perform();
		
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void scrollUp(int repeat) {
		while (repeat-- > 0) scrollUp();
	}
	
	public static void scrollUp() {
		
		int height = driver.manage().window().getSize().getHeight();
		int width = driver.manage().window().getSize().getWidth();
		
		int startX = (int) (0.5 * width);
		int endX = (int) (0.5 * width);
		
		int startY = (int) (0.90 * height);
		int endY = (int) (0.10 * height);
		
		TouchAction action = new TouchAction(driver);
		
		action
		.longPress(PointOption.point(startX, startY))
		.moveTo(PointOption.point(endX, endY))
		.release()
		.perform();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Helper method to check element in prechkLocator exists, then select and click the element of clickLocator
	 * 
	 * @param prechkLocator Xpath string for pre-check element
	 * @param clickLocator Xpath string for the element to click
	 */
	private static void valAndClick(String prechkLocator, String clickLocator) {
		
		try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prechkLocator)));
				checkAndClick(clickLocator);
		}
		catch (TimeoutException e) {
			System.out.println("FATAL: Pre-check element not exist / Timeout!");
		}
		catch (Exception e) {
			// something else wrong?
			e.printStackTrace();
		}
		
	}

	private static void checkAndClick(String locValue) {

		List<MobileElement> lstElems = driver.findElements(By.xpath(locValue));
	
		if (lstElems.size() > 0) {
			// System.out.println("DEBUG: checkAndClick()::Elems size: " + lstElems.size());
			lstElems.get(0).click();
		}
		else {
			System.out.println("ERROR: checkAndClick()::element [" + locValue + "] not found in list view!");
		}
	
}	
	
	/**
	 * Helper method to select and click the list view item identified by elemClick
	 * 
	 * @param elemClick String for the element's text attribute
	 * @throws InterruptedException 
	 */
	private static void clickView(String elemClick) throws InterruptedException {

		MobileElement parent = null; 
		List<MobileElement> lstViews = null;
		int retry_max = 3;
		
		System.out.println("DEBUG:clickView():: Going to click [" + elemClick +"]");

		// we will do <retries times>
		// StaleElement exception seems very common here, we will do retry
		
		WebDriverWait wait2 = (WebDriverWait) new WebDriverWait(driver, 8)
				.ignoring(StaleElementReferenceException.class);

		while (retry_max-- > 0) {

			parent = (MobileElement) wait2.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']")));
			// parent = (MobileElement) wait2.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.ListView[@resource-id='android:id/list']")));
			
			if ( parent != null ) {
				try {
					lstViews = parent.findElements(By.xpath("//android.widget.TextView"));
					
					System.out.println("DEBUG:clickView():: Size of List View: " + lstViews.size());
					for (MobileElement view : lstViews) {
						System.out.println(" -> " + view.getAttribute("text"));
						// iterate until the elemClick to click
						if (view.getAttribute("text").equals(elemClick)) {
							System.out.println("     ^-- CLICK!");
							view.click();
							// break;
							return ;
						}
					}	
					
					// if not found?!
					System.out.println("FATAL:clickView():: [" + elemClick + "] not found! To retry..");
					continue;
					
				}
				catch (StaleElementReferenceException e) {
					System.out.println("DEBUG:clickView():: Caught [StaleElementReferenceException] retrying.." );
				}
				catch (Exception e) {
					System.out.println("DEBUG:clickView():: Caught [something else?!] retrying.." );
					e.printStackTrace();
				}

			}
			else {
				System.out.println("DEBUG:clickView():: parent is null! Retrying.." );
			}

		}

		// fall through
		fail("FATAL:clickView():: [" + elemClick + "] not found! Exhausted retries!");

	}
	
	
}
