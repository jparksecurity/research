package jsexa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String[] args) throws Exception {

//		// TODO Auto-generated method stub
////		keyword keys =new keyword();
//		System.out.println("1");
//		CosineSimilarAlgorithm sim= new CosineSimilarAlgorithm();
//		System.out.println(sim.getSimilarity("汉2131231子", "汉而三213子"));
//		System.out.println("2");
//
        try {

//insert keyword into DB,Start searching
            bing bing = new bing(10);
            bing.run();
            googleResults google = new googleResults(10);
            google.run();
            test baidu = new test(10);
            baidu.run();
            Yandex yahoo = new Yandex(20);
            yahoo.run();
            googlesearch gs = new googlesearch();
            gs.main("Bank", "DE");

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

}
