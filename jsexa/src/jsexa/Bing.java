package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Bing {
    int index;
    String title, site, abstracts;

    public Bing(int no_of_results) {

        this.index = no_of_results;
    }

    public static void bing_results(String keyword, int no_of_results) throws Exception {

        String myDriver = "com.mysql.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "root", "password");

        String initialkeyword = keyword;
        keyword = keyword.replace(" ", "+");
        String url = "https://www.bing.com/search?q=" + keyword + "&count=" + String.valueOf(no_of_results);
        //1-100
        //Connect to the url and obain HTML response
        Document doc = Jsoup.connect(url).userAgent("chrome").timeout(5000).get();
//		   System.out.println( doc.html());
        //parsing HTML after examining DOM
        String title, site, abstracts = "";
        Elements els = doc.select("#b_content .b_algo");
        for (Element el : els) {
            try {
                System.out.println("keyword searching: " + keyword + "\n");

                title = el.getElementsByTag("a").text();
                site = el.getElementsByTag("a").attr("href").toString();
                abstracts = el.getElementsByTag("p").text().toString();
                abstracts = abstracts.replaceAll("\"", "");

                System.out.println("Title : " + title);
                System.out.println("Site : " + site);
                System.out.println("Abstract : " + abstracts + "\n");

                String html = new gethtml().gethtml(site);
                html = html.replaceAll("\"", "");
                System.out.println("html : " + "\n");

                Statement st1 = conn.createStatement();
                st1.executeUpdate("INSERT INTO Bing(keyword_Id,Keyword,Tittle,site,abstract,html) " +
                        "VALUES ((SELECT Keyword_ID from Keyword where Keyword=\"" + initialkeyword + "\"),\"" + keyword + "\",\"" + title + "\",\"" + site + "\",\"" + abstracts + "\",\"" + html + "\")  ");

            } catch (Exception e) {
                System.err.println("Got an exception! ");
                System.err.println("keyword searching:" + keyword);
                System.err.println(e.getMessage());
            }
        }
        conn.close();
    }

    public void run() {
        // TODO Auto-generated method stub
        try {
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "password");

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("Select * FROM Keyword ");

            String keyword;
            while (rs.next()) {
                keyword = rs.getString("Keyword");
                bing_results(keyword, index);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
