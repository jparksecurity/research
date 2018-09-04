package backup;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class testDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			String myDriver = "com.mysql.jdbc.Driver";
		    String myUrl = "jdbc:mysql://localhost:3306/research_v1?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
		    Class.forName(myDriver);
		    Connection conn = DriverManager.getConnection(myUrl, "root", "password");
		    
		    Statement st = conn.createStatement(); 
		   
		    ResultSet rs = st.executeQuery("SELECT Keyword_Id,keyword from Keyword");
		    while(rs.next()){
		    int first = rs.getInt("Keyword_Id");
	        String last = rs.getString("keyword");
	        Charset.forName("UTF-8").encode(last);
	        System.out.println(first+" "+last);
		    }
		    String line="中文测试12345";
		  //  Charset.forName("UTF-8").encode(line);
		    st.executeUpdate("INSERT INTO Keyword(Keyword) VALUES (\""+line+"\")"); 
	        conn.close();
		}catch (Exception e) { 
	        System.err.println("Got an exception! "); 
	        System.err.println(e.getMessage()); 
	    }
	}

}
//root@localhost:3306
//jdbc:mysql://localhost:3306/?user=root