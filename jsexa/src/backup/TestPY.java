package backup;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.python.core.*; 
public class TestPY {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		PythonInterpreter python = new PythonInterpreter();
//		 
//		int number1 = 10;
//		int number2 = 32;
//		 
//		python.set("number1", new PyInteger(number1));
//		python.set("number2", new PyInteger(number2));
//		python.exec("number3 = number1+number2");
//		PyObject number3 = python.get("number3");
//		System.out.println("val : "+number3.toString());
		try{
//		String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
//		BufferedWriter out = new BufferedWriter(new FileWriter("test1.py"));
//		out.write(prg);
//		out.close();
		int number1 = 10;
		
		int number2 = 32;
//		Process p = Runtime.getRuntime().exec("python test2.py 'hello' 'http://icanhazip.com!' ");
		System.out.println("Start");
//		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String ret = "";
		
		StringWriter writer = new StringWriter(); //ouput will be stored here
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptContext context = new SimpleScriptContext();
	    context.setWriter(writer); //configures output redirection
	    ScriptEngine engine = manager.getEngineByName("python");
	    engine.eval(new FileReader("test2.py"), context);
	    System.out.println(writer.toString()); 
		}catch(Exception e){}
		}
	}


