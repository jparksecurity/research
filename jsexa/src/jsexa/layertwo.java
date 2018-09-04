package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Stack;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class layertwo {
	MyDB mydb;
	String[] mystack;
	public layertwo() throws ClassNotFoundException, SQLException{
		mydb =new MyDB();
		mystack =new String[]{
				"baidu","bing","ask","aol","search","lycos"
		};
		
	}
	public String get_text_html(String html){

			Document doc =Jsoup.parse(html);
			String text = doc.body().text().replaceAll("？", "?");
			return text;
	
	}
	
	public String get_text_html_wo(String html){
		String text =html.replaceAll("'", "‘");
		
		
		return text;
	}
	
	
	
	public int process_engine_body(String engine,int id) throws SQLException{
		Stack<Integer> stack =new Stack<Integer>();
		
		int Temp_id=0;
		if(id!=0){
			String html = mydb.gethtmlByEID(id, engine);
			String body =get_text_html(html);
//			System.out.println(body);
			return mydb.update_item("html_body", engine, body, id, "", "");
		}
		stack =mydb.get_table_id(engine);
		boolean flag = stack.isEmpty();
		while(!stack.isEmpty()){
			try{
				Temp_id =stack.pop();
				String html = mydb.gethtmlByEID(Temp_id, engine);
				String body =get_text_html(html);
				System.out.println(engine+"\t"+Temp_id);
				 mydb.update_item("html_body", engine, body, Temp_id, "", "");
			}catch(Exception e){
				mydb.update_item("html_body", engine, "cannaot transfer", Temp_id, "", "");
			}
			
		}
		return flag? 0:1;
	}
	
	public enum engines_name{
		baidu,bing,lycos,ask,aol,search
	}
	
	public void check_websites_multiples() throws SQLException{
		Stack<Integer> s=mydb.get_table_id("websites");
		String[] temp_stack =this.mystack;
		int count =0;
		while(!s.isEmpty()){
			count=0;
			int cur_id =s.pop();
			for(String str:temp_stack){
				
				if(mydb.get_int_by_given_id(str, "websites",cur_id)>0){
					count+=1;
				};
			}
			//update 
			System.out.println(cur_id+":"+count);
			mydb.update_website_mul(cur_id, count);
		}
		
		
	}
}
