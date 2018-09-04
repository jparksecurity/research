package jsexa;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.python.jline.internal.Log;

public class layerthree {
	MyDB mydb;
	public layerthree() throws ClassNotFoundException, SQLException{
		mydb =new MyDB();
	}
	public String openFF(String AC,int suspect_id) throws InterruptedException, SQLException, ClassNotFoundException{
		Map<Integer,String> urls =mydb.getsuspecturl(AC);
		System.out.println(urls.toString());
		if(urls.isEmpty()){
			System.out.println("url set is empty");
			return null;
		}
		Set<Integer> ids=urls.keySet();
		layertwo l2 =new layertwo();
		layerone l1 =new layerone();
		System.setProperty("webdriver.firefox.bin", "/Applications/TorBrowser.app/Contents/MacOS/firefox");
		String torPath = "/Applications/TorBrowser.app/Contents/MacOS/firefox";
		String profilePath = "/Users/zijiguo/Library/Application Support/Firefox/Profiles/ziji_tor_profile";
		FirefoxProfile profile = new FirefoxProfile(new File(profilePath));
		profile.setPreference("plugin.state.flash", 0);
		File file = new File("hide_all_images-1.5.3-fx.xpi");
		profile.addExtension(file);
		System.setProperty("webdriver.gecko.driver", "/Users/zijiguo/Documents/workspace/jsexa/geckodriver");
		FirefoxBinary binary = new FirefoxBinary(new File(torPath));
		FirefoxOptions options = new FirefoxOptions();
		options.setBinary(binary);
		options.setProfile(profile);
		FirefoxDriver driver = new FirefoxDriver(options);
		for(Integer id:ids){
			try{
			System.out.println("url:\n"+urls.get(id));
			driver.get(l1.geturlfixed(urls.get(id)));
			System.out.println("url:\n"+urls.get(id)+" loaded");
			String html_body = l2.get_text_html(driver.getPageSource());
			if(html_body.isEmpty()){
				Log.error("id:"+id+" url:"+urls.get(id)+" cannot load page");
			}else{
				System.out.println("body:\n"+html_body);
				System.out.println(mydb.updatesuspect(id, html_body, AC));
				
			}
			}catch(Exception e){
				Log.error("id:"+id +" cannot load page "+ e.getMessage());
//				System.out.println(mydb.updatesuspect(id, " cannot load page ", AC));
				continue;
			}
		}
		return driver.getPageSource();
       
	}
}
