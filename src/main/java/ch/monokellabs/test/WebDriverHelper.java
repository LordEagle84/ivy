package ch.monokellabs.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * Facade to access simplified and ivy-specific {@link WebDriver} api. 
 * 
 * @author Lord eAgle
 */
public class WebDriverHelper {
	private WebDriver driver;
	private String baseUrl;
	private String ivyApplication;

	public static WebDriverHelper htmlUnit(){
		return new WebDriverHelper(new org.openqa.selenium.htmlunit.HtmlUnitDriver(true));
	}
	
	public static WebDriverHelper firefoxPortable(){
		FirefoxDriver firefox = FirefoxPortable.createWithSystemProperty()
				.setLanguage("de")
				.createDriver();
		return new WebDriverHelper(firefox);
	}
	
	public WebDriverHelper(WebDriver driver){
		this.driver = driver;

		// Max time to wait for an element to appear is set to 10 Seconds
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		baseUrl = System.getProperty("server.test.baseUrl", "http://localhost:8081/ivy/");
		ivyApplication = System.getProperty("server.test.testApplication", "designer");
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public AjaxHelper ajax(){
		return new AjaxHelper(driver);
	}
	
	public PrimeElementFiller prime(){
		return new PrimeElementFiller(driver);
	}
	
	public TestApplication app(){
		return new TestApplication(driver, baseUrl, ivyApplication);
	}
	
}
