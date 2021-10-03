package com.appium.tboader;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;

public class TBoarderTest extends TBBaseClass {

	// public static void main(String[] args) throws InterruptedException, MalformedURLException {

	@Test
	public void Test01() throws InterruptedException, MalformedURLException {		

		System.out.println("This is main() for " + (new Object(){}.getClass().getEnclosingClass().getSimpleName()));
		

		
		// Call the BaseClass's setup. Later can change to Unit Test annotation
		setup();

		SessionId sessionId = driver.getSessionId();
		System.out.println("INFO: sessionId: " + sessionId + " on " + driver.getRemoteAddress());
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
        clickView("Controls");
        clickView("01. Light Theme");
        
        // Text Box: First Name
        //android.widget.EditText[@resource-id='com.touchboarder.android.api.demos:id/edit']
        fillInTextbox("//android.widget.EditText[@resource-id='com.touchboarder.android.api.demos:id/edit']",
        		"CS");
        
        // Text Box: Last Name
        //android.widget.EditText[@resource-id='com.touchboarder.android.api.demos:id/edit2']
        fillInTextbox("//android.widget.EditText[@resource-id='com.touchboarder.android.api.demos:id/edit2']", 
        		"Beh");       

        // Checkbox 1
        //android.widget.CheckBox[@resource-id='com.touchboarder.android.api.demos:id/check1']
        checkboxAction("//android.widget.CheckBox[@resource-id='com.touchboarder.android.api.demos:id/check1']",
        		true);
        
        // Checkbox 2
        //android.widget.CheckBox[@resource-id='com.touchboarder.android.api.demos:id/check2']
        checkboxAction("//android.widget.CheckBox[@resource-id='com.touchboarder.android.api.demos:id/check2']",
        		true);
        
        
        // Radio Group
        // Radio Button - Let's select the first one
        //android.widget.RadioGroup
        selectRadioButton("//android.widget.RadioGroup", "RadioButton 1");
        
        // Toggle Button
        switchToggleButton("//android.widget.ToggleButton[@resource-id='com.touchboarder.android.api.demos:id/toggle1']" ,
        	true);

        switchToggleButton("//android.widget.ToggleButton[@resource-id='com.touchboarder.android.api.demos:id/toggle2']" ,
            	true);
        
        
        System.out.println("INFO: Finished execution.");
	}

	
	private static void fillInTextbox(String xpath, String val) {
		
		try {
			MobileElement tbox = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			tbox.sendKeys(val);
		}
		catch (Exception e) {
			System.out.println("ERROR:fillInTextBox():: Exception Error!");
			e.printStackTrace();
		}
		
	}
	
	
	private static void checkboxAction(String xpath, boolean toCheck) {
		
		System.out.println("DEBUG: in CheckboxAction()");
		try {
			MobileElement cbox = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

			System.out.println("INFO:checkboxAction()::Current state of " + cbox.getAttribute("text") + " - " + cbox.getAttribute("checked") );
			
			if ( (toCheck == true) && (cbox.getAttribute("checked").contentEquals("false") )) {
				cbox.click();
				System.out.println("INFO:checkboxAction():: CHECKED [" + cbox.getAttribute("text") +"]" );
			}
			else if ( (toCheck == false) && (cbox.getAttribute("checked").contentEquals("true") )) {
				cbox.click();
				System.out.println("INFO:checkboxAction():: UNCHECKED [" + cbox.getAttribute("text") + "]");
			}
			else if ( (toCheck == true) && (cbox.getAttribute("checked").contentEquals("true") ) ) {
				System.out.println("INFO:checkboxAction():: [" + cbox.getAttribute("text") + "] already CHECKED. No further action needed.");
			}
			else if ( (toCheck == false) && (cbox.getAttribute("checked").contentEquals("false") )) {
				System.out.println("INFO:checkboxAction():: [" + cbox.getAttribute("text") + "] already UNCHECKED. No further action needed.");
			}
		}
		catch (Exception e) {
			System.out.println("INFO:checkboxAction():: Exception Error!");
			e.printStackTrace();
		}
		
	}
	
	private static void selectRadioButton(String xpath, String selectedButton) {
		
		List<MobileElement> rButtons = null;
		
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
	
	
	private static void switchToggleButton(String xpath, boolean switchOn) {

		try {
			MobileElement tgBtn = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

			if ( switchOn == true && tgBtn.getAttribute("checked").contentEquals("false") ) {
				tgBtn.click();
			}
			else if ( switchOn == false && tgBtn.getAttribute("checked").contentEquals("true") ) {
				tgBtn.click();
			}
			else if ( switchOn == true && tgBtn.getAttribute("checked").contentEquals("true") ) {
				System.out.println("INFO:toggleButton()::" + xpath + " already switched ON. No further action needed.");
			}
			else if ( switchOn == false && tgBtn.getAttribute("checked").contentEquals("false") ) {
				System.out.println("INFO:toggleButton()::" + xpath + " already switched OFF. No further action needed.");
			}
		}
		catch (Exception e) {
			System.out.println("INFO:toggleButton():: Exception Error!");
			e.printStackTrace();
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

}
