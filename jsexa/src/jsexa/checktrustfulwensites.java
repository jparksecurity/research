package jsexa;

import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

public class checktrustfulwensites {
	MyDB mydb;
	
	public checktrustfulwensites() throws ClassNotFoundException, SQLException{
		mydb= new MyDB();
	}
	
	
	
	public void layerzero(String engine) throws SQLException, InterruptedException{
		Stack<Integer> uncheckedlist = new Stack<Integer>();
		uncheckedlist=mydb.getuncheckeditems(engine);
		while(!uncheckedlist.isEmpty()){
			System.out.println("------------------------------");
			if(mydb.checkifchecked(uncheckedlist.peek(), engine)==0){
				int id =uncheckedlist.pop();
				String url =mydb.geturlByEID(id, engine);
				checkreapeat(url,engine,id);
			}else{
				uncheckedlist.pop();
			}
		}
	}
	
	public void checkreapeat(String url,String engine, int id) throws SQLException, InterruptedException{
		Stack<Integer> uncheckedlist = new Stack<Integer>();
		uncheckedlist =mydb.getrepeateditems(url, engine);
		boolean flag =uncheckedlist.size()>1;
		while(!uncheckedlist.isEmpty()){
			if(flag){
				mydb.updatescoreByID(uncheckedlist.pop(), engine, checklayerzero(id,engine)-1);
			}
			else{
				mydb.updatescoreByID(uncheckedlist.pop(), engine, checklayerzero(id,engine));
			}
		}
	}
	
	
	public int checklayerzero(int engine_id,String engine) throws SQLException, InterruptedException{
		
		String tittle =mydb.gettittleByEID(engine_id, engine);
		String abstracts=mydb.getabstractByEID(engine_id, engine);
		int keyword_id =mydb.getkeywordByEID(engine_id,engine);
		String keyword =mydb.getkeywordByID(keyword_id);
		String html = mydb.gethtmlByEID(engine_id, engine);
		
		if(html!=null)html=html.toLowerCase();
		if(abstracts!=null)abstracts=abstracts.toLowerCase();
		if(tittle!=null)tittle=tittle.toLowerCase();
		if(keyword!=null)keyword=keyword.toLowerCase();
		
		int score= checkTA(keyword,tittle,abstracts);
		TimeUnit.SECONDS.sleep(1);
		System.out.println("id:"+engine_id+"\tscore:"+score);
		
		return score+ checkhtml(keyword,html);
	}
	
	public int word_appears(String keyword, String html){
		int numNeedles = 0;
		int pos = html.indexOf(keyword);
		html=html.toLowerCase();
		int temp=0;
		while(pos >= 0 ){
			if(temp==pos-1)break;
			temp=pos;
			pos +=1;
			numNeedles += 1;
		    pos = html.indexOf(keyword,pos);
		    
		}
		return numNeedles;
	}
	
	
	public int checkhtml(String keyword,String html) throws SQLException{
		if(html==null)return 3;
		
		//TO-DO
		//if pass, pass; if not, go to deeper check
		 return (word_appears(keyword,html)>1)?0:furtherhtmlcheck(keyword,html);
	}
	
	/**
	 * 
	 * @param keyword
	 * @return the keyword without suffix
	 * @throws SQLException 
	 * 
	 */
	public String unsuffixized(String keyword) throws SQLException{
			Stack<String> s =mydb.get_suffiex();
			while(!s.isEmpty()){
				String temp =s.pop();
				int suffixlength = temp.length();
				if(keyword.contains(temp)&&keyword.length()-suffixlength>=4&&keyword.lastIndexOf(temp)==(keyword.length()-suffixlength)){
					return keyword.substring(0, keyword.length()-suffixlength);
				}
			}
			return keyword;
	}
	
	/**
	 * 
	 * @param keyword
	 * @param html
	 * @param num
	 * @return
	 * @throws SQLException
	 */
	public int furtherhtmlcheck(String keyword,String html) throws SQLException{		
		int result;
		int num=keyword.split(" ").length;
//		System.out.println(num);
		if(num==1){
			if( check_contains_unsuffixedword(keyword,html)){
				result=0;
			}
			else{
				result =3;
			}
		}else{
//			String key =mydb.getkeywordByID(1);
			String tkey[] =keyword.split(" ");
			//if(check(1)&&check(2)&&check(...))return 0 ;if(check(1)||check(2)||check(...))return 2. 
			boolean first=true,second=false;
			for(String s:tkey){
				if(check_contains_unsuffixedword(s,html)){
					second=true;
				}else{
					System.out.println("cannot find "+s);
					first=false;
				}
			}
			if(first){
				result= 0;
			}
			else if(second){
				result= 2;
			}
			else {
				result= 3;
			}
		}
		return result;
	}
	
	public boolean check_contains_unsuffixedword(String keyword,String html) throws SQLException{
		int length = keyword.length();
		String unsuffixized_keyword= unsuffixized(keyword);
		int count =0;
//		System.out.println("check_contains_unsuffixedword: "+keyword);
		if(length<6){
//			System.out.println("keyword smaller than 6");
			return (word_appears(unsuffixized_keyword,html)>=2);
		}else{
			String temp = keyword.substring(0, length/2);
			int htmllength = html.length();
			if(html.contains(temp)){
//				System.out.println("searching "+temp+" in html ");
				int index =0;
				int space =0;
//				System.out.println("index: "+index+" space: "+space);
				while(index<htmllength&&index>-1){
				
					String whole_word="";
					index =html.indexOf(temp,index+1);
					if(index==-1){
						break;
					}
//					System.out.println(html.substring(index));
					space = html.indexOf(" ", index);
//					System.out.println(index);
					if(space<htmllength&&space>0){
						whole_word = html.substring(index, space);
					}else{
						whole_word = html.substring(index);
					}
					
//				System.out.println("word is: "+whole_word);
					double getJaroWinklerDistance=StringUtils.getJaroWinklerDistance(whole_word.toLowerCase(),keyword);
					int getLevenshteinDistance=StringUtils.getLevenshteinDistance(whole_word.toLowerCase(),keyword);
//				System.out.println(getJaroWinklerDistance);
//				System.out.println(getLevenshteinDistance);
					if(getJaroWinklerDistance>0.8&&getLevenshteinDistance<6){
						count++;
					}
				}
			}else{
//				System.out.println("html does not contains first half");
				return false;
			}
		}
//		System.out.println(count);
		return (count>=2)?true:false;
	}
	
	public int checkTA(String keyword, String tittle,String abstracts){
		return (tittle.toLowerCase().contains(keyword)||abstracts.toLowerCase().contains(keyword))? 0:1;
	}
	
	
}
