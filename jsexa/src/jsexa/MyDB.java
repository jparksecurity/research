package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.mysql.jdbc.PreparedStatement;


public class MyDB {
    String myDriver = "com.mysql.jdbc.Driver";
    String myUrl = "jdbc:mysql://localhost:3306/research?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    Connection conn;

    public MyDB() throws SQLException, ClassNotFoundException {
        Class.forName(myDriver);
        conn = DriverManager.getConnection(myUrl, "root", "password");

    }

    public String simplizeurl(String url) {
        String temp = "";
        if (url.startsWith("https://")) {
            temp = url.substring(8);
        } else if (url.startsWith("http://")) {
            temp = url.substring(7);
        } else {
            temp = url;
        }
        return temp.contains("/") ? temp.substring(0, temp.indexOf("/")) : temp;

    }


    public int set_record_total_num(int id) throws SQLException {
        int total = 0;
        total += get_int_by_given_id("ask", "websites", id) + get_int_by_given_id("aol", "websites", id) + get_int_by_given_id("search", "websites", id)
                + get_int_by_given_id("lycos", "websites", id) + get_int_by_given_id("bing", "websites", id) + get_int_by_given_id("baidu", "websites", id);
        String sql = " update websites set total = " + total + " where websites = '" + id + "'";
        PreparedStatement st1 = (PreparedStatement) conn.prepareStatement(sql);
        st1.executeUpdate();
        return total;
    }

