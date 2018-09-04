package jsexa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.lang3.StringUtils;
public class testcheck {

	public static void main(String[] args) throws SQLException, InterruptedException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		checktrustfulwensites check =new checktrustfulwensites();
		chrome t =new chrome();
		MyDB mydb =new MyDB();
		layerone l1=new layerone();
		layertwo l2 =new layertwo();
		layerthree l3 =new layerthree();
		String text ="let's see what happened?";
//		System.out.println(text.replaceAll("？", "?"));
		recordwebsites rw =new recordwebsites();
//		System.out.println("text:\n"+l2.process_engine_body("ask", 0));
//		check.layerzero("search");
		chrome.testGoogleSearch();
//		check.layerzero("ask");
		checksuspicious cs =new checksuspicious();
//		mydb.updatesuspect(0, "test", "US");
//		System.out.println(mydb.getsuspecturl("US").keySet());
//		l2.check_websites_multiples();
//		System.out.println(l1.geturlfixed("www.baidu.com"));
//		System.out.println("text:\n"+l2.get_text_html(cs.openFF("US",0)));
//		System.out.println(mydb.loadkeywords("partial", 10, "aol"));
//		String s1 ="I am the very model of a modern Major-General, I've information vegetable, animal, and mineral,I know the kings of England, and I quote the fights historical,From Marathon to Waterloo, in order categorical.";
//		String s2 ="I am the very model of a cartoon individual,My animation's comical, unusual, and whimsical,I'm quite adept at funny gags, comedic theory I have read,From wicked puns and stupid jokes to anvils that drop on your head.";
//		comaprematching(s1,s2);
	}
	
	
	public static void comaprematching(String s1, String s2){
		System.out.println(StringUtils.getLevenshteinDistance(s1.toLowerCase(),s2));
		System.out.println(s1.length()+" "+s2.length());
		System.out.println(StringUtils.getJaroWinklerDistance(s1.toLowerCase(),s2));	
	}
	public String readfile(String fname) throws IOException{
		InputStream is = new FileInputStream(fname); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder(); 
		while(line != null){ 
			sb.append(line).append("\n"); 
			line = buf.readLine(); 
			} 
		String fileAsString = sb.toString().toLowerCase();
		return fileAsString; 
	}
	
	
	

}
