package jsexa;

import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class checkcontent {

	public Map<Integer,Double> checkhtml(String engine) throws ClassNotFoundException, SQLException{
		CosineSimilarAlgorithm sim= new CosineSimilarAlgorithm();
		MyDB db = new MyDB();
		
		Stack<Integer> id=db.get_table_id(engine);
		Map<Integer,Double> m = new HashMap<Integer,Double>();
		
		for(int tid: id){
			String url=db.get_str_by_given_id("site", engine, tid);
			String html=db.get_str_by_given_id("html", engine, tid);
			if(sim.getSimilarity(html,geturl(url))<0.5){
				m.put(tid, sim.getSimilarity(html,geturl(url)));
			}
			
		}
		return m;
		
	}
	
	
	public String geturl(String url){
		URLConnection connection = null;
		 String content="";
		try {
		  connection =  new URL("http://www.google.com").openConnection();
		  Scanner scanner = new Scanner(connection.getInputStream());
		  scanner.useDelimiter("\\Z");
		  content = scanner.next();
		}catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		return content;
	}
}
