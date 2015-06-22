package ch.monokellabs.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * Simple API to work with elements that can not instantly be visible as they are loaded/manipulated trough AJAX requests.
 * @author Lord eAgle
 */
public class AjaxHelper{
	private WebDriver driver;
	private int timeOutInSeconds = 10;

	public AjaxHelper(WebDriver driver){
		this.driver = driver;
	}
	
	public AjaxHelper timeoutAfter(int timeoutInSeconds){
		this.timeOutInSeconds = timeoutInSeconds;
		return this;
	}
	
	public void assertElementContains(final String elementId, final String expectedContent){
		new WebDriverWait(driver, timeOutInSeconds).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver){
				List<WebElement> elements = driver.findElements(By.id(elementId));
				if (elements.isEmpty())
				{
					System.out.println("yet no "+elementId+" @ "+driver.getPageSource());
					return false;
				}
				try{
					return elements.get(0).getText().contains(expectedContent);
				}
				catch(StaleElementReferenceException ex){
					return false;
				}
			}
		});
	}
	
	public void assertElementContainsNot(final String elementId, final String notExpectedContent){
		new WebDriverWait(driver, timeOutInSeconds).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver){
				return !driver.findElement(By.id(elementId)).getText().contains(notExpectedContent);
			}
		});
	}
	
	public void assertElementValue(final String elementId, final String expectedContent){
		new WebDriverWait(driver, timeOutInSeconds).until(
				ExpectedConditions.textToBePresentInElementValue(By.id(elementId), expectedContent)
		);
	}
	
	public WebElement findUntilVisible(final String elementId){
		return findUntilVisible(By.id(elementId));
	}
	
	public WebElement findUntilVisible(final By condition){
		return new WebDriverWait(driver, 10).until(
				ExpectedConditions.visibilityOfElementLocated(condition)
		);
	}
}