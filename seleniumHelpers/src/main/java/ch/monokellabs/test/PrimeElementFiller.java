package ch.monokellabs.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * Utility to simplify complex primefaces AJAX element manipulations.
 * @author Lord eAgle
 */
public class PrimeElementFiller{
	private AjaxHelper ajax;

	public PrimeElementFiller(WebDriver driver){
		ajax = new AjaxHelper(driver);
	}
	
	public SelectOneMenuWidget selectOne(WebElement element){
		return new SelectOneMenuWidget(element);
	}
	
	public class SelectOneMenuWidget{
		private WebElement selectOneMenu;

		private SelectOneMenuWidget(WebElement element){
			this.selectOneMenu = element;
		}
		
		public void select(String value){
			WebElement selectExpander = selectOneMenu.findElement(By.className("ui-icon-triangle-1-s"));
			selectExpander.click();
			
			String fqElementId = selectOneMenu.getAttribute("id");
			WebElement selectPanel = ajax.findUntilVisible(fqElementId+"_panel");
			List<WebElement> selectableItems = selectPanel.findElements(By.className("ui-selectonemenu-item"));
			for(WebElement item : selectableItems){
				if (item.getAttribute("data-label").equalsIgnoreCase(value)){
					item.click();
				}
			}
		}
	}
	
	public AutoCompleteWidget autoComplete(WebElement element){
		return new AutoCompleteWidget(element);
	}
	
	public class AutoCompleteWidget{
		private WebElement autoComplete;

		private AutoCompleteWidget(WebElement element){
			this.autoComplete = element;
		}
		
		public void selectSuggestion(String value){
			
			WebElement dropDown = autoComplete.findElement(By.className("ui-autocomplete-dropdown"));
			dropDown.click();
			
			String fqElementId = autoComplete.getAttribute("id");
			WebElement dropDownPanel = ajax.findUntilVisible(fqElementId+"_panel");
			List<WebElement> items = dropDownPanel.findElements(By.className("ui-autocomplete-item"));
			for(WebElement item : items){
				if (value.equals(item.getText())){
					item.click();
				}
			}
		}
	}
}