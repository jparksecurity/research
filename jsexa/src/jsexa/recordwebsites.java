package jsexa;

import java.sql.SQLException;
import java.util.Stack;

import org.python.jline.internal.Log;

public class recordwebsites {
	MyDB mydb;
	
	
	
	public recordwebsites() throws ClassNotFoundException, SQLException{
		mydb = new MyDB();
		
	}
	
	public int updates_websites_total_num(int id) throws SQLException{
		
		return mydb.set_record_total_num(id);
		
		
	}
	public void updates_websites_total_num() throws SQLException{
		Stack<Integer> s =mydb.get_table_id("websites");
		while(!s.isEmpty()){
			mydb.set_record_total_num(s.pop());
		}
	}
	
	public void updateswebsites(String engine) throws SQLException{
		Stack<Integer> s = new Stack<Integer>();
		s=(Stack) mydb.get_unrecorded_websites("site", engine);
		int temp;
		while(!s.isEmpty()){
			temp =s.pop();
			if(mydb.updatewebsites(engine,temp)==0){
				Log.error("cannot update websites from "+engine+"'s "+temp);
				break;
			}
		}
	}
	
	
	
//return only domain name
	
	
}
