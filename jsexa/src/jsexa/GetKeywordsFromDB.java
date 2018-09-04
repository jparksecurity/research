package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetKeywordsFromDB {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		String myDriver = "com.mysql.jdbc.Driver";
	    String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "root", "password");
	    Statement st = conn.createStatement();
	    ResultSet rs = st.executeQuery("Select * FROM google ");
	    
	    while (rs.next())
	      {
//	        int id = rs.getInt("B_ID");
	        String keyword = rs.getString("Keyword");
	        String lastName = rs.getString("Tittle");
//	        Date dateCreated = rs.getDate("date_created");
//	        boolean isAdmin = rs.getBoolean("is_admin");
//	        int numPoints = rs.getInt("num_points");
//	        
	        // print the results
	        System.out.format("%s:%s\n", keyword,lastName);
//	        System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
	      }
	}

}
