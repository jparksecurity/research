package jsexa;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class googleResults implements Runnable {

    int index;
    String title, site, abstracts;

    public googleResults(int no_of_results) {

        this.index = no_of_results;
    }

    public static void google_results(String keyword, int no_of_results) throws Exception {
//Replace space by + in the keyword as in the google search url

        String myDriver = "com.mysql.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "root", "password");

//    st.executeUpdate("INSERT INTO Keyword(keyword) " + 
//        "VALUES (\""+keyword+"\")"); 
        String initialkeyword = keyword;
        keyword = keyword.replace(" ", "+");
        String url = "https://www.google.com/search?q=" + keyword + "&num=" + String.valueOf(no_of_results);
        //num = 10, 20, 30, 40, 50, and 100
//Connect to the url and obain HTML response
        Document doc = Jsoup.connect(url).userAgent("chrome").timeout(5000).get();
//parsing HTML after examining DOM
        String title = "", site = "", abstracts = "";
        Elements els = doc.select("#res .g");
        System.out.println("keyword searching: " + keyword + "\n");
        for (Element el : els) {
            try {
                for(int i = 0; site.indexOf("/url?q") < 0; i++){
                    if(i >= el.getElementsByTag("a").eachText().size() || i >= el.getElementsByTag("a").eachAttr("abs:href").size()){
                        throw new Exception("It was a map");
                    }
                    title = el.getElementsByTag("a").eachText().get(i);
                    site = el.getElementsByTag("a").eachAttr("abs:href").get(i);
                }
                site = site.substring(site.indexOf("http", 1), site.indexOf("&sa"));
                site = URLDecoder.decode(site, "UTF-8");
//                StringBuilder output = new StringBuilder();
//                int i;
//                while ( (i = site.indexOf("%")) >= 0) {
//                    output.append(site.substring(0, i));
//                    output.append((char)Integer.parseInt(site.substring(i+1, i+3), 16));
//                    output.append(site.substring(i+3));
//                    site = output.toString();
//                }
//                System.out.println(output);
                abstracts = el.getElementsByTag("span").text().toString();
                abstracts = abstracts.replaceAll("\"", "");

                System.out.println("Title : " + title);
                System.out.println("Site : " + site);
                System.out.println("Abstract : " + abstracts + "\n");

                String html = new gethtml().gethtml(site);
                html = html.replaceAll("\"", "");
                System.out.println("html : " + "\n");
                Statement st1 = conn.createStatement();
                st1.executeUpdate("INSERT INTO Google(keyword_Id,Keyword,Tittle,site,abstract,html) " +
                        "VALUES ((SELECT Keyword_ID from Keyword where Keyword=\"" + initialkeyword + "\"),\"" + keyword + "\",\"" + title + "\",\"" + site + "\",\"" + abstracts + "\",\"" + html + "\")  ");
            } catch (Exception e) {
                System.err.println("Got an exception! ");
                System.err.println("keyword searching:" + keyword);
                System.err.println(e.getMessage());
            }
        }
        conn.close();


    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "password");

            Statement st = conn.createStatement();
//    
//st.executeUpdate("INSERT INTO Keyword(keyword) " + 
//    "VALUES (\""+keyword+"\")"); 
            ResultSet rs = st.executeQuery("Select * FROM Keyword ");

            String keyword;
            while (rs.next()) {
                keyword = rs.getString("Keyword");
                google_results(keyword, index);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}