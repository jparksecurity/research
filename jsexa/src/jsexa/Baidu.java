package jsexa;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Baidu implements Runnable {

    int index;
    String myDriver = "com.mysql.jdbc.Driver";
    String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    Statement st;

    public Baidu(int givenindex) throws SQLException {
        index = givenindex;
        Connection conn = DriverManager.getConnection(myUrl, "root", "password");
        st = conn.createStatement();
    }

    private void baidu_result(String keyword, int no_of_results) throws Exception {

        System.out.println("Baidu Keyword searching: " + keyword + "\n");
        String initial_keyword = keyword;
        keyword = keyword.replace(" ", "+");

        String url = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd=" + keyword + "&rn=" + no_of_results;
        //1-50
        Document doc = Jsoup.connect(url).userAgent("chrome").timeout(50000).get();

        String tempurl, tempabstract, temptittle, site, abstracts = "";
        Elements els = doc.select("#content_left .t");
        for (Element el : els) {
            try {
                tempurl = el.getElementsByTag("a").attr("href");
                // I need to get first two
                tempabstract = el.parent().getElementsByClass("c-abstract").text();
                temptittle = el.text();
                int index_tittle = temptittle.length();
                site = findrealurl(tempurl);
                gethtml gh = new gethtml();
                String html = gh.gethtml(site);
                html = html.replaceAll("\"", "");
                // System.out.println(index+"\t"+no_of_results+"\t"+index_tittle);
                if (index_tittle >= 0) {
                    abstracts = findrealabstract(tempabstract, index_tittle).toString();
                    //System.out.println("if condition done");
                } else {
                    abstracts = null;
                }
                Charset.forName("UTF-8").encode(temptittle);
                System.out.println("tittle: " + temptittle);
                System.out.println("site: " + site);
                abstracts = abstracts.replaceAll("\"", "");
                System.out.println("abstract: " + abstracts);
//			ResultSet rs = st.executeQuery("SELECT * FROM research.Keyword WHERE Keyword = \""+initial_keyword+"\"");
//			if(rs.next()){
//			System.out.println("Keyword_ID:"+rs.getInt("Keyword_ID"));}
//                st.executeUpdate("INSERT INTO Baidu(keyword_Id,Keyword,Tittle,site,abstract,html) " +
//                        "VALUES ((SELECT Keyword_ID from Keyword where Keyword=\"" + initial_keyword + "\"),\"" + keyword + "\",\"" + temptittle + "\",\"" + site + "\",\"" + abstracts + "\",\"" + html + "\")  ");

            } catch (Exception e) {
                System.err.println("Got an exception! ");
                System.err.println(e.getMessage());
            }
        }
    }

    public static String findrealurl(String url) throws IOException, InterruptedException {
        URL resourceUrl, base, next;
        HttpURLConnection conn;
        String location;

        while (true)
        {
            resourceUrl = new URL(url);
            conn        = (HttpURLConnection) resourceUrl.openConnection();

            conn.setConnectTimeout(150000);
            conn.setReadTimeout(150000);
            conn.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections
            conn.setRequestProperty("User-Agent", "Mozilla/5.0...");

            switch (conn.getResponseCode())
            {
                case HttpURLConnection.HTTP_MOVED_PERM:
                    location = conn.getHeaderField("Location");
                    if(URLDecoder.decode(location, "UTF-8") != location)
                        location = URLDecoder.decode(location, "UTF-8");
                    else {
                        location = URLEncoder.encode(location, "ISO-8859-1");
                        location = URLDecoder.decode(location, "UTF-8");
                    }
                    base     = new URL(url);
                    next     = new URL(base, location);  // Deal with relative URLs
                    url      = next.toExternalForm();
                    continue;
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    location = conn.getHeaderField("Location");
                    location = URLDecoder.decode(location, "UTF-8");
                    base     = new URL(url);
                    next     = new URL(base, location);  // Deal with relative URLs
                    url      = next.toExternalForm();
                    continue;
            }

            break;
        }

        return url;
    }

    public static String findrealabstract(String str, int index) {
        String temp = str;
        int end = str.lastIndexOf("...");
        //System.out.println("temp:"+temp+" end:"+end +" String length:"+temp.length());
        if (end >= 0) {
            //System.out.println("returning:");
            String result = temp.substring(index, end);
            //System.out.println("returning:"+  result);
            return result;
        } else {
            return temp;
        }
    }

    public String changeCharset(String str)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用源字符编码解码字符串
            byte[] bs = str.getBytes();
            return new String(bs, "UTF-8");
        }
        return null;
    }

    @Override
    public void run() {
        try {
            ResultSet rs = st.executeQuery("Select * FROM Keyword ");

            String keyword;
            while (rs.next()) {
                keyword = rs.getString("Keyword");
                baidu_result(keyword, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
