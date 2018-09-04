package backup;

import java.io.IOException;
import java.sql.Statement;

import jsexa.gethtml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class testGooglefetching {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String url = "https://www.google.com/search?q=bitcoin&num=10";
        //Connect to the url and obain HTML response
        Document doc = Jsoup.connect(url).userAgent("Mozilla/17.0").timeout(30000).get();

        Elements els = doc.select("#res .g");
        String title = "", site = "", abstracts = "";
        for (Element el : els) {
//				System.out.println("keyword searching: " + keyword + "\n");
            //Statement st1 = conn.createStatement();

            //System.out.println(el);
            try {
                for(int i = 0; site.indexOf("/url?q") < 0; i++){
                    title = el.getElementsByTag("a").eachText().get(i);
                    site = el.getElementsByTag("a").eachAttr("abs:href").get(i);
                }
                System.out.println("Title : " + title);
                System.out.println("ABSSite : " + site);
                site = site.substring(site.indexOf("http", 1), site.indexOf("&sa"));
                int i;
                while ((i = site.indexOf("%")) >= 0) {
                    StringBuilder output = new StringBuilder();
                    output.append(site.substring(0, i));
                    output.append((char) Integer.parseInt(site.substring(i + 1, i + 3), 16));
                    output.append(site.substring(i + 3));
                    site = output.toString();
                }
                abstracts = el.getElementsByTag("span").text().toString();
                System.out.println("Site : " + site);
                abstracts = abstracts.replaceAll("\"", "");
                System.out.println("Abstract : " + abstracts);

                gethtml gh = new gethtml();
                String html = gh.gethtml(site);
                html = html.replaceAll("\"", "");
                System.out.println();
            } catch (Exception e) {
                System.err.println("Got an exception! ");
                System.err.println("keyword searching: bitcoin");
                System.err.println(e.getMessage());
            }
            //System.out.println(html);
//				st1.executeUpdate("INSERT INTO Google(keyword_Id,Keyword,Tittle,site,abstract,html) " + 
//				        "VALUES ((SELECT Keyword_ID from Keyword where Keyword=\""+initialkeyword+"\"),\""+keyword+"\",\""+title+"\",\""+site+"\",\""+abstracts+"\",\""+html+"\")  "); 

        }
//			conn.close(); 
    }

}
