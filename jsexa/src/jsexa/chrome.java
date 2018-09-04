package jsexa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class chrome {

	public static  void testGoogleSearch() {
		  // Optional, if not specified, WebDriver will search your path for chromedriver.
		  System.setProperty("webdriver.chrome.driver","C:\\Users\\박중현\\Desktop\\Spring 18\\Research\\Web-Cloaking\\Archive\\jsexa\\chromedriver.exe");

		  WebDriver driver = new ChromeDriver();
		  driver.get("http://www.sztdpower.com/product/show-7365902.html");
//		  Thread.sleep(5000);  // Let the user actually see something!
//		  WebElement searchBox = driver.findElement(By.name("q"));
		  System.out.println(driver.getPageSource());
//		  searchBox.sendKeys("ChromeDriver");
//		  searchBox.submit();
//		  Thread.sleep(5000);  // Let the user actually see something!
		  driver.quit();
		}
}
