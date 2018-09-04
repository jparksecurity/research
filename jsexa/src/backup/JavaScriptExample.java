package backup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JavaScriptExample {
	
	public static void main(String[] args) throws Exception {
		baidu_result("isu",10);

    }
	
	public static void baidu_result(String keyword, int no_of_results) throws Exception{

//	   
//	    st.executeUpdate("INSERT INTO Keyword(keyword) " + 
//	        "VALUES (\""+keyword+"\")"); 

		keyword = keyword.replace(" ", "+");
		String url ="http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd="+keyword;
		Document doc = Jsoup.connect(url).userAgent("chrome").timeout(5000).get();
		int index =0;
		String tempurl,tempabstract,temptittle,site,abstracts;
		int index_tittle;
		//System.out.println(doc.html());
		for (;index<no_of_results;index++) {
			 tempurl=doc.select("#content_left .t").get(index).getElementsByTag("a").attr("href");
			 tempabstract = doc.select("#content_left div#"+(index+1)+".c-container").text();
			 temptittle =doc.select("#content_left .t").get(index).text();
			 index_tittle=temptittle.length();
			 site = findrealurl(tempurl);
			 System.out.println("temptittle:"+temptittle+"\t index_tittle:"+index_tittle);
			 if(index_tittle>=0){
			 abstracts =findrealabstract(tempabstract,index_tittle);}
			 else{
				 abstracts =null;
			 }
			System.out.println("tittle: " + temptittle);
			System.out.println("site: " + findrealurl(tempurl));
			System.out.println("abstract: " + findrealabstract(tempabstract,index_tittle));  
			
		}
		
	    } 
	
	
	public static String findrealurl(String url) throws IOException{
			Document doc = Jsoup.connect(url).userAgent("chrome").timeout(5000).get();
			String Tittle = doc.select("meta").get(0).attr("content");
			return Tittle.substring(7, Tittle.length()-1);
	}
	public static String findrealabstract(String str, int index){
		String temp = str;
		int end = str.indexOf("...");
		if(end>=0){
		return temp.substring(index, end);
		}
		else{
			return temp;
		}
	}
	
}
