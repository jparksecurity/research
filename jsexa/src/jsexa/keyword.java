package jsexa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class keyword {

	ArrayList<String> ary = new ArrayList<String>();
//	ArrayList<String> oldary = new ArrayList<String>();
	keyword() throws IOException, ClassNotFoundException, SQLException{
		
		String myDriver = "com.mysql.jdbc.Driver";
	    String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "root", "password");
	    Statement st = conn.createStatement(); 
	    ResultSet rs = st.executeQuery("Select * FROM Keyword ");
	    while (rs.next())
	      {
//	        int id = rs.getInt("B_ID");
	        String keyword = rs.getString("Keyword");
	        ary.add(keyword);
	        System.out.println("Loading keywords from DB, Adding:"+keyword);
	      }
		File fil =new File("keywords.txt");
		 InputStreamReader reader = new InputStreamReader(new FileInputStream(fil));
		 BufferedReader br = new BufferedReader(reader);
		 String line = "";  

         while ( (line = br.readLine())!=null) {  
          
            
             if(ary.contains(line)){
            	 System.out.println("duplicate keyword:"+line);
             }else if(line!=null){
            	 System.out.println("Loading keywords from Local, Adding:"+line);
            	 ary.add(line);
             st.executeUpdate("INSERT INTO Keyword(Keyword) " + 
	    		        "VALUES (\""+line+"\")"); 
             }
         } 
	}
	public ArrayList<String> getkeywords(){
		return ary;
	}
}
