package jsexa;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Stack;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.python.jline.internal.Log;

public class googlesearch {
    layertwo l2;
    MyDB mydb;

    public googlesearch() throws ClassNotFoundException, SQLException {
        l2 = new layertwo();
        mydb = new MyDB();
    }

    public void main(String keyword, String AC) throws SQLException, InterruptedException {
        Stack<String> keywords = mydb.loadkeywords("all", 10, "google");
        String url = "https://www.google.com/search?q=" + keyword + "&num=10";
        int id = mydb.get_int_bygivenstr("keyword_Id", "keyword", "Keyword", keyword);
        for (String str : keywords) {
            openFF(id, url, AC);
        }
    }


    public void openFF(int id, String url, String AC) throws InterruptedException {

        String torPath = "C:\\Tor Browser\\Browser\\firefox.exe";
        String profilePath = "C:\\Tor Browser\\Browser\\TorBrowser\\Data\\Browser\\profile.default";

        //System.setProperty("webdriver.firefox.bin", torPath);
        File torProfileDir = new File(profilePath);
        FirefoxBinary binary = new FirefoxBinary(new File(torPath));
        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(binary);
        options.setProfile(torProfile);
        WebDriver driver = new FirefoxDriver(options);

        driver.get("http://whatismyipaddress.com/");
        // Print a Log In message to the screen
        System.out.println("Successfully opened the website www.Store.Demoqa.com");
        List<WebElement> class_g = driver.findElements(By.tagName("g"));
        String title, site, abstracts, html = "";
        for (WebElement el : class_g) {

            try {
                System.out.println("Title : " + el.findElement(By.tagName("h3")).getText());
                // el.getElementsByTag("h3").text()
                title = el.findElement(By.tagName("h3")).getText();
                //el.getElementsByTag("cite").text().toString()
                site = el.findElement(By.tagName("cite")).getText();
                //el.getElementsByTag("span").text().toString();
                abstracts = el.findElement(By.tagName("span")).getText();
                System.out.println("Site : " + site);
                abstracts = abstracts.replaceAll("\"", "");
                Thread.sleep(10000);
                driver.get(mydb.simplizeurl(site));
                String html_body = l2.get_text_html(driver.getPageSource());
                System.out.println("Abstract : " + abstracts + "\n");
            } catch (Exception e) {
                Log.error("id:" + id + " cannot load page");
                continue;
            }
            //System.out.println(html);
        }
//        WebElement  btn = driver.findElement(By.id("submitButton"));//Locating element by id
        //Wait for 5 Sec
        Thread.sleep(15000);

        // Close the driver
//        driver.quit();
    }
}
