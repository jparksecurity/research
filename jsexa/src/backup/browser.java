package backup;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



public class browser {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FirefoxProfile profile=new FirefoxProfile();
	    profile.setAssumeUntrustedCertificateIssuer(false);
	    DesiredCapabilities firefoxdriver = DesiredCapabilities.firefox();
	    profile.setPreference("xpinstall.signatures.required", false);
	    firefoxdriver.setCapability(FirefoxDriver.PROFILE, profile);
		System.setProperty("webdriver.gecko.driver", "geckodriver");
		WebDriver driver = new FirefoxDriver();
		
		driver.get("google.com");
//		File torProfileDir = new File(
//		        "...\\Tor Browser\\Data\\Browser\\profile.default");
//		FirefoxBinary binary = new FirefoxBinary(new File(
//		        "...\\Tor Browser\\Start Tor Browser.exe"));
//		FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
//		torProfile.setPreference("webdriver.load.strategy", "unstable");
//
//		try {
//		    binary.startProfile(torProfile, torProfileDir, "");
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
		
//		System.setProperty("webdriver.gecko.driver", "geckodriver");

	}
}