    //get websites of tables
    public Stack<String> getwebsites(String engine) throws SQLException {
        Statement st = conn.createStatement();
        Stack<String> stack = new Stack<String>();
        st.executeQuery("SELECT site FROM " + engine);
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            stack.push(rs.getString(1));
        }
        return stack;
    }

    public int get_int_by_given_id(String target, String table, int id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + target + " FROM " + table + " where " + table + " = " + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public String get_str_by_given_id(String target, String table, int id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + target + " FROM " + table + " where " + table + " = " + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public String get_str_bygivenstr(String target, String table, String column, String str) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + target + " FROM " + table + " where " + column + " = " + str);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public int get_int_bygivenstr(String target, String table, String column, String str) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + target + " FROM " + table + " where " + column + " = '" + str + "'");
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public boolean check_if_web_recorded(String url) throws SQLException {
        Statement st = conn.createStatement();
        simplizeurl(url);
        st.executeQuery("SELECT * FROM websites WHERE url = '" + simplizeurl(url) + "'");
        ResultSet rs = st.getResultSet();
        System.out.println(simplizeurl(url));
        return rs.next();
    }

    public int insert_new_web_2_websites(String url, String engine) throws SQLException {
        String sql = "insert into websites(url," + engine + ") value ('" + url + "',1);";
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(sql);
        return st.executeUpdate();
    }

    public int update_websites_engine_num(String engine, String url, int num) throws SQLException {
        num += 1;
        String sql = " update websites set " + engine + " = " + num + " where url = '" + url + "'";
        PreparedStatement st1 = (PreparedStatement) conn.prepareStatement(sql);
        return st1.executeUpdate();
    }

    public int updatewebsites(String engine, int id) throws SQLException {
        int num = 0;
        String url = get_str_by_given_id("site", engine, id);
        System.out.println("\n-----\nurl:" + url);
        url = simplizeurl(url);
        num = get_int_bygivenstr(engine, "websites", "url", url);
        System.out.println("num:" + num);

        System.out.println("url:" + url);
        if (check_if_web_recorded(url)) {
            System.out.println("contains url:" + url);
            return update_websites_engine_num(engine, url, num);
        } else {
            System.out.println("new come url:" + url);
            return insert_new_web_2_websites(url, engine);
        }
    }

    //returned id
    public Stack<Integer> get_unrecorded_websites(String target, String table) throws SQLException {
        Statement st = conn.createStatement();
        PreparedStatement st1;
        System.out.println("get_unrecorded_websites/ target:" + target + " table:" + table);
        Stack<Integer> i = new Stack<Integer>();
        st.executeQuery("SELECT " + table + " FROM " + table + " where flag = 0;");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            i.push(rs.getInt(1));
            String sql = "update " + table + " set flag = 1" + " where " + table + " = " + rs.getInt(1) + ";";
            st1 = (PreparedStatement) conn.prepareStatement(sql);
            st1.executeUpdate();
        }
        return i;
    }

    //get keyword from DB by given id
    public String getkeywordByID(int id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT keyword FROM keyword where ID=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }

    //get keyword from table by given id
    public int getkeywordByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT ID FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    //get abstract from DB with parameter engine name and id
    public String getabstractByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT abstract FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }

    //get checkscore from given table certain id
    public int getcheckscoreByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT checkscore FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public int getcountofengine(String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT count(" + engine + ") FROM " + engine);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    //get tittle from DB with parameter engine name and id
    public String gettittleByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT tittle FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }

    //get url from DB with parameter engine name and id
    public String geturlByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT site FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }

    //get html from DB with parameter engine name and id
    public String gethtmlByEID(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT html FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }


    public int update_website_mul(int id, int val) throws SQLException {
        String sql = " update websites set multiples = ?" + " where websites = ?;";
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(sql);
        st.setInt(1, val);
        st.setInt(2, id);
        int updateEXP_done = st.executeUpdate();
        if (updateEXP_done == 1) {
            return 1;
        }
        return 0;
    }

    //update checkscore to certain engine'id
    public String updatescoreByID(int id, String engine, int score) throws SQLException {
        String sql = " update " + engine + " set checkscore = ?" + " where " + engine + " = ?;";
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(sql);
        st.setInt(1, score);
        st.setInt(2, id);
        int updateEXP_done = st.executeUpdate();
        if (updateEXP_done == 1) {
            return "done";
        }
        return "";
    }

    // return all the unchecked items in parameter:engine as Stack<Integer>
    public Stack<Integer> getuncheckeditems(String engine) throws SQLException {
        Stack s = new Stack<Integer>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + engine + " FROM " + engine + " where checkscore=0");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            s.push(rs.getInt(1));
        }
        return s;
    }

    //check if exsists reapeated url with para @url @engine
    public Stack<Integer> getrepeateditems(String url, String engine) throws SQLException {
        Stack s = new Stack<Integer>();
        try {
            Statement st = conn.createStatement();
            st.executeQuery("SELECT " + engine + " FROM " + engine + " where site='" + url + "'");
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                s.push(rs.getInt(1));
            }
        } catch (Exception e) {

        }

        return s;
    }

    public Stack<String> get_suffiex() throws SQLException {
        Stack s = new Stack<Integer>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT suffixes FROM suffix");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            s.push(rs.getString(1));
        }
        return s;

    }

    public Stack<Integer> get_table_id(String table) throws SQLException {
        Stack s = new Stack<Integer>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT " + table + " FROM " + table);
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            s.push(rs.getInt(1));
        }
        return s;

    }

    //check certain item checked with para @engine @id
    public int checkifchecked(int id, String engine) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT checkscore FROM " + engine + " where " + engine + "=" + id);
        ResultSet rs = st.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }


    public Stack<String> loadkeywords(String input, int limits, String engine) throws SQLException {
        Stack<String> keywords = new Stack<String>();
        Stack<Integer> stack = new Stack<Integer>();
        Stack<String> partial = new Stack<String>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT keyword FROM keyword");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            keywords.push(rs.getString(1));
        }
        if (input.equals("all")) {
            return keywords;
        } else {
            st.executeQuery("SELECT * FROM " + engine + " GROUP BY ID HAVING  count(ID) < " + limits + ";");
            rs = st.getResultSet();
            while (rs.next()) {
                stack.push(rs.getInt(2));
            }
            while (!stack.isEmpty()) {
                partial.push(getkeywordByID(stack.pop()));
            }
            return partial;
        }
    }

    public Map<Integer, String> getsuspecturl(String ac) throws SQLException {
        System.out.println("getsuspecturl");
        Map<Integer, String> res = new HashMap<Integer, String>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT suspect_ID,site FROM suspect where " + ac + " is null");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            res.put(rs.getInt(1), rs.getString(2));
        }


        return res;
    }


    public Map<Integer, String> getsuspicious(String ac) throws SQLException {
        System.out.println("getsuspecturl");
        Map<Integer, String> res = new HashMap<Integer, String>();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT ID,URL FROM suspicious where " + ac + " is null");
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            res.put(rs.getInt(1), rs.getString(2));
        }


        return res;
    }

    public String updatesuspect(int id, String html, String AC) throws SQLException {
        String sql = " update suspect set " + AC + " = '" + html + "' where suspect_ID =" + id;
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(sql);
        int updateEXP_done = st.executeUpdate();
        if (updateEXP_done == 1) {
            return "done";
        }
        return "";
    }

    public String updatesuspicious(int id, String html, String html_t, String AC) throws SQLException {
        String sql = " update suspicious set " + AC + " = '" + html + "' where ID =" + id;
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(sql);
        int updateEXP_done = st.executeUpdate();
        if (updateEXP_done == 1) {
            System.out.println("finish update body");
            sql = " update suspicious set " + AC + "_T = '" + html_t + "' where ID =" + id;
            st = (PreparedStatement) conn.prepareStatement(sql);
            updateEXP_done = st.executeUpdate();
            if (updateEXP_done == 1) {
                System.out.println("finish update txt");
                return "done";
            } else {
                return "txt cannot update";
            }

        }
        return "";
    }

    //update Str Item
    public int update_item(String target, String engine, String value, int id, String strvalue, String locat) throws SQLException {
        if (id != 0) {
            String sql = " update " + engine + " set " + target + " = '" + value.replaceAll("'", "‘") + "' where " + engine + " = " + id;
            PreparedStatement st1 = (PreparedStatement) conn.prepareStatement(sql);
            return st1.executeUpdate();
        }
        String sql = " update " + engine + " set " + target + " = '" + value + "' where " + locat + " = '" + strvalue + "'";
        PreparedStatement st1 = (PreparedStatement) conn.prepareStatement(sql);
        return st1.executeUpdate();
    }


}
