package jsexa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.*;

public class Yahoo implements Runnable {

    int index;
    String myDriver = "com.mysql.jdbc.Driver";
    String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    Statement st;

    public Yahoo(int givenindex) throws SQLException {
        index = givenindex;
        Connection conn = DriverManager.getConnection(myUrl, "root", "password");
        st = conn.createStatement();
    }

    private void result(String keyword, int no_of_results) throws Exception {

        System.out.println("Yahoo Keyword searching: " + keyword + "\n");
        String initial_keyword = keyword;
        keyword = keyword.replace(" ", "+");

        String url = "https://search.yahoo.com/search?p=" + keyword + "&n=" + no_of_results;
        //1-100
        Document doc = Jsoup.connect(url).userAgent("chrome").timeout(50000).get();

        String title, site, abstracts = "";
        Elements els = doc.select("div.dd.algo.algo-sr");
        for (Element el : els) {
            try {
                title = el.getElementsByTag("a").text();
                site = el.getElementsByTag("a").attr("href").toString();
                abstracts = el.getElementsByTag("p").text().toString();
                abstracts = abstracts.replaceAll("\"", "");

                System.out.println("Title : " + title);
                System.out.println("Site : " + site);
                System.out.println("Abstract : " + abstracts + "\n");

//                String html = new gethtml().gethtml(site);
//                html = html.replaceAll("\"", "");
//                System.out.println("html : " + "\n");

//			ResultSet rs = st.executeQuery("SELECT * FROM research.Keyword WHERE Keyword = \""+initial_keyword+"\"");
//			if(rs.next()){
//			System.out.println("Keyword_ID:"+rs.getInt("Keyword_ID"));}
//                st.executeUpdate("INSERT INTO Yahoo(keyword_Id,Keyword,Tittle,site,abstract,html) " +
//                        "VALUES ((SELECT Keyword_ID from Keyword where Keyword=\"" + initial_keyword + "\"),\"" + keyword + "\",\"" + temptittle + "\",\"" + site + "\",\"" + abstracts + "\",\"" + html + "\")  ");

            } catch (Exception e) {
                System.err.println("Got an exception! ");
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try {
            ResultSet rs = st.executeQuery("Select * FROM Keyword ");

            String keyword;
            while (rs.next()) {
                keyword = rs.getString("Keyword");
                result(keyword, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
