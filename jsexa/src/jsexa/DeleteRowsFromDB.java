package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

public class DeleteRowsFromDB {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
//		try{
		String myDriver = "com.mysql.jdbc.Driver";
		String myUrl = "jdbc:mysql://127.0.0.1:3306/sys?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
		System.out.println("hello world");
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "root", "password");
	    Statement st = conn.createStatement();
//	    System.out.println("hello world");
	    st.executeQuery("SELECT * FROM trustfulwebsites"); 
	    ResultSet rs = st.getResultSet();
	    while (rs.next()) {
//	        System.out.println(rs.getString(2)); //gets the first column's rows.
	        
	    }
	    MyDB mydb =new MyDB();
//	  System.out.println(  mydb.getkeywordByID(2));
//	    System.out.println(  mydb.getabstractByEID(2519,"bing"));
	  System.out.println(  mydb.checkifchecked(1,"test"));
//	    String url="";
//	 Stack<Integer> s = mydb.getrepeateditems("'"+url+"'","bing");
//	  for(int i: s){
//		  System.out.println(i);
//	  }
	  
//		}catch(Exception e){
	  
	  
//			 System.out.println(e.getMessage());
//		}

}}
