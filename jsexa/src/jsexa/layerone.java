package jsexa;

public class layerone {
	public layerone(){
		
	}
	public String geturlfixed(String url){
		
		return url.startsWith("http")? url:"http://".concat(url);
		
		
	}

}
