package jsexa;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class gethtml {
	String url;
	public String gethtml(String url) throws IOException{
		this.url=url;
		String html;
		String newUrl = "";
				if (url.startsWith("http://"))
				    newUrl = url;
				if (url.startsWith("https://"))
				    newUrl = url;
				if (url.startsWith("www"))
				    newUrl = "http://" + url;
		System.out.println("gethtml");
		Document doc = Jsoup.connect(newUrl).userAgent("chrome").timeout(50000).get();
		html=doc.html();
		
		//System.out.println(html);
		return html;
	}
}
